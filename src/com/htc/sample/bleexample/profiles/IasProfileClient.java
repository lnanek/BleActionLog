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
package com.htc.sample.bleexample.profiles;

import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.htc.android.bluetooth.le.gatt.BleCharacteristic;
import com.htc.android.bluetooth.le.gatt.BleClientProfile;
import com.htc.android.bluetooth.le.gatt.BleClientService;
import com.htc.android.bluetooth.le.gatt.BleConstants;
import com.htc.android.bluetooth.le.gatt.BleGattID;
import com.htc.sample.bleexample.constants.BleActions;
import com.htc.sample.bleexample.constants.BleCharacteristics;
import com.htc.sample.bleexample.constants.BleServices;

/**
 * Connects to a device to write to its alert level.
 *
 */
public class IasProfileClient extends BleClientProfile implements ExampleActions {

	private static String TAG = IasProfileClient.class.getSimpleName();

	private ServiceClient mService;

	private Context mContext;
	
	private byte mEncryption;

	public IasProfileClient(final Context aContext, final byte aEncryption) {
		super(aContext, new BleGattID(UUID.randomUUID()));
		Log.d(TAG, "Contructor...");
		mContext = aContext;
		mEncryption = aEncryption;		
		mService = new ServiceClient(aContext, new BleGattID(BleServices.IMMEDIATE_ALERT));

		final ArrayList<BleClientService> requiredServices = new ArrayList<BleClientService>();
		requiredServices.add(mService);
		init(requiredServices, null);
	}
	
	@Override
	public void onInitialized(final boolean aSuccess) {
		Log.d(TAG, "onInitialized, success: " + aSuccess);

		// Note that the super class will call registerProfile.
		super.onInitialized(aSuccess);

		if (aSuccess) {
			broadcast(BleActions.ACTION_INITIALIZED, null);
		} else {
			broadcast(BleActions.ACTION_INITIALIZE_FAILED, null);
		}
	}

	@Override
	public void onDeviceConnected(final BluetoothDevice aDevice) {
		Log.d(TAG, "onDeviceConnected");
		
		Log.d(TAG, "setEncryption = " + mEncryption);
		setEncryption(aDevice, mEncryption);

		broadcast(BleActions.ACTION_CONNECTED, aDevice);

		// Note that the super class will call refresh.
		super.onDeviceConnected(aDevice);
	}

	@Override
	public void onDeviceDisconnected(final BluetoothDevice aDevice) {
		Log.d(TAG, "onDeviceDisconnected");
		super.onDeviceDisconnected(aDevice);

		broadcast(BleActions.ACTION_DISCONNECTED, aDevice);

		// connectBackground(device);
	}

	@Override
	public void onRefreshed(final BluetoothDevice aDevice) {
		Log.d(TAG, "onRefreshed");
		super.onRefreshed(aDevice);
		broadcast(BleActions.ACTION_REFRESHED, aDevice);
	}

	@Override
	public void onProfileRegistered() {
		Log.d(TAG, "onProfileRegistered");
		super.onProfileRegistered();
		broadcast(BleActions.ACTION_REGISTERED, null);
	}

	@Override
	public void onProfileDeregistered() {
		Log.d(TAG, "onProfileDeregistered");
		super.onProfileDeregistered();
		notifyAll();
	}

	private void broadcast(final String aAction, final BluetoothDevice aDevice) {
		Intent intent = new Intent();
		intent.setAction(aAction);
		if (null != aDevice) {
			intent.putExtra(BluetoothDevice.EXTRA_DEVICE, aDevice.getAddress());
		}
		mContext.sendBroadcast(intent);
	}

	public void alert(final BluetoothDevice aDevice, final byte aLevel) {
		Log.d(TAG, "alert");

		final BleCharacteristic alertLevelCharacteristic = mService
				.getCharacteristic(aDevice, new BleGattID(BleCharacteristics.ALERT_LEVEL));
		if (null == alertLevelCharacteristic) {
			Log.d(TAG,
					"alert failed - could not find alert level characteristic in service");
			return;
		}

		byte[] value = { aLevel };
		alertLevelCharacteristic.setValue(value);
		alertLevelCharacteristic
				.setWriteType(BleConstants.GATTC_TYPE_WRITE_NO_RSP);
		mService.writeCharacteristic(aDevice, 0, alertLevelCharacteristic);

	}

	public String getFirstActionName() {
		return "Send High Alert";
	}
	
	public String getSecondActionName() {
		return "Send Low Alert";
	}
	
	public void performFirstAction(final BluetoothDevice aDevice) {
		alert(aDevice, BleCharacteristics.ALERT_LEVEL_HIGH);
	}
	
	public void performSecondAction(final BluetoothDevice aDevice) {
		alert(aDevice, BleCharacteristics.ALERT_LEVEL_LOW);
	}
	
}
