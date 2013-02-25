package com.yudiz.Message;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.TelephonyManager;

//-----class for check the incoming call-----

public class RejectPhone extends BroadcastReceiver {

	Context mContext;
	DBAdapter dbnumber;
	String incommingNumber;

	//----- method for receive call-----
	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		mContext = context;
		Bundle bundle = intent.getExtras();
		dbnumber = new DBAdapter(mContext);
		dbnumber.open();
		Cursor cur = dbnumber.getAllRecords();
		//-----get the incoming number-----
		incommingNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

		//-----create object for telephonyManager for TELEPHONY_SERVICE-----
		TelephonyManager telephone = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Class<?> c = Class.forName(telephone.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);

			com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m
					.invoke(telephone);
			//-----check the incoming number are in block list then match then reject the call-----
			cur.moveToFirst();
			for (int i = 0; i < cur.getCount(); i++) {
				if ((cur.getString(1).toString().equals(incommingNumber.substring(incommingNumber.length()-10, incommingNumber.length())))) {
					//-----call method for end call-----
					telephonyService.endCall();
					break;
				}
				cur.moveToNext();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dbnumber.close();
		abortBroadcast();
	}

}
