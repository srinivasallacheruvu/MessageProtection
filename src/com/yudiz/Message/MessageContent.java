package com.yudiz.Message;

//-----Class for store information of received message  & return information-----
public class MessageContent {

	private CharSequence body;
	private String date;
	private String id;
	private String number;
	private String count;

	//-----method for get & set body of message-----
	public CharSequence getBody() {
		return body;
	}
	public void setBody(CharSequence body) {
		this.body = body;
	}

	
	//-----method for get & set received date of message-----
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	
	//-----method for get & set id of message-----
	public String getid() {
		return id;
	}
	public void setid(String id) {
		this.id = id;
	}

	
	//-----method for get & set no of messages for particular no.-----
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}

	
	//-----method for get & set Phone no. of message-----
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
