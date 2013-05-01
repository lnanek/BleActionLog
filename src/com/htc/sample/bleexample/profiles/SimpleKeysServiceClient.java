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

import static com.htc.sample.bleexample.constants.BleCharacteristics.checkBit;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import com.htc.android.bluetooth.le.gatt.BleCharacteristic;
import com.htc.android.bluetooth.le.gatt.BleClientService;
import com.htc.android.bluetooth.le.gatt.BleGattID;
import com.htc.sample.bleexample.ConnectActivity;
import com.htc.sample.bleexample.constants.BleUtils;
import com.htc.sample.bleexample.constants.TiBleConstants;

/**
 * Reads and sets values on a TI SensorTag temperature service.
 */
public class SimpleKeysServiceClient extends BleClientService {
	
    public static String TAG = SimpleKeysServiceClient.class.getSimpleName();
	
	private Context mContext;
    
    public SimpleKeysServiceClient(final Context aContext) {
        super(new BleGattID(TiBleConstants.SIMPLE_KEYS_SERV_UUID));
        Log.d(TAG, "Constructor...");
        mContext = aContext;
    }
    
    private void playSound() {
    	Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	Ringtone r = RingtoneManager.getRingtone(mContext, notification);
    	r.play();
    }

    private void vibrate() {
    	Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    	v.vibrate(1000);
    }
    
    @Override
    public void onWriteCharacteristicComplete(int status, BluetoothDevice d,
            BleCharacteristic characteristic) {
        Log.d(TAG, "onWriteCharacteristicComplete");
    	super.onWriteCharacteristicComplete(status, d, characteristic);
    }

    @Override
    public void onRefreshComplete(BluetoothDevice d) {
        Log.d(TAG, "onRefreshComplete");
    	super.onRefreshComplete(d);
    }

    @Override
    public void onSetCharacteristicAuthRequirement(BluetoothDevice d,
            BleCharacteristic characteristic, int instanceID) {
        Log.d(TAG, "onSetCharacteristicAuthRequirement");
    	super.onSetCharacteristicAuthRequirement(d, characteristic, instanceID);
    }

    @Override
    public void onReadCharacteristicComplete(BluetoothDevice d, BleCharacteristic characteristic) {
        Log.d(TAG, "onReadCharacteristicComplete");
    	super.onReadCharacteristicComplete(d, characteristic);

    	final String label = BleUtils.getLabelForUuid(characteristic.getID().getUuid().toString());
		final byte firstByte = characteristic.getValue()[0];
		ConnectActivity.broadcastStatus(mContext, "onReadCharacteristicComplete - " + label + " - " + firstByte);
    }

    @Override
    public void onCharacteristicChanged(BluetoothDevice d, BleCharacteristic characteristic) {
        Log.d(TAG, "onCharacteristicChanged");
    	super.onCharacteristicChanged(d, characteristic);

		final byte firstByte = characteristic.getValue()[0];
    	
    	if ( BleUtils.eqId(characteristic, TiBleConstants.SIMPLE_KEYS_KEYPRESSED_UUID) ) {
    		
    		final boolean firstButtonDown = checkBit(firstByte, 0);
    		final boolean secondButtonDown = checkBit(firstByte, 1);
    		final boolean thirdButtonDown = checkBit(firstByte, 2);
    		
    		if ( firstButtonDown ) {
    			playSound();
    		}

    		if ( secondButtonDown ) {
    			vibrate();
    		}
    		
    		ConnectActivity.broadcastStatus(mContext, 
    				"onCharacteristicChanged - SIMPLE_KEYS_KEYPRESSED_UUID:\n" +
        			" firstButtonDown = " + firstButtonDown + ", \n" + 
        			" secondButtonDown = " + secondButtonDown + ", \n" + 
        			" thirdButtonDown (test mode only) = " + thirdButtonDown
        			);    		
    		return;
    	}
    	final String label = BleUtils.getLabelForUuid(characteristic);
    	ConnectActivity.broadcastStatus(mContext, "onCharacteristicChanged - " + label + " - " + firstByte);
    }

	@Override
	public ArrayList<BleCharacteristic> getAllCharacteristics(
			BluetoothDevice d) {
        Log.d(TAG, "getAllCharacteristics");
		return super.getAllCharacteristics(d);
	}

	@Override
	public int[] getAllServiceInstanceIds(BluetoothDevice d) {
        Log.d(TAG, "getAllServiceInstanceIds");
		return super.getAllServiceInstanceIds(d);
	}

	@Override
	public BleCharacteristic getCharacteristic(BluetoothDevice d,
			BleGattID arg1) {
        Log.d(TAG, "getCharacteristic");
		return super.getCharacteristic(d, arg1);
	}

	@Override
	public BleGattID getServiceId() {
        Log.d(TAG, "getServiceId");
		return super.getServiceId();
	}

	@Override
	public void onReadCharacteristicComplete(int status, BluetoothDevice d,
			BleCharacteristic characteristic) {
        Log.d(TAG, "onReadCharacteristicComplete");
		super.onReadCharacteristicComplete(status, d, characteristic);
	}

	@Override
	public int readCharacteristic(BluetoothDevice d, BleCharacteristic characteristic) {
        Log.d(TAG, "readCharacteristic");
		return super.readCharacteristic(d, characteristic);
	}

	@Override
	public void refresh(BluetoothDevice d) {
        Log.d(TAG, "refresh");
		super.refresh(d);
	}

	@Override
	public int registerForNotification(BluetoothDevice d, int instanceId,
			BleGattID characteristic) {
        Log.d(TAG, "registerForNotification");
		return super.registerForNotification(d, instanceId, characteristic);
	}

	@Override
	public int unregisterNotification(BluetoothDevice d, int instanceId,
			BleGattID characteristic) {
        Log.d(TAG, "unregisterNotification");
		return super.unregisterNotification(d, instanceId, characteristic);
	}

	@Override
	public int writeCharacteristic(BluetoothDevice d, int instanceId,
			BleCharacteristic characterisitic) {
        Log.d(TAG, "writeCharacteristic");
		return super.writeCharacteristic(d, instanceId, characterisitic);
	}
		    
}
