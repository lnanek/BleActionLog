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
package com.htc.sample.bleexample.constants;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.htc.sample.bleexample.ConnectActivity;

/**
 * Broadcast actions sent and received by the sample code to indicate GATT actions.
 *
 */
public class BleActions {
	
	public static final String ACTION_INITIALIZED = BleActions.class.getName() + ".ACTION_INITIALIZED";
	
	public static final String ACTION_INITIALIZE_FAILED = BleActions.class.getName() + ".ACTION_INITIALIZE_FAILED";
	
	public static final String ACTION_REGISTERED = BleActions.class.getName() + ".ACTION_REGISTERED";
	
	public static final String ACTION_CONNECTED = BleActions.class.getName() + ".ACTION_CONNECTED";
	
	public static final String ACTION_REFRESHED = BleActions.class.getName() + ".ACTION_REFRESHED";
	
	public static final String ACTION_NOTIFICATION = BleActions.class.getName() + ".ACTION_NOTIFICATION";
	
	public static final String ACTION_DISCONNECTED = BleActions.class.getName() + ".ACTION_DISCONNECTED";
	
	public static final String ACTION_STATUS_MESSAGE = 
			ConnectActivity.class.getName() + ".ACTION_STATUS_MESSAGE";
	
	public static final String STATUS_MESSAGE_EXTRA = 
			ConnectActivity.class.getName() + ".STATUS_MESSAGE_EXTRA";
	
	public static final String STATUS_X_EXTRA = 
			ConnectActivity.class.getName() + ".STATUS_X_EXTRA";
	
	public static final String STATUS_Y_EXTRA = 
			ConnectActivity.class.getName() + ".STATUS_Y_EXTRA";
	
	public static final String STATUS_Z_EXTRA = 
			ConnectActivity.class.getName() + ".STATUS_Z_EXTRA";
	
	public static final String STATUS_RANGE_EXTRA = 
			ConnectActivity.class.getName() + ".STATUS_RANGE_EXTRA";

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
		intent.setAction(ACTION_STATUS_MESSAGE);
		if ( null != aStatus ) {
			intent.putExtra(STATUS_MESSAGE_EXTRA, aStatus);
		}
		aContext.sendBroadcast(intent);		
	}

	public static void broadcastStatus(final Context aContext, final String aStatus,
			final Float aX, final Float aY, final Float aZ, final Float aRange) {
		Intent intent = new Intent();
		intent.setAction(ACTION_STATUS_MESSAGE);
		if ( null != aStatus ) {
			intent.putExtra(STATUS_MESSAGE_EXTRA, aStatus);
		}
		if ( null != aX ) {
			intent.putExtra(STATUS_X_EXTRA, aX);
		}
		if ( null != aY ) {
			intent.putExtra(STATUS_Y_EXTRA, aY);
		}
		if ( null != aZ ) {
			intent.putExtra(STATUS_Z_EXTRA, aZ);
		}
		if ( null != aRange ) {
			intent.putExtra(STATUS_RANGE_EXTRA, aRange);
		}
		aContext.sendBroadcast(intent);		
	}
}

