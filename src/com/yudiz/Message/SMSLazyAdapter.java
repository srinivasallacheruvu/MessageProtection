package com.yudiz.Message;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SMSLazyAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	List<MessageContent> sms;
	View vi;

	// ------Constructor of this class
	public SMSLazyAdapter(Activity a, List<MessageContent> sms) {
		activity = a;
		this.sms = sms;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return sms.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView text;
		public TextView text1;
	}

	// ------inflate layout of user friend list 
	public View getView(final int position, View convertView, ViewGroup parent) {
		vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.viewsmsitem, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.txtSmsBody);
			holder.text1 = (TextView) vi.findViewById(R.id.txtSmsDate);
			vi.setTag(holder);
		} else{
			holder = (ViewHolder) vi.getTag();
		}
		MessageContent messageContent = sms.get(position);
		holder.text.setText(messageContent.getBody());
		holder.text1.setText(messageContent.getDate());
		return vi;
	}
}