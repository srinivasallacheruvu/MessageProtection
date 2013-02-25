package com.yudiz.Message;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	//-----create objects -----
	EditText etPassword;
	Button btnLogin;
	SharedPreferences app_pref;
	LinearLayout ll;
	TextView txtPassword;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		etPassword = (EditText) findViewById(R.id.etLoginPassword);
		txtPassword = (TextView) findViewById(R.id.txtPassword);
		ll = (LinearLayout) findViewById(R.id.llName);

		// -----define object of SharedPreferences-----
		app_pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		// -----Create the animation for moving string-----
		Animation in = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_right);
		txtPassword.setAnimation(in);

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View vi) {

				if (etPassword.getText().toString().equals(
						app_pref.getString("Password", ""))) {
					Intent i = new Intent(Main.this, Option.class);
					// -----Create object of editor of sharedPreferences for
					// edit value in Preferences-----
					SharedPreferences.Editor edit = app_pref.edit();
					edit.putString("Password", etPassword.getText().toString());
					// -----commit for complete edit-----
					edit.commit();
					startActivity(i);
					etPassword.setText("");
				} else {
					// -----Create the animation for shake editBox when enter
					// wrong password-----
					Animation shake = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.shake);
					displayMsg("Wrong Password !!!!!!!");
					etPassword.setAnimation(shake);
				}

			}
		});

	}

	// -----method for toast message-----
	public void displayMsg(String msg) {
		Toast.makeText(Main.this, msg, Toast.LENGTH_SHORT).show();
	}
}