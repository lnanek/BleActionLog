/************************************************************************************
 *
 *  Copyright (C) 2013 HTC
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.htc.sample.bleexample.constants.BleServices;
import com.htc.sample.bleexample.constants.BleUtils;

/**
 * Configures preferences for which BLE device to connect to and how.
 *
 */
public class ConnectionPreferenceActivity extends PreferenceActivity {
	
	private static final String LOG_TAG = ConnectionPreferenceActivity.class.getSimpleName();

	private static final int REQUEST_ENABLE_BT = 1;

	private static final int REQUEST_SELECT_DEVICE = 2;

	private static final int REQUEST_SELECT_DEVICE_AND_SAVE = 3;
	
	private PreferenceManager mManager;
	
	private Preference mDevicePref;
	
	private Preference mConnectionAttemptsPref;
	
	private Preference mRetryRateSPref;
	
	private Preference mEncryptionPref;
	
	private Preference mServicePref;
	
	private Preference mUseCustomScanParametersPref;
	
	private BluetoothAdapter mAdapter;
	
	private SharedPreferences mSharedPrefs;
	
	private Prefs mPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    
		
		// Setup member variables
		mPrefs = new Prefs(this);
        addPreferencesFromResource(R.xml.preferences);
		mManager = getPreferenceManager();
		mSharedPrefs = mManager.getSharedPreferences();
		
		// Setup UI
		mDevicePref = mManager.findPreference("device");
		mDevicePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent newIntent = new Intent(ConnectionPreferenceActivity.this,
						SelectDeviceActivity.class);
				startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);	
				return true;
			}
		});
		mConnectionAttemptsPref = mManager.findPreference("connectionAttempts");
		mRetryRateSPref = mManager.findPreference("retryRateS");
		mEncryptionPref = mManager.findPreference("encryption");
		mServicePref = mManager.findPreference("service");
		mUseCustomScanParametersPref = mManager.findPreference("useCustomScanParameters");	
		
		// Make sure Bluetooth supported.
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		if (null == mAdapter) {
			Toast.makeText(this, "Bluetooth is not available - exiting...",
						Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		// Make sure Bluetooth enabled.
		if (!mAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
	}

	protected void updateSummaryValues() {
		
		mDevicePref.setSummary(mSharedPrefs.getString(mDevicePref.getKey(), ""));
		mConnectionAttemptsPref.setSummary(mSharedPrefs.getString(mConnectionAttemptsPref.getKey(), "1"));
		mRetryRateSPref.setSummary(mSharedPrefs.getString(mRetryRateSPref.getKey(), "5"));
		
		final int encryptionIndex = Integer.parseInt(mSharedPrefs.getString(mEncryptionPref.getKey(), "0"));
		final CharSequence encryptionLabel = getResources().getTextArray(R.array.pref_encryption_entries)[encryptionIndex];
		mEncryptionPref.setSummary(encryptionLabel);
		
		final String serviceValue = mSharedPrefs.getString(mServicePref.getKey(), 
				BleServices.IMMEDIATE_ALERT);
		final String serviceLabel = BleUtils.getLabelForUuid(serviceValue);
		mServicePref.setSummary(serviceLabel);
		
		final boolean useCustomScanParameters = mSharedPrefs.getBoolean(
				mUseCustomScanParametersPref.getKey(), false);
		mUseCustomScanParametersPref.setSummary(useCustomScanParameters ? "True" : "False");
	}
	
	@Override
	public void onWindowFocusChanged(final boolean aHasFocus) {
		super.onWindowFocusChanged(aHasFocus);
		// Update the summaries under each preference
		if ( true ) {
			updateSummaryValues();
		}
	}

	@Override
	public void onActivityResult(final int aRequestCode, final int aResultCode, final Intent aData) {
		Log.i(LOG_TAG, "onActivityResult");
		
		// User returned from prompt to enable Bluetooth.
		if (aRequestCode == REQUEST_ENABLE_BT) {
			// User did not enable Bluetooth.
			if (aResultCode != Activity.RESULT_OK) {
				Toast.makeText(this,
						"User decided not to enable Bluetooth - exiting...",
						Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			return;
		}
		// User returned from selecting a device.
		if (aRequestCode == REQUEST_SELECT_DEVICE || aRequestCode == REQUEST_SELECT_DEVICE_AND_SAVE) {
			// Get the selected device
			if (aResultCode == Activity.RESULT_OK && aData != null) {
				final String deviceAddress = aData
						.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
				final BluetoothDevice device = mAdapter.getRemoteDevice(deviceAddress);

				// Save the name and address
				final String address = deviceAddress;
				final String name = device.getName();
				final Editor edit = mSharedPrefs.edit();
				edit.putString(mDevicePref.getKey(), getDisplayName(address, name));
				edit.putString("address", deviceAddress);
				edit.commit();
			}
			// Continue if started from the save button
			if (aRequestCode == REQUEST_SELECT_DEVICE_AND_SAVE) {
				final Intent intent = new Intent(this, ConnectActivity.class);
				startActivity(intent);				
			}
			return;
		}
	}
	
	// Get string describing a Bluetooth device
	public String getDisplayName(final String aAddress, final String aName) {
		if ( null == aAddress ) {
			return "No device selected";
		}
		
		if ( null == aName ) {
			return aAddress;
		}
		
		return aName + " (" + aAddress + ")";
	}
	
	// Show save action button to proceed
	@Override
	public boolean onCreateOptionsMenu(final Menu aMenu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.preference_activity, aMenu);
	    return true;
	}

	// Handle action button pressed.
	@Override
	public boolean onOptionsItemSelected(final MenuItem aItem) {
		
		// Save button pressed.
		if ( aItem.getItemId() == R.id.menu_save ) {
			
			// If no device selected, prompt to pick a device first.
			if ( null == mPrefs.getDeviceAddress() ) {
				Intent newIntent = new Intent(ConnectionPreferenceActivity.this,
						SelectDeviceActivity.class);
				startActivityForResult(newIntent, REQUEST_SELECT_DEVICE_AND_SAVE);				
				return true;
			}
			
			// Otherwise finish and start the connect activity.
			final Intent intent = new Intent(this, ConnectActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(aItem);
	}

}
