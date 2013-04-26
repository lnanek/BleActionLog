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
package com.htc.sample.bleexample.profiles;

import android.bluetooth.BluetoothDevice;

/**
 * Supports some example actions that can be performed.
 */
public interface ExampleActions {
	
	String getFirstActionName();
	
	String getSecondActionName();
	
	void performFirstAction(final BluetoothDevice aDevice);
	
	void performSecondAction(final BluetoothDevice aDevice);
	

}
