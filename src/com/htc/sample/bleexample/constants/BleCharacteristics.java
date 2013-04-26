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


/**
 * UUIDs and values for working with GATT characteristics.
 *
 */
public class BleCharacteristics {
	
	public static final String ALERT_CATEGORY_ID = "00002a43-0000-1000-8000-00805f9b34fb";
	public static final String ALERT_CATEGORY_ID_BIT_MASK = "00002a42-0000-1000-8000-00805f9b34fb";
	public static final String ALERT_LEVEL = "00002a06-0000-1000-8000-00805f9b34fb";
	public static final String ALERT_NOTIFICATION_CONTROL_POINT = "00002a44-0000-1000-8000-00805f9b34fb";
	public static final String ALERT_STATUS = "00002a3f-0000-1000-8000-00805f9b34fb";
	public static final String APPEARANCE = "00002a01-0000-1000-8000-00805f9b34fb";
	public static final String BLOOD_PRESSURE_FEATURE = "00002a49-0000-1000-8000-00805f9b34fb";
	public static final String BLOOD_PRESSURE_MEASUREMENT = "00002a35-0000-1000-8000-00805f9b34fb";
	public static final String BODY_SENSOR_LOCATION = "00002a38-0000-1000-8000-00805f9b34fb";
	public static final String CURRENT_TIME = "00002a2b-0000-1000-8000-00805f9b34fb";
	public static final String DATE_TIME = "00002a08-0000-1000-8000-00805f9b34fb";
	public static final String DAY_DATE_TIME = "00002a0a-0000-1000-8000-00805f9b34fb";
	public static final String DAY_OF_WEEK = "00002a09-0000-1000-8000-00805f9b34fb";
	public static final String DEVICE_NAME = "00002a00-0000-1000-8000-00805f9b34fb";
	public static final String DST_OFFSET = "00002a0d-0000-1000-8000-00805f9b34fb";
	public static final String EXACT_TIME_256 = "00002a0c-0000-1000-8000-00805f9b34fb";
	public static final String FIRMWARE_REVISION_STRING = "00002a26-0000-1000-8000-00805f9b34fb";
	public static final String HARDWARE_REVISION_STRING = "00002a27-0000-1000-8000-00805f9b34fb";
	public static final String HEART_RATE_BODY_SENSOR_LOCATION = "00002a38-0000-1000-8000-00805f9b34fb";
	public static final String HEART_RATE_CONTROL_POINT = "00002a39-0000-1000-8000-00805f9b34fb";
	public static final String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
	public static final String IEEE_11073_20601_REGULATORY = "00002a2a-0000-1000-8000-00805f9b34fb";
	public static final String INTERMEDIATE_CUFF_PRESSURE = "00002a36-0000-1000-8000-00805f9b34fb";
	public static final String INTERMEDIATE_TEMPERATURE = "00002a1e-0000-1000-8000-00805f9b34fb";
	public static final String LOCAL_TIME_INFORMATION = "00002a0f-0000-1000-8000-00805f9b34fb";
	public static final String MANUFACTURER_NAME_STRING = "00002a29-0000-1000-8000-00805f9b34fb";
	public static final String MEASUREMENT_INTERVAL = "00002a21-0000-1000-8000-00805f9b34fb";
	public static final String MODEL_NUMBER_STRING = "00002a24-0000-1000-8000-00805f9b34fb";
	public static final String NEW_ALERT = "00002a46-0000-1000-8000-00805f9b34fb";
	public static final String PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "00002a04-0000-1000-8000-00805f9b34fb";
	public static final String PERIPHERAL_PRIVACY_FLAG = "00002a02-0000-1000-8000-00805f9b34fb";
	public static final String RECONNECTION_ADDRESS = "00002a03-0000-1000-8000-00805f9b34fb";
	public static final String REFERENCE_TIME_INFORMATION = "00002a14-0000-1000-8000-00805f9b34fb";
	public static final String RINGER_CONTROL_POINT = "00002a40-0000-1000-8000-00805f9b34fb";
	public static final String RINGER_SETTING = "00002a41-0000-1000-8000-00805f9b34fb";
	public static final String SERIAL_NUMBER_STRING = "00002a25-0000-1000-8000-00805f9b34fb";
	public static final String SERVICE_CHANGED = "00002a05-0000-1000-8000-00805f9b34fb";
	public static final String SOFTWARE_REVISION_STRING = "00002a28-0000-1000-8000-00805f9b34fb";
	public static final String SUPPORTED_NEW_ALERT_CATEGORY = "00002a47-0000-1000-8000-00805f9b34fb";
	public static final String SUPPORTED_UNREAD_ALERT_CATEGORY = "00002a48-0000-1000-8000-00805f9b34fb";
	public static final String SYSTEM_ID = "00002a23-0000-1000-8000-00805f9b34fb";
	public static final String TEMPERATURE_MEASUREMENT = "00002a1c-0000-1000-8000-00805f9b34fb";
	public static final String TEMPERATURE_TYPE = "00002a1d-0000-1000-8000-00805f9b34fb";
	public static final String TIME_ACCURACY = "00002a12-0000-1000-8000-00805f9b34fb";
	public static final String TIME_SOURCE = "00002a13-0000-1000-8000-00805f9b34fb";
	public static final String TIME_UPDATE_CONTROL_POINT = "00002a16-0000-1000-8000-00805f9b34fb";
	public static final String TIME_UPDATE_STATE = "00002a17-0000-1000-8000-00805f9b34fb";
	public static final String TIME_WITH_DST = "00002a11-0000-1000-8000-00805f9b34fb";
	public static final String TIME_ZONE = "00002a0e-0000-1000-8000-00805f9b34fb";
	public static final String TX_POWER_LEVEL = "00002a07-0000-1000-8000-00805f9b34fb";
	public static final String UNREAD_ALERT_STATUS = "00002a45-0000-1000-8000-00805f9b34fb";
	
	public static final byte ALERT_LEVEL_NONE = 0;
	public static final byte ALERT_LEVEL_LOW = 1;
	public static final byte ALERT_LEVEL_HIGH = 2;

    public static final byte CLIENT_CHAR_CONFIG_FLAG_NOTIFICATIONS_ENABLED = 1;
    public static final byte CLIENT_CHAR_CONFIG_FLAG_INDICATIONS_ENABLED = 1 << 1;
    public static final byte CLIENT_CHAR_CONFIG_RESERVED_BYTE = 0;

    public static int unsignedByteToInt(final byte b) {
        return (int) b & 0xFF;
	}

    public static int unsignedBytesToInt(final byte hb, final byte lb) {
        return ((int)((hb << 8) & 0xff00)) | ((int) lb & 0xFF);
	}
    
    public static boolean checkBit(final int aValueToReadBitFrom, final int aBitIndex) {
        return (aValueToReadBitFrom & 1 << aBitIndex) != 0;
    }
    
}

