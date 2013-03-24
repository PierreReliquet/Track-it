/**
 * Copyright 2013 Pierre Reliquet©
 * 
 * Loans Manager is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Loans Manager is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Loans Manager. If not, see <http://www.gnu.org/licenses/>
 */
package fr.free.pierre.reliquet.loansmanager.dao;

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
    
    protected static final String DB_NAME     = "loanmanager.db";
    protected static final int    DB_VERSION  = 1;
    
    public static final Object    NO_MATCHING = null;
    
    /**
     * This method is made to ensure that the initialization of the DB is going
     * to be properly made
     * 
     * @param context
     */
    public static void globalInit(Context context) {
        ProductsDAO.init(context);
        BorrowersDAO.init(context);
        LoansDAO.init(context);
    }
    
    protected DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.onCreate(this.getWritableDatabase());
        this.getWritableDatabase().close();
        // for development purposes only
        // onUpgrade(getWritableDatabase(), 0, 1);
    }
    
    protected String getStringFromColumn(Cursor c, String column) {
        return c.getString(c.getColumnIndex(column));
    }
    
    protected void insertObject(String table, ContentValues values) {
        this.getWritableDatabase().insert(table, null, values);
        this.getWritableDatabase().close();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    
}