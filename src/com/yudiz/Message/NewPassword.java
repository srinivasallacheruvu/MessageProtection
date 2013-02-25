package com.yudiz.Message;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPassword extends Activity {
	// -----create objects -----
	EditText etOld, etNew, etConfirm;
	Button btnSet;
	SharedPreferences app_pref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newpassword);
		etOld = (EditText) findViewById(R.id.etOldPassword);
		etNew = (EditText) findViewById(R.id.etNewPassword);
		etConfirm = (EditText) findViewById(R.id.etConfirmPassword);
		btnSet = (Button) findViewById(R.id.btnSetNewPassword);

		// -----define object of SharedPreferences-----
		app_pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		btnSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (app_pref.getString("Password", "").equals(
						etOld.getText().toString())
						&& etNew.getText().toString().equals(
								etConfirm.getText().toString())) {
					// -----Create object of editor of sharedPreferences for
					// edit value in Preferences-----
					SharedPreferences.Editor edit = app_pref.edit();
					edit.putString("Password", etNew.getText().toString());
					// -----commit for complete edit-----
					edit.commit();
					displayMsg("Password Set....");
					Intent i = new Intent(NewPassword.this, Option.class);
					startActivity(i);
				} else if (etOld.getText().toString().equals("")
						|| etNew.getText().toString().equals("")
						|| etConfirm.getText().toString().equals("")) {
					// -----all filed empty then display message-----
					displayMsg("Enter all fileds....");
				} else {
					// -----if old and confirm password not match then display
					// message-----
					displayMsg("old or confirm Password invalid....");
				}
			}
		});
	}

	// -----method for toast message-----
	public void displayMsg(String msg) {
		Toast.makeText(NewPassword.this, msg, Toast.LENGTH_SHORT).show();
	}
}
