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
import com.htc.android.bluetooth.le.gatt.BleDescriptor;
import com.htc.android.bluetooth.le.gatt.BleGattID;
import com.htc.sample.bleexample.ConnectActivity;
import com.htc.sample.bleexample.constants.BleActions;
import com.htc.sample.bleexample.constants.BleCharacteristics;
import com.htc.sample.bleexample.constants.BleUtils;
import com.htc.sample.bleexample.constants.TiBleConstants;

/**
 * Connects to a heart rate collector to read and set values.
 *
 */
public class HrmProfileClient extends BleClientProfile implements ExampleActions {
	
	private static String TAG = HrmProfileClient.class.getSimpleName();
	
	private HrmServiceClient mService;
	
	private Context mContext;
	
	private byte mEncryption;

	public HrmProfileClient(final Context aContext, final byte aEncryption) {
		super(aContext, new BleGattID(UUID.randomUUID()));
		Log.d(TAG, "Contructor...");
		mContext = aContext;
		mEncryption = aEncryption;
		mService = new HrmServiceClient(aContext);
				
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
			ConnectActivity.broadcast(mContext, BleActions.ACTION_INITIALIZED, null);
		} else {
			ConnectActivity.broadcast(mContext, BleActions.ACTION_INITIALIZE_FAILED, null);
		}
	}

	@Override
	public void onDeviceConnected(final BluetoothDevice aDevice) {
		Log.d(TAG, "onDeviceConnected");		
		ConnectActivity.broadcast(mContext, BleActions.ACTION_CONNECTED, aDevice);
		
		Log.d(TAG, "setEncryption = " + mEncryption);
		setEncryption(aDevice, mEncryption);
		
		Log.d(TAG, "Registering for heart rate measurement notifications...");
		mService.registerForNotification(aDevice, 0, 
				new BleGattID(BleCharacteristics.HEART_RATE_MEASUREMENT));
		
		// Note that the super class will call refresh.
		super.onDeviceConnected(aDevice);
	}

	@Override
	public void onDeviceDisconnected(final BluetoothDevice aDevice) {
		Log.d(TAG, "onDeviceDisconnected");
		super.onDeviceDisconnected(aDevice);

		ConnectActivity.broadcast(mContext, BleActions.ACTION_DISCONNECTED, aDevice);

		// connectBackground(device);
	}

	@Override
	public void onRefreshed(final BluetoothDevice aDevice) {
		Log.d(TAG, "onRefreshed");
		super.onRefreshed(aDevice);
		ConnectActivity.broadcast(mContext, BleActions.ACTION_REFRESHED, aDevice);
		
		if ( !BleUtils.hasCharacterisitics(aDevice, mService,
				BleCharacteristics.HEART_RATE_MEASUREMENT,
				BleCharacteristics.HEART_RATE_BODY_SENSOR_LOCATION) ) {
			ConnectActivity.broadcastStatus(mContext, 
					"Expected characteristic missing - check device and reconnect");
			disconnect(aDevice);
			return;
		}
		
		Log.d(TAG, "Configuring heart rate measurement for notifications...");
		BleUtils.configureNotifications(aDevice, mService, 
				new BleGattID(BleCharacteristics.HEART_RATE_MEASUREMENT));		
	}

	@Override
	public void onProfileRegistered() {
		Log.d(TAG, "onProfileRegistered");
		super.onProfileRegistered();
		ConnectActivity.broadcast(mContext, BleActions.ACTION_REGISTERED, null);
	}

	@Override
	public void onProfileDeregistered() {
		Log.d(TAG, "onProfileDeregistered");
		super.onProfileDeregistered();
		notifyAll();
	}

	public String getFirstActionName() {
		return "Reset Energy Expended";
	}
	
	public String getSecondActionName() {
		return "Read Body Location";
	}
	
	public void performFirstAction(final BluetoothDevice aDevice) {
		writeHeartRateControlPointCharacteristic(aDevice, 0);
	}
	
	public void performSecondAction(final BluetoothDevice aDevice) {
		readBodySensorLocationCharacteristic(aDevice);
	}

	private int writeHeartRateControlPointCharacteristic(
			final BluetoothDevice aDevice, final int aValue) {
		final BleCharacteristic controlPoint = new BleCharacteristic(
				new BleGattID(BleCharacteristics.HEART_RATE_CONTROL_POINT));
		controlPoint.setValue(aValue);
		return mService.writeCharacteristic(aDevice, 0, controlPoint);
	}

	private int readBodySensorLocationCharacteristic(final BluetoothDevice aDevice) {
		final BleCharacteristic sensorLocation = new BleCharacteristic(
				new BleGattID(BleCharacteristics.HEART_RATE_BODY_SENSOR_LOCATION));		
	    return mService.readCharacteristic(aDevice, sensorLocation);
	}
		
}
