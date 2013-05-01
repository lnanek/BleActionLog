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
package com.htc.sample.bleexample;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Controls UI for connection activity.
 */
public class ConnectUi {

	private static final String LOG_TAG = "ConnectUi";

	private Button mCancelBackgroundConnection;

	private Button mConnectForeground;

	private Button mConnectBackground;

	private Button mFirstAction;

	private Button mSecondAction;

	private Button mSelectDevice;

	private TextView mStatusView;
	
	private PointerView mPointerView;

	TextView mDeviceNameView;

	private ConnectActivity mActivity;

	public ConnectUi(final ConnectActivity aActivity) {
		Log.i(LOG_TAG, "ConnectUi");

		mActivity = aActivity;
		mActivity.setContentView(R.layout.main);

		mStatusView = (TextView) mActivity.findViewById(R.id.statusValue);
		mStatusView.setMovementMethod(new ScrollingMovementMethod());
		mStatusView.setText("Configuring Bluetooth...\n");
		mDeviceNameView = (TextView) mActivity.findViewById(R.id.deviceName);
		mPointerView = (PointerView) mActivity.findViewById(R.id.pointerView);

		mCancelBackgroundConnection = (Button) mActivity
				.findViewById(R.id.cancel);
		mCancelBackgroundConnection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aActivity.cancelBackgroundConnection();
			}
		});

		mConnectForeground = (Button) mActivity.findViewById(R.id.foreground);
		mConnectForeground.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aActivity.startForegroundConnectionAttempts();
			}
		});

		mConnectBackground = (Button) mActivity.findViewById(R.id.background);
		mConnectBackground.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aActivity.connectBackground();
			}
		});

		mSelectDevice = (Button) mActivity.findViewById(R.id.btn_select);
		mSelectDevice.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				aActivity.onSetupDeviceClicked();
			}
		});

		mFirstAction = (Button) mActivity.findViewById(R.id.firstAction);
		mFirstAction.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				aActivity.onFirstActionPressed();
			}
		});

		mSecondAction = (Button) mActivity.findViewById(R.id.secondAction);
		mSecondAction.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				aActivity.onSecondActionPressed();
			}
		});

		setConnectionButtonsEnabled(false);
		setFirstProfileButton(false, false, "Action");
		setSecondProfileButton(false, false, "Action");

	}
	
	void setPointer(final Float aX, final Float aY, final Float aZ, final Float aRange) {
		mPointerView.set(aX, aY, aZ, aRange);
	}

	void setConnectionButtonsEnabled(final boolean aEnabled) {
		mConnectForeground.setEnabled(aEnabled);
		mConnectBackground.setEnabled(aEnabled);
	}

	void setFirstProfileButton(final boolean aVisible, final boolean aEnabled,
			final String aLabel) {
		mFirstAction.setVisibility(aVisible ? View.VISIBLE : View.GONE);
		mFirstAction.setEnabled(aEnabled);
		mFirstAction.setText(aLabel);
	}

	void setSecondProfileButton(final boolean aVisible, final boolean aEnabled,
			final String aLabel) {
		mSecondAction.setVisibility(aVisible ? View.VISIBLE : View.GONE);
		mSecondAction.setEnabled(aEnabled);
		mSecondAction.setText(aLabel);
	}

	public void setProfileButtonsEnabled(boolean b) {
		mFirstAction.setEnabled(b);
		mSecondAction.setEnabled(b);
	}

	public void appendStatus(final String aStatus) {
		mStatusView.append(aStatus);
	}
	
}