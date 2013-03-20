package org.pierrrrrrrot.loanmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * The database helper to improve the communication with the sqlite db.
 * 
 * @author Pierre Reliquet
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

    protected static final String DB_NAME = "loanmanager.db";
    protected static final int DB_VERSION = 1;

    public static final Object NO_MATCHING = null;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // for development purposes only
        // onUpgrade(getWritableDatabase(), 0, 1);
    }

    protected void insertObject(String table, ContentValues values) {
        getWritableDatabase().insert(table, null, values);
        getWritableDatabase().close();
    }

    protected String getStringFromColumn(Cursor c, String column) {
        return c.getString(c.getColumnIndex(column));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}