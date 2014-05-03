package com.example.app;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ReceiverSMS extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(
				"android.provider.Telephony.SMS_RECEIVED")) {
			StringBuilder sb = new StringBuilder();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] msgs = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					msgs[i] = SmsMessage
						.createFromPdu((byte[]) pdus[i]);
				}
				for (SmsMessage s : msgs) {
					sb.append("received");
					sb.append(s.getDisplayOriginatingAddress());
					sb.append("content:");
					sb.append(s.getDisplayMessageBody());
				}
				Toast.makeText(
						context, 
						"received: " + sb.toString(),
						Toast.LENGTH_LONG).show();
			}
		
		}
	}
}
