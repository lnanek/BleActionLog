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

import java.lang.reflect.Field;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.htc.android.bluetooth.le.gatt.BleCharacteristic;
import com.htc.android.bluetooth.le.gatt.BleClientService;
import com.htc.android.bluetooth.le.gatt.BleConstants;
import com.htc.android.bluetooth.le.gatt.BleDescriptor;
import com.htc.android.bluetooth.le.gatt.BleGattID;
import com.htc.sample.bleexample.ConnectActivity;

/**
 * Holds utility methods for working with BLE in general.
 *
 */
public class BleUtils {
	
	private static final String LOG_TAG = BleUtils.class.getSimpleName();
	
	/**
	 * @return if two UUIDs are equal
	 */
	public static boolean eqId(final String aLeftId, final String aRightId) {
		if ( null == aLeftId && null == aRightId ) {
			return true;
		}
		if ( null == aLeftId || null == aRightId ) {
			return false;
		}
		return aLeftId.equalsIgnoreCase(aRightId);
	}
	
	/**
	 * @return if two UUIDs are equal
	 */
	public static boolean eqId(final UUID aLeftId, final String aRightId) {
		if ( null == aLeftId && null == aRightId ) {
			return true;
		}
		if ( null == aLeftId || null == aRightId ) {
			return false;
		}
		return aLeftId.toString().equalsIgnoreCase(aRightId);
	}
	
	/**
	 * @return if two UUIDs are equal
	 */
	public static boolean eqId(final BleGattID aLeftId, final String aRightId) {
		if ( null == aLeftId && null == aRightId ) {
			return true;
		}
		if ( null == aLeftId || null == aRightId ) {
			return false;
		}
		return aLeftId.getUuid().toString().equalsIgnoreCase(aRightId);
	}
	
	/**
	 * @return if two UUIDs are equal
	 */
	public static boolean eqId(final BleCharacteristic aLeftId, final String aRightId) {
		if ( null == aLeftId && null == aRightId ) {
			return true;
		}
		if ( null == aLeftId || null == aRightId ) {
			return false;
		}
		return aLeftId.getID().getUuid().toString().equalsIgnoreCase(aRightId);
	}

	/**
	 * Finds a defined constant name, if any, for a given UUID String. Not comprehensive.
	 */
	public static String getLabelForUuid(final String aId) {
		return getLabel(aId, 
				BleServices.class, TiBleConstants.class, BleCharacteristics.class);
	}
	
	/**
	 * Finds a defined constant name, if any, for a given UUID. Not comprehensive.
	 */
	public static String getLabelForUuid(final UUID aId) {
		return getLabelForUuid(aId.toString());
	}
	
	/**
	 * Finds a defined constant name, if any, for a given BleGattID. Not comprehensive.
	 */
	public static String getLabelForUuid(final BleGattID aId) {
		return getLabelForUuid(aId.getUuid().toString());
	}
	
	/**
	 * Finds a defined constant name, if any, for a given BleCharacteristic. Not comprehensive.
	 */
	public static String getLabelForUuid(final BleCharacteristic aId) {
		return getLabelForUuid(aId.getID().getUuid().toString());
	}

	/**
	 * Searches classes for static fields with a matching value.
	 */
	private static String getLabel(final String aId, final Class<?>... classes) {
		Log.i(LOG_TAG, "getLabel, aId = " + aId);
		for ( final Class<?> c : classes ) {
			for ( final Field field : c.getDeclaredFields() ) {
				try {
					
					final Object value = field.get(null);
					if ( !(value instanceof String) ) {
						continue;
					}
					
					final String id = (String) value;
					if ( id.equalsIgnoreCase(aId)) {
						return field.getName();
					}

				} catch (IllegalArgumentException e) {
					// Next
				} catch (IllegalAccessException e) {
					// Next
				}			
			}
		}
		return null;
	}
	
	public static boolean hasCharacterisitics(
			final BluetoothDevice aDevice, 
			final BleClientService aService, 
			final String... ids) {
		if ( null == ids ) {
			return true;
		}
		
		for ( String id : ids ) {
			final BleCharacteristic characteristic = 
					aService.getCharacteristic(aDevice, new BleGattID(id));
			if ( null == characteristic ) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void configureNotifications(
			final BluetoothDevice aDevice, 
			final BleClientService aService, 
			final BleGattID aCharacteristic) {
		final BleCharacteristic characteristic = new BleCharacteristic(aCharacteristic);
		final BleDescriptor clientConfig = new BleDescriptor(
				new BleGattID(BleConstants.GATT_UUID_CHAR_CLIENT_CONFIG));
		characteristic.addDescriptor(clientConfig);
		final byte[] value = new byte[] {
				BleConstants.GATT_CLIENT_CONFIG_NOTIFICATION_BIT, 
				0 // GATT_CLIENT_CHAR_CONFIG_RESERVED_BYTE
		};
		clientConfig.setValue(value);
		clientConfig.setWriteType(BleConstants.GATTC_TYPE_WRITE);
		aService.writeCharacteristic(aDevice, 0, characteristic);
	}
}
