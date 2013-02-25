package com.yudiz.Message;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class ViewSMS extends Activity implements OnItemLongClickListener,
		TextToSpeech.OnInitListener {
	DBAdapterSms dbsms;
	TextView txtHeader;
	Bundle b;
	SMSLazyAdapter adapter;
	AlertDialog.Builder builder, builderDetails;
	ListView smsList;
	List<MessageContent> listMessages;
	TextToSpeech mTts;
	private static final int MY_DATA_CHECK_CODE = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewsms);
		b = this.getIntent().getExtras();
		dbsms = new DBAdapterSms(this);
		builder = new AlertDialog.Builder(this);
		builderDetails = new AlertDialog.Builder(this);
		listMessages = new ArrayList<MessageContent>();
		txtHeader = (TextView) findViewById(R.id.txtViewSMSHeader);
		smsList = (ListView) findViewById(R.id.lvSMS);
		txtHeader.setText(b.getString("Number"));
		dbsms.open();
		
		// -----get the all record of SMS for 1 number from local DB-----
		Cursor cur = dbsms.getAllRecords();
		startManagingCursor(cur);
		cur.moveToLast();
		for (int i = 0; i < cur.getCount(); i++) {
			if (b.getString("Number").equals(cur.getString(1))) {
				CharSequence body = cur.getString(2);
				if (body != null) {
					body = SmileyParser.getInstance(ViewSMS.this)
							.addSmileySpans(body);
				}
				MessageContent messageContent = new MessageContent();
				messageContent.setBody(body);
				messageContent.setDate(cur.getString(3));
				messageContent.setid(cur.getString(0));
				listMessages.add(messageContent);
			}
			cur.moveToPrevious();
		}
		//-----if no SMS then display message-----
		if (listMessages.size() == 0) {
			displayMessage("No SMS For This Number.");
		}
		
		//-----view the all SMS for 1 number-----
		adapter = new SMSLazyAdapter(ViewSMS.this, listMessages);
		smsList.setAdapter(adapter);
		smsList.setOnItemLongClickListener(this);
		
		//-----create object for speak the SMS-----
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}

	// -----method for show the option for SMS by long click at number
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View vi,
			final int position, long arg3) {
		final TextView tv = (TextView) vi.findViewById(R.id.txtSmsBody);
		final CharSequence[] items = { "View Message Details",
				"Delete Message", "Delete All Message", "Speak SMS" };
		builder.setTitle("Message Options");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					messageDetails(tv.getText().toString());
					break;
				case 1:
					deleteSMS(adapter.sms.get(position).getid());
					break;
				case 2:
					deleteAll(b.getString("Number"));
					break;
				case 3:
					mTts.speak(tv.getText().toString(), TextToSpeech.QUEUE_ADD,
							null);
					break;
				default:
					break;
				}
			}
		});
		builder.show();
		return false;
	}

	//-----method for delete all SMS for 1 number local DB
	public void deleteAll(final String no) {
		builderDetails.setTitle("Message Delete");
		builderDetails.setMessage("Do u want to Delete all message.......")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg) {
						dbsms.delete(no);
						ViewSMS.this.finish();
						Bundle b1 = new Bundle();
						Intent i = new Intent(getApplicationContext(),
								ViewSMS.class);
						b1.putString("Number", b.getString("Number"));
						i.putExtras(b1);
						startActivity(i);
						dialog.dismiss();
					}
				}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).show();
	}
	
	
	//-----method for delete a SMS for 1 number local DB
	public void deleteSMS(final String id) {
		builderDetails.setTitle("Message Delete");
		builderDetails.setMessage("Do u want to Delete this message.......")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg) {
						dbsms.deleteRecord(id);
						ViewSMS.this.finish();
						Bundle b1 = new Bundle();
						Intent i = new Intent(getApplicationContext(),
								ViewSMS.class);
						b1.putString("Number", b.getString("Number"));
						i.putExtras(b1);
						startActivity(i);
						dialog.dismiss();
					}
				}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).show();

	}

	public void messageDetails(String body) {
		builderDetails.setTitle("Message Details");
		builderDetails.setMessage(body).setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbsms.close();
	}

	//-----method for speak the SMS -----
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				System.out.println("if");
				mTts = new TextToSpeech(this, this);
			} else {
				System.out.println("else");
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}

	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.ERROR) {
			Toast.makeText(ViewSMS.this,
					"Error occurred while initializing Text-To-Speech engine",
					Toast.LENGTH_LONG).show();
		}
	}

	public void displayMessage(String msg) {
		Toast.makeText(ViewSMS.this, msg, Toast.LENGTH_SHORT).show();
	}

}
