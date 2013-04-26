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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.htc.android.bluetooth.le.gatt.BleConstants;

/**
 * Persists preferences for the app.
 * 
 */
public class Prefs {

	private final SharedPreferences mPrefs;

	public Prefs(final Context context) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context
				.getApplicationContext());
	}
		
	public byte getEncryptionLevel() {
		return Byte.parseByte(mPrefs.getString("encryption", "0"));
	}
	
	public String getDeviceDisplayName() {
		return mPrefs.getString("device", "");
	}
	
	public String getDeviceAddress() {
		return mPrefs.getString("address", null);
	}
	
	public String getService() {
		return mPrefs.getString("service", "00001802-0000-1000-8000-00805f9b34fb");
	}
	
	public boolean isUseCustomScanParameters() {
		return mPrefs.getBoolean("useCustomScanParameters", false);
	}
	
	public int getConnectionAttempts() {
		return Integer.parseInt(mPrefs.getString("connectionAttempts", "1"));
	}
	
	public int getRetryRateS() {
		return Integer.parseInt(mPrefs.getString("retryRateS", "5"));
	}

}
