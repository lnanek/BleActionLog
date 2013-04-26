/************************************************************************************
 *
 *  Copyright (C) 2013 HTC (modified)
 *  Copyright (C) 2009-2011 Broadcom Corporation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ************************************************************************************/
package com.htc.sample.bleexample;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.widget.Toast;

import com.htc.android.bluetooth.le.gatt.BleAdapter;
import com.htc.android.bluetooth.le.gatt.BleClientProfile;
import com.htc.sample.bleexample.constants.BleActions;
import com.htc.sample.bleexample.profiles.ExampleActions;
import com.htc.sample.bleexample.profiles.ProfileClientFactory;

/**
 * Connects to a device.
 *
 */
public class ConnectActivity extends Activity {
	
	public static final String ACTION_STATUS_MESSAGE = 
			ConnectActivity.class.getName() + ".ACTION_STATUS_MESSAGE";
	
	public static final String STATUS_MESSAGE_EXTRA = 
			ConnectActivity.class.getName() + ".STATUS_MESSAGE_EXTRA";
	
	private static final int CUSTOM_SCAN_INTERVAL_MS = 2 * 1000;
	
	private static final int CUSTOM_SCAN_WINDOW_MS = 2 * 1000;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(final Context context, final Intent intent) {
			runOnUiThread(new Runnable() {
				public void run() {
					if ( intent.getAction().equals(BleActions.ACTION_INITIALIZED) ) {
						mUi.appendStatus("BLE Profile initialized\n");
					} else if ( intent.getAction().equals(BleActions.ACTION_INITIALIZE_FAILED) ) {
						mUi.appendStatus("BLE Profile failed to initialize.\n");
						mUi.appendStatus("Restart Bluetooth and try again.\n");
					} else if ( intent.getAction().equals(BleActions.ACTION_REGISTERED) ) {
						mUi.appendStatus("BLE Profile registered.\n");
						mUi.appendStatus("Choose a connection option...\n");
						mUi.setConnectionButtonsEnabled(true);
					} else if ( intent.getAction().equals(BleActions.ACTION_CONNECTED) ) {
						mUi.appendStatus("BLE Profile connected\n");
						mUi.appendStatus("Attempting to refresh...\n");
					} else if ( intent.getAction().equals(BleActions.ACTION_REFRESHED) ) {
						mUi.appendStatus("BLE Profile refreshed\n");
						mForegroundConnectionAttempter.removeMessages(ATTEMPT_CONNECTION_MESSAGE);
						mUi.appendStatus("Ready for GATT actions...\n");
						mUi.setProfileButtonsEnabled(true);
					} else if ( intent.getAction().equals(BleActions.ACTION_NOTIFICATION) ) {
						mUi.appendStatus("Characteristic changed.\n");
					} else if ( intent.getAction().equals(BleActions.ACTION_DISCONNECTED) ) {
						mUi.appendStatus("BLE Profile disconnected\n");
					} else if ( intent.getAction().equals(ACTION_STATUS_MESSAGE) ) {
						final String message = intent.getStringExtra(STATUS_MESSAGE_EXTRA);
						mUi.appendStatus(message + "\n");
					}
				}
			});
		}
	};
	
	private static final String LOG_TAG = ConnectActivity.class.getName();

	private static final int REQUEST_ENABLE_BT = 1;
	
	private static final int ATTEMPT_CONNECTION_MESSAGE = 0;

	private BleClientProfile mClient;

	private ConnectUi mUi;

	private BluetoothAdapter mAdapter;

	private Prefs mPrefs;
	
	private Handler mForegroundConnectionAttempter = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			performForegroundConnectionAttempt(msg.arg1);			
		}
	};
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mUi = new ConnectUi(this);

		mPrefs = new Prefs(this);
		if ( null != mPrefs.getDeviceDisplayName() ) {
			mUi.mDeviceNameView.setText(mPrefs.getDeviceDisplayName());
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		configure();
	}

	void configure() {
		Log.i(LOG_TAG, "configure");
				
		// Make sure Bluetooth enabled.
		if (!mAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			return;
		}
		
		// Make sure device setup.
		if ( null == mPrefs.getDeviceAddress() ) {
			final Intent intent = new Intent(this, ConnectionPreferenceActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		if ( mPrefs.isUseCustomScanParameters() ) {
			final BleAdapter bleAdapter = new BleAdapter(this) {
				@Override
				public void onInitialized(boolean success) {
					mUi.appendStatus("BleAdapter onInitialized: " + success + "\n");
					Log.i(LOG_TAG, "BleAdapter onInitialized: " + success);
					super.onInitialized(success);
					setScanParameters(CUSTOM_SCAN_INTERVAL_MS, CUSTOM_SCAN_WINDOW_MS);
				}
			};
			bleAdapter.init();			
		}
						
		// Create, initialize, register GATT profile client if needed.
		if (null == mClient) {
									
			// Listen for actions from the profile client.
			final IntentFilter filter = new IntentFilter();
			filter.addAction(BleActions.ACTION_INITIALIZED);
			filter.addAction(BleActions.ACTION_INITIALIZE_FAILED);
			filter.addAction(BleActions.ACTION_REGISTERED);
			filter.addAction(BleActions.ACTION_CONNECTED);
			filter.addAction(BleActions.ACTION_REFRESHED);
			filter.addAction(BleActions.ACTION_NOTIFICATION);
			filter.addAction(BleActions.ACTION_DISCONNECTED);
			filter.addAction(ACTION_STATUS_MESSAGE);
			registerReceiver(mReceiver, filter);
			
			//Create client.
			mUi.appendStatus("Attempting to register...\n");
			mClient = ProfileClientFactory.getClient(this);
		}
		
		if ( !(mClient instanceof ExampleActions) ) {
			mUi.setFirstProfileButton(false, false, "Action");
			mUi.setSecondProfileButton(false, false, "Action");
			return;
		}
		
		final ExampleActions exampleClient = (ExampleActions) mClient;
		if ( null != exampleClient.getFirstActionName() ) {
			mUi.setFirstProfileButton(true, false, exampleClient.getFirstActionName());
		}
		if ( null != exampleClient.getSecondActionName() ) {
			mUi.setSecondProfileButton(true, false, exampleClient.getSecondActionName());
		}
	}

	@Override
	public void onStop() {
		Log.i(LOG_TAG, "onStop");
		
		clearConnections();
				
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// XXX Does deregistering profile take a certain amount of time?
		// Might cause trouble if restart app within that time period?
		clearProfile();		
	}
	
	private BluetoothDevice getDevice() {
		Log.i(LOG_TAG, "getDevice");
		
		final String address = mPrefs.getDeviceAddress();
		if (null == mAdapter || null == address) {
			return null;
		}

		return mAdapter.getRemoteDevice(address);
	}


	private void clearConnections() {
		Log.i(LOG_TAG, "clearConnections");

		mForegroundConnectionAttempter.removeMessages(ATTEMPT_CONNECTION_MESSAGE);
		
		final BluetoothDevice device = getDevice();
		if (null == mClient || null == device) {
			return;
		}
		mClient.cancelBackgroundConnection(device);
		mClient.disconnect(device);
	}

	private void clearProfile() {
		Log.i(LOG_TAG, "clearProfile");

		if ( null != mReceiver ) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		
		if (null == mClient) {
			return;
		}

		mClient.deregisterProfile();
		mClient = null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(LOG_TAG, "onActivityResult");
		
		// User returned from prompt to enable Bluetooth.
		if (requestCode == REQUEST_ENABLE_BT) {
			// User did not enable Bluetooth.
			if (resultCode != Activity.RESULT_OK) {
				Toast.makeText(this,
						"User decided not to enable Bluetooth - exiting...",
						Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			// User did enable Bluetooth.
			configure();
			return;
		}
	}

	void onFirstActionPressed() {
		Log.i(LOG_TAG, "onFirstActionPressed");
		final BluetoothDevice device = getDevice();
		if ( null == device || null == mClient ) {
			return;
		}

		if ( mClient instanceof ExampleActions ) {
			final ExampleActions example = (ExampleActions) mClient;
			example.performFirstAction(getDevice());
		}
	}

	void onSecondActionPressed() {
		Log.i(LOG_TAG, "onSecondActionPressed");
		final BluetoothDevice device = getDevice();
		if ( null == device || null == mClient ) {
			return;
		}

		if ( mClient instanceof ExampleActions ) {
			final ExampleActions example = (ExampleActions) mClient;
			example.performSecondAction(getDevice());
		}
	}

	void cancelBackgroundConnection() {
		Log.i(LOG_TAG, "cancelBackgroundConnection");
		clearConnections();
	}

	void startForegroundConnectionAttempts() {
		Log.i(LOG_TAG, "connectForeground");
		mForegroundConnectionAttempter.removeMessages(ATTEMPT_CONNECTION_MESSAGE);
		for ( int connectionAttempts = 0; 
				connectionAttempts < mPrefs.getConnectionAttempts(); connectionAttempts++ ) {
			final int delayMs = connectionAttempts * mPrefs.getRetryRateS() * 1000;

			Log.i(LOG_TAG, "scheduling attempt " + (connectionAttempts + 1) + " in: " + delayMs);
			
			final Message message = mForegroundConnectionAttempter.obtainMessage();
			message.what = ATTEMPT_CONNECTION_MESSAGE;
			message.arg1 = connectionAttempts;
			mForegroundConnectionAttempter.sendMessageDelayed(message, delayMs);
		}
	}

	private void performForegroundConnectionAttempt(final int connectionAttempts) {
		Log.i(LOG_TAG, "performForegroundConnectionAttempt: attempt " + (connectionAttempts + 1)
				+ " of  " + mPrefs.getConnectionAttempts());
		
		final BluetoothDevice device = getDevice();
		
		if ( null == device ) {
			mUi.appendStatus("Aborting connection attempts. No device selected.\n");
			mForegroundConnectionAttempter.removeMessages(ATTEMPT_CONNECTION_MESSAGE);
			return;
		}
		
		if ( null == mClient ) {
			mUi.appendStatus("Aborting connection attempts. Profile not initialized.\n");
			mForegroundConnectionAttempter.removeMessages(ATTEMPT_CONNECTION_MESSAGE);
			return;
		}
		
		if ( !mClient.isProfileRegistered() ) {
			mUi.appendStatus("Aborting connection attempts. Profile not registered.\n");
			mForegroundConnectionAttempter.removeMessages(ATTEMPT_CONNECTION_MESSAGE);
			return;
		}
		
		try {
			mClient.connect(device);
		} catch (Exception e) {
			Log.e(LOG_TAG, "error connecting", e);
		}
	}

	void connectBackground() {
		Log.i(LOG_TAG, "connectBackground");
		try {
			mClient.connectBackground(getDevice());
		} catch (Exception e) {
			Log.e(LOG_TAG, "error background connecting", e);
		}
	}

	void onSetupDeviceClicked() {
		Log.i(LOG_TAG, "onSetupDeviceClicked");

		final Intent intent = new Intent(this, ConnectionPreferenceActivity.class);
		startActivity(intent);
		finish();
	}
	
	public static void broadcast(final Context aContext, 
			final String aAction, final BluetoothDevice aDevice) {
		Intent intent = new Intent();
		intent.setAction(aAction);
		if (null != aDevice) {
			intent.putExtra(BluetoothDevice.EXTRA_DEVICE, aDevice.getAddress());
		}
		aContext.sendBroadcast(intent);
	}
	
	public static void broadcastStatus(final Context aContext, final String aStatus) {
		Intent intent = new Intent();
		intent.setAction(ConnectActivity.ACTION_STATUS_MESSAGE);
		if ( null != aStatus ) {
			intent.putExtra(ConnectActivity.STATUS_MESSAGE_EXTRA, aStatus);
		}
		aContext.sendBroadcast(intent);		
	}

}