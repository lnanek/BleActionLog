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
	
}

