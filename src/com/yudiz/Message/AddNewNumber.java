package com.yudiz.Message;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewNumber extends Activity {
	Button btnAdd;
	EditText etNumber;
	DBAdapter dbNumber;
	boolean flag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnewnumber);
		dbNumber = new DBAdapter(this);
		btnAdd = (Button) findViewById(R.id.btnBlockNumber);
		etNumber = (EditText) findViewById(R.id.etBlockNumber);
		dbNumber.open();
		flag = false;
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Cursor cur = dbNumber.getAllRecords();
				cur.moveToFirst();
				for (int i = 0; i < cur.getCount(); i++) {
					if (etNumber.getText().toString().equals(cur.getString(1))) {
						flag = true;
						break;
					}
					cur.moveToNext();
				}
				if (flag) {
					displayMessage("This number already blocked.");
				} else if (etNumber.getText().toString().equals("")) {
					displayMessage("Pls enter number.");
				} else {
					dbNumber
							.insertRecord("+91"+etNumber.getText().toString(), "", "");
					displayMessage("Number blocked.");
					Intent i = new Intent(AddNewNumber.this, Option.class);
					startActivity(i);
				}
				etNumber.setText("");
			}
		});
		dbNumber.open();
	}

	public void displayMessage(String msg) {
		Toast.makeText(AddNewNumber.this, msg, Toast.LENGTH_SHORT).show();
	}
}
