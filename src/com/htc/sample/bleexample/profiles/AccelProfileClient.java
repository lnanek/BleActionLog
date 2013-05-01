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
import android.util.Log;

import com.htc.android.bluetooth.le.gatt.BleCharacteristic;
import com.htc.android.bluetooth.le.gatt.BleClientProfile;
import com.htc.android.bluetooth.le.gatt.BleClientService;
import com.htc.android.bluetooth.le.gatt.BleConstants;
import com.htc.android.bluetooth.le.gatt.BleGattID;
import com.htc.sample.bleexample.constants.BleActions;
import com.htc.sample.bleexample.constants.BleUtils;
import com.htc.sample.bleexample.constants.TiBleConstants;

/**
 * Connects to a TI Sensor Tag to read the temperature.
 * 
 */
public class AccelProfileClient extends BleClientProfile implements
		ExampleActions {

	private static String TAG = AccelProfileClient.class.getSimpleName();

	private AccelServiceClient mService;

	private Context mContext;
	
	private byte mEncryption;

	public AccelProfileClient(final Context aContext, final byte aEncryption) {
		super(aContext, new BleGattID(UUID.randomUUID()));
		Log.d(TAG, "Contructor...");
		mContext = aContext;
		mEncryption = aEncryption;
		mService = new AccelServiceClient(aContext);

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
			BleActions.broadcast(mContext, BleActions.ACTION_INITIALIZED, null);
		} else {
			BleActions.broadcast(mContext, BleActions.ACTION_INITIALIZE_FAILED, null);
		}
	}

	@Override
	public void onDeviceConnected(final BluetoothDevice aDevice) {
		Log.d(TAG, "onDeviceConnected");
		BleActions.broadcast(mContext, BleActions.ACTION_CONNECTED, aDevice);

		Log.d(TAG, "setEncryption = " + mEncryption);
		setEncryption(aDevice, mEncryption);
		
		Log.d(TAG, "Registering for accelerometer notifications...");
		mService.registerForNotification(aDevice, 0, new BleGattID(
				TiBleConstants.ACCELEROMETER_DATA_UUID));
		
		// Note that the super class will call refresh.
		super.onDeviceConnected(aDevice);
	}

	@Override
	public void onDeviceDisconnected(final BluetoothDevice aDevice) {
		Log.d(TAG, "onDeviceDisconnected");
		super.onDeviceDisconnected(aDevice);

		BleActions.broadcast(mContext, BleActions.ACTION_DISCONNECTED, aDevice);

		// connectBackground(device);
	}

	@Override
	public void onRefreshed(final BluetoothDevice aDevice) {
		Log.d(TAG, "onRefreshed");
		super.onRefreshed(aDevice);
		BleActions.broadcast(mContext, BleActions.ACTION_REFRESHED, aDevice);
		
		if ( !BleUtils.hasCharacterisitics(aDevice, mService,
				TiBleConstants.ACCELEROMETER_DATA_UUID, 
				TiBleConstants.ACCELEROMETER_CONF_UUID) ) {
			BleActions.broadcastStatus(mContext, 
					"Expected characteristic missing - check device and reconnect");
			disconnect(aDevice);
			return;
		}
	}	

	@Override
	public void onProfileRegistered() {
		Log.d(TAG, "onProfileRegistered");
		super.onProfileRegistered();
		BleActions.broadcast(mContext, BleActions.ACTION_REGISTERED, null);
	}

	@Override
	public void onProfileDeregistered() {
		Log.d(TAG, "onProfileDeregistered");
		super.onProfileDeregistered();
		notifyAll();
	}

	public void turnOnAccelerometerReadings(final BluetoothDevice aDevice) {
		Log.d(TAG, "turnOnAccelerometerReadings");
		final BleCharacteristic tempConfig = new BleCharacteristic(
				new BleGattID(TiBleConstants.ACCELEROMETER_CONF_UUID));
		byte[] value = { 1 };
		tempConfig.setValue(value);
		tempConfig.setWriteType(BleConstants.GATTC_TYPE_WRITE);
		mService.writeCharacteristic(aDevice, 0, tempConfig);
	}

	public String getFirstActionName() {
		return "Turn On Accelerometer Sensor";
	}

	public String getSecondActionName() {
		return "Configure Notifications";
	}

	public void performFirstAction(final BluetoothDevice aDevice) {
		turnOnAccelerometerReadings(aDevice);
	}

	public void performSecondAction(final BluetoothDevice aDevice) {
		BleUtils.configureNotifications(aDevice, mService, new BleGattID(
				TiBleConstants.ACCELEROMETER_DATA_UUID));
	}

}
