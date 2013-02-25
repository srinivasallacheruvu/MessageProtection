package com.yudiz.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Option extends Activity {
	Button  btnViewList, btnAddNew,btnCngPsw;
	Intent i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optoins);
	
		btnViewList = (Button) findViewById(R.id.btnViewList);
		btnAddNew = (Button) findViewById(R.id.btnAddNew);
		btnCngPsw = (Button) findViewById(R.id.btnChangePassword);
		
		//-----start activity for list of block numbers-----
		btnViewList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				i = new Intent(Option.this, ViewList.class);
				startActivity(i);
			}
		});
		
		//-----start activity for add new number-----
		btnAddNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				i = new Intent(Option.this, AddNewNumber.class);
				startActivity(i);
			}
		});
		
		//-----start activity for change password-----
		btnCngPsw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i = new Intent(Option.this,NewPassword.class);
				startActivity(i);
			}
		});
	}
}
