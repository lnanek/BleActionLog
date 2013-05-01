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

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.htc.android.bluetooth.le.gatt.BleCharacteristic;
import com.htc.android.bluetooth.le.gatt.BleClientService;
import com.htc.android.bluetooth.le.gatt.BleGattID;
import com.htc.sample.bleexample.constants.BleActions;
import com.htc.sample.bleexample.constants.BleCharacteristics;
import com.htc.sample.bleexample.constants.BleUtils;
import com.htc.sample.bleexample.constants.TiBleConstants;

/**
 * Reads and sets values on a TI SensorTag temperature service.
 */
public class AccelServiceClient extends BleClientService {
	
    public static String TAG = AccelServiceClient.class.getSimpleName();
	
	private Context mContext;
    
    public AccelServiceClient(final Context aContext) {
        super(new BleGattID(TiBleConstants.ACCELEROMETER_SERV_UUID));
        Log.d(TAG, "Constructor...");
        mContext = aContext;
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
		BleActions.broadcastStatus(mContext, "onReadCharacteristicComplete - " + label + " - " + firstByte);
    }

    @Override
    public void onCharacteristicChanged(BluetoothDevice d, BleCharacteristic characteristic) {
        Log.d(TAG, "onCharacteristicChanged");
    	super.onCharacteristicChanged(d, characteristic);
    	
    	if ( BleUtils.eqId(characteristic, TiBleConstants.ACCELEROMETER_DATA_UUID) ) {
    		    		
    		final float x = calcXValue(characteristic.getValue());
    		final float y = calcYValue(characteristic.getValue());
    		final float z = calcZValue(characteristic.getValue());

    		BleActions.broadcastStatus(mContext, 
    				"onCharacteristicChanged - IRTEMPERATURE_DATA_UUID:\n" +
        			" x = " + x + "\n" +
        			" y = " + y + "\n" +
        			" z = " + z,
        			x, y, z, KXTJ9_RANGE
        			);    		
    		return;
    	}

		final byte firstByte = characteristic.getValue()[0];
    	final String label = BleUtils.getLabelForUuid(characteristic);
    	BleActions.broadcastStatus(mContext, "onCharacteristicChanged - " + label + " - " + firstByte);
    }
    
    private static final float KXTJ9_RANGE = 4.0f;
    
    private static final float calcXValue(final byte[] aData) {
        return ((aData[0] * 1.0f) / (256 / KXTJ9_RANGE));
    }
    
    private static final float calcYValue(final byte[] aData) {
        //Orientation of sensor on board means we need to swap Y (multiplying with -1)
        return ((aData[1] * 1.0f) / (256 / KXTJ9_RANGE)) * -1;
    }
    
    private static final float calcZValue(final byte[] aData) {
        return ((aData[2] * 1.0f) / (256 / KXTJ9_RANGE));
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
