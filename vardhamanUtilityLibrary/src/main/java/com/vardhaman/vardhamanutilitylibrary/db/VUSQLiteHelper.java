package com.vardhaman.vardhamanutilitylibrary.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VUSQLiteHelper extends SQLiteOpenHelper {
	public static String DB_NAME = "temp.db";
	private final Context myContext;
	public SQLiteDatabase myDataBase;
	public static String DB_PATH = "/data/data/";
	private final static int DB_VERSION = 1;
	private boolean isDBUpgraded = false;
	private static VUSQLiteHelper instanceDBHelper;

	// Constructor Method
	public VUSQLiteHelper( Context context , String dbName)
		{
            super(context, dbName , null, DB_VERSION);
			this.myContext = context;
            DB_NAME = dbName;
			DB_PATH = "/data/data/"+myContext.getPackageName()+"/databases/";
			Log.d("VUSQLiteHelper", "DB_PATH"+DB_PATH);
			try {
				createDataBase();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	// Singleton Method.
	public static VUSQLiteHelper getInstance(Context context,String dbName)
		{

			if (instanceDBHelper == null)
				{
					instanceDBHelper = new VUSQLiteHelper(context,dbName);
				}
			return instanceDBHelper;
		}
	public SQLiteDatabase getDatabaseInstance(){
		return myDataBase;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("VUSQLiteHelper", "onCreate Called");
	}
	
	// database Creation
	public void createDataBase() throws IOException
		{
			Boolean dbExists = databaseExist();
			if (!dbExists)
				{
					try
						{
							copyDataBase();
						}
					catch (IOException e)
						{
							throw new Error("Error copying database: " + e.getMessage());
						}
				}
		}
	
	// check if database exists
	public boolean databaseExist()
		{
			File dbFile = new File(DB_PATH + DB_NAME);
			return dbFile.exists();
		}

	// Copy database from assets
	public void copyDataBase() throws IOException
		{
		Log.d("MySQLiteHelper", "copyDatabase Called");
			try
				{
					InputStream myInput = this.myContext.getAssets().open(DB_NAME);
					String outFileName = DB_PATH + DB_NAME;
                    File databaseDir = new File(DB_PATH);
                    if(!databaseDir.exists())
                        databaseDir.mkdirs();
					OutputStream myOutput = new FileOutputStream(outFileName);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = myInput.read(buffer)) > 0)
						{
							myOutput.write(buffer, 0, length);
						}
					
					myOutput.flush();
					myOutput.close();
					myInput.close();
				}
			catch (Exception e)
				{
					e.printStackTrace();
				}
		}

	// open database for querying
	public synchronized void openDataBase() throws SQLException
		{
			try
				{
					String myPath = DB_PATH + DB_NAME;
					if (myDataBase == null || !myDataBase.isOpen())
						{
							myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
							
							Log.d("MYSQLiteHelper", "DBVersion = "+myDataBase.getVersion());
							if(isDBUpgraded){
								myDataBase.setVersion(DB_VERSION);
							}
							if(myDataBase.getVersion()<DB_VERSION){
								onUpgrade(myDataBase, myDataBase.getVersion(), DB_VERSION);
								//Reopen
								myDataBase.close();
								myDataBase = null;
								openDataBase();
							}							
						}
				}
			catch (Exception e)
				{
					e.printStackTrace();
				}
		}
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		  
	    Log.w(VUSQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    //deleting old database
	    //TODO
	    File dbFile = new File(DB_PATH + DB_NAME);
	    if(dbFile.exists()){
	    	isDBUpgraded = dbFile.delete();
	    	Log.d("MySQLiteHelper", "deletion of old DB file, result = "+isDBUpgraded);	    	
	    }
	    //creating new one
	    try {
			createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	

}
