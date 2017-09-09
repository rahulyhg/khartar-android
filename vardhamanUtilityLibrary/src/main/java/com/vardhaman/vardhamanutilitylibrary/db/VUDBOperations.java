package com.vardhaman.vardhamanutilitylibrary.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import java.sql.Date;

public class VUDBOperations {
	VUSQLiteHelper mySQLiteHelper;
	Context context;
	SQLiteDatabase database;
	SharedPreferences sharedPrefs;
	String selectedSort, selectedCountry, selectedWithin, City;
	Date dateTime;
	String myUserId;

    protected final String ORDER_BY_ASC = " ASC";
    protected final String ORDER_BY_DEC = " DESC";

	public VUDBOperations(Context context, String dbName) {
		this.context = context;
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		mySQLiteHelper = VUSQLiteHelper.getInstance(context, dbName);
		mySQLiteHelper.openDataBase();
		database = mySQLiteHelper.getDatabaseInstance();
	}


    protected SQLiteDatabase getDatabase() {
        return database;
    }
}
