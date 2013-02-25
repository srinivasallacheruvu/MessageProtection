package com.yudiz.Message;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;

//-----class for check the incoming SMS-----
public class SmsReceiver extends BroadcastReceiver {
	
	Context mContext;
	String address, body, dt;
	DBAdapter dbnumber;
	DBAdapterSms dbsms;
	String incommingNumber;

	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		mContext = context;
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		dbnumber = new DBAdapter(mContext);
		dbsms = new DBAdapterSms(mContext);
		dbnumber.open();
		dbsms.open();
		Cursor cur = dbnumber.getAllRecords();

		if (intent.getAction().equalsIgnoreCase(
				"android.provider.Telephony.SMS_RECEIVED")) {
			if (bundle != null) {
				// ---retrieve the SMS message received---
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				msgs[0] = SmsMessage.createFromPdu((byte[]) pdus[0]);
				address = msgs[0].getOriginatingAddress();
				body = msgs[0].getMessageBody().toString();
				//-----change the forMate of date -----
				try {
					dt = formateDate(String.valueOf(msgs[0]
							.getTimestampMillis()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//-----if number are in DB then store message in local DB-----
				cur.moveToFirst();
				for (int i = 0; i < cur.getCount(); i++) {
					if ((cur.getString(1).toString().equals(address))) {
						dbsms.insertRecord(address, body, dt);
						abortBroadcast();
						break;
					}
					cur.moveToNext();
				}
			}
		}
		dbnumber.close();
		dbsms.close();

	}

	//-----method for change date from milliSeconds to dd/mm/yyyy forMate
	private String formateDate(String strMilliseconds) throws Exception {
		String strDate = "";
		Date date = new Date(Long.parseLong(strMilliseconds));
		strDate = DateFormat.format("dd/mm/yyyy hh:mm", date).toString();
		return strDate;
	}
}