package com.yudiz.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBAdapterSms {
	public static final String KEY_ID = "id";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_MESSAGE= "message";
    public static final String KEY_DATE = "smsdate";    
            
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "DBForSMS";
    private static final String DATABASE_TABLE = "smstable";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table smstable (id integer primary key autoincrement, "
        + "number text not null, message text not null, smsdate date);";
    
    private final Context context; 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
    
    public DBAdapterSms(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBAdapterSms open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a contact into the database---
    public long insertRecord(String number, String message, String dt) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NUMBER,number);
        initialValues.put(KEY_MESSAGE,message);
        initialValues.put(KEY_DATE,dt);
               
        return db.insert(DATABASE_TABLE, null, initialValues);
        
    }

  //---deletes a particular contact---
    public boolean deleteRecord(String id) 
    {
    	db.execSQL("DELETE FROM smstable WHERE id = "+Integer.parseInt(id)+";");
    	return true;
    }
    
    public void delete(String no)
    {
    	db.execSQL("DELETE FROM smstable WHERE number = "+no+";");
    }
    //---retrieves all the contacts---
    public Cursor getAllRecords() 
    {
        Cursor mCursor= db.query(DATABASE_TABLE, new String[] {
        		KEY_ID, 
        		KEY_NUMBER,
        		KEY_MESSAGE,
        		KEY_DATE}, 
                null, 
                null, 
                null, 
                null, 
                null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getCountSMS()
    {

    	String sql="Select number, Count(*) as sms From smstable Group by number Having Count(*)>0 ";
    	Cursor mCursor=db.rawQuery(sql,null);
    	if (mCursor != null) {
             mCursor.moveToFirst();
             return mCursor;
        }
    	else{
    		return null;
    	}
    }
    //---retrieves a particular contact---
  public Cursor getRecord(int rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ID, 
                		KEY_NUMBER,
                		KEY_MESSAGE,
                		KEY_DATE}, 
                		KEY_ID + "='" + rowId+"'", 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
 /* public Cursor getContactLastName(String rowId) throws SQLException 
  {
	//  query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
      Cursor mCursor =
              db.query(true, DATABASE_TABLE, new String[] {
              		KEY_ROWID,
              		KEY_FNAME, 
              		KEY_LNAME,
              		KEY_PHONE,
              		KEY_EMAIL,KEY_ADDRESS
              		}, 
              		KEY_LNAME + "='" + rowId+"'", 
              		null,
              		null, 
              		null, 
              		null, 
              		null);
      if (mCursor != null) {
          mCursor.moveToFirst();
      }
      return mCursor;
  }    */
  public int updateRecord(int id,String message, String number, String dt) throws SQLException
  {
	  ContentValues initialValues = new ContentValues();
	  initialValues.put(KEY_NUMBER,number);
      initialValues.put(KEY_MESSAGE,message);
      initialValues.put(KEY_DATE,dt);
      return db.update(DATABASE_TABLE,initialValues, KEY_ID+"="+id, null);
  }
  
}


