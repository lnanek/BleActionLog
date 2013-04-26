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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.htc.android.bluetooth.le.gatt.BleAdapter;

/**
 * Allows user to select a BLE device from a list of discovered devices.
 * 
 */
public class SelectDeviceActivity extends Activity {

	// Adapt device list to list view display.
	class DeviceAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public DeviceAdapter() {
			mInflater = LayoutInflater.from(SelectDeviceActivity.this);
		}

		public int getCount() {
			return mDeviceList.size();
		}

		public Object getItem(final int aPosition) {
			return mDeviceList.get(aPosition);
		}

		public long getItemId(final int aPosition) {
			return aPosition;
		}

		public View getView(final int aPosition, final View aViewToReuse,
				final ViewGroup aParent) {
			ViewGroup view = null;
			if (aViewToReuse != null) {
				view = (ViewGroup) aViewToReuse;
			} else {
				view = (ViewGroup) mInflater.inflate(R.layout.device_element,
						null);
			}
			final BluetoothDevice device = (BluetoothDevice) mDeviceList
					.get(aPosition);

			final TextView addressView = (TextView) view
					.findViewById(R.id.address);
			addressView.setText(device.getAddress());

			final TextView nameView = (TextView) view.findViewById(R.id.name);
			nameView.setText(device.getName());

			return view;
		}
	}

	private DeviceAdapter mDeviceAdapter;

	private List<BluetoothDevice> mDeviceList;

	private BluetoothAdapter mBtAdapter;

	private TextView mEmptyList;

	private TextView mTitleView;

	// Handle click on a device in the list
	private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(final AdapterView<?> aAdapterView,
				final View aView, final int aPosition, final long aId) {
			// Stop discovery and return address to calling activity.
			mBtAdapter.cancelDiscovery();
			Bundle localBundle = new Bundle();
			String address = ((BluetoothDevice) mDeviceList.get(aPosition))
					.getAddress();
			localBundle.putString(BluetoothDevice.EXTRA_DEVICE, address);
			Intent localIntent = new Intent();
			localIntent.putExtras(localBundle);
			setResult(Activity.RESULT_OK, localIntent);
			finish();
		}
	};

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context paramContext, Intent paramIntent) {
			String action = paramIntent.getAction();
			// Scanning for devices started.
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				setProgressBarIndeterminateVisibility(true);
				mTitleView.setText("Scanning...");
				if (mDeviceList.size() == 0) {
					mEmptyList.setText("No BLE devices found yet...");
				}
			}
			// Found a new device.
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice localBluetoothDevice = (BluetoothDevice) paramIntent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (BleAdapter.getDeviceType(localBluetoothDevice) == BleAdapter.DEVICE_TYPE_BLE
						|| BleAdapter.getDeviceType(localBluetoothDevice) == BleAdapter.DEVICE_TYPE_DUMO) {
					addDevice(localBluetoothDevice);
				}
			}
			// Scanning for devices finished.
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				mTitleView.setText("Scan finished");
			}
		}
	};

	private void addDevice(final BluetoothDevice aDevice) {
		// If the device list already contains the device, we're done.
		for (final BluetoothDevice listItem : mDeviceList) {
			if (listItem.getAddress().equals(aDevice.getAddress())) {
				return;
			}
		}
		// Otherwise add the device.
		mEmptyList.setVisibility(View.GONE);
		mDeviceList.add(aDevice);
		mDeviceAdapter.notifyDataSetChanged();
	}

	protected void onCreate(final Bundle aBundle) {
		super.onCreate(aBundle);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (null == mBtAdapter) {
			finish();
			return;
		}

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		mTitleView = (TextView) findViewById(R.id.title_devices);
		mEmptyList = (TextView) findViewById(R.id.empty);

		// Cancel button finishes activity without returning a device.
		final Button cancelButton = (Button) findViewById(R.id.btn_cancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				finish();
			}
		});

		// Scan button restarts the device scan.
		final Button scanButton = (Button) findViewById(R.id.btn_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				mBtAdapter.cancelDiscovery();
				mBtAdapter.startDiscovery();
			}
		});

		// Listen for Bluetooth Discovery broadcasts
		IntentFilter intentFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, intentFilter);

		// Populate list
		mDeviceList = new ArrayList<BluetoothDevice>();
		mDeviceAdapter = new DeviceAdapter();

		final ListView listView = (ListView) findViewById(R.id.new_devices);
		listView.setAdapter(mDeviceAdapter);
		listView.setOnItemClickListener(mDeviceClickListener);

		// Add bonded devices to list on creation.
		for (final BluetoothDevice bondedDevice : mBtAdapter.getBondedDevices()) {
			addDevice(bondedDevice);
		}
		// Discover other devices.
		mBtAdapter.cancelDiscovery();
		mBtAdapter.startDiscovery();
	}

	public void onStop() {
		super.onStop();
		unregisterReceiver(mReceiver);
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
		finish();
	}
}