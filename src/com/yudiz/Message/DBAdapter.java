package com.yudiz.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBAdapter {
	public static final String KEY_ID = "id";
    public static final String KEY_NUMBER = "no";
    public static final String KEY_SMS = "sms";
    public static final String KEY_CALL = "call";    
   
            
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "DBForNumber";
    private static final String DATABASE_TABLE = "number";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table number (id integer primary key autoincrement , "
        + "no text not null, sms text null,call text null);";
    
    private final Context context; 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
    
    public DBAdapter(Context ctx) 
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
    public DBAdapter open() throws SQLException 
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
    public long insertRecord(String number, String sms, String call) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NUMBER,number);
        initialValues.put(KEY_SMS,sms);
        initialValues.put(KEY_CALL, call);
       
              
        return db.insert(DATABASE_TABLE, null, initialValues);
        
    }

  //---deletes a particular contact---
    public boolean deleteRecord(String id) 
    {
        //return db.delete(DATABASE_TABLE, KEY_FNAME LIKE+ id, null) > 0;
    	//db.execSQL("DELETE FROM CONTACTS WHERE FNAME="+id+";");
    	db.execSQL("DELETE FROM number WHERE id="+Integer.parseInt(id)+";");
    	return true;
    }
    
    //---retrieves all the contacts---
    public Cursor getAllRecords() 
    {
    	
    	
        Cursor mCursor= db.query(DATABASE_TABLE, new String[] {
        		KEY_ID, 
        		KEY_NUMBER,
        		KEY_SMS,
        		KEY_CALL              
                }, 
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

    
    //---retrieves a particular contact---
  public Cursor getRecord(int no) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ID, 
                		KEY_NUMBER,
                		KEY_SMS,
                		KEY_CALL}, 
                		KEY_NUMBER+ "='" +no+"'", 
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
  public int updateRecord(int id,String sms,String call) throws SQLException
  {
	  ContentValues initialValues = new ContentValues();
	  initialValues.put(KEY_SMS,sms);
      initialValues.put(KEY_CALL, call);
      return db.update(DATABASE_TABLE,initialValues, KEY_ID+"="+id, null);
  }
  
}


