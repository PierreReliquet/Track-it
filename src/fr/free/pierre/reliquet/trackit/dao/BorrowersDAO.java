/**
 * Copyright 2013 Pierre Reliquet©
 * 
 * Track-it is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Track-it is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Track-it. If not, see <http://www.gnu.org/licenses/>
 */
package fr.free.pierre.reliquet.trackit.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.free.pierre.reliquet.trackit.model.Borrower;

/**
 * @author Pierre Reliquet
 * 
 */
public class BorrowersDAO extends DBHelper {
    
    private static final String          BORROWERS_COLUMN_AVERAGE_LOAN_TIME = "AVERAGE_LOAN_TIME";
    
    private static final String          BORROWERS_COLUMN_ID                = "ID";
    private static final String          BORROWERS_COLUMN_NAME              = "NAME";
    private static final String          BORROWERS_COLUMN_NUMBER_OF_LOANS   = "NUMBER_LOAN";
    private static final String          BORROWERS_TABLE                    = "borrowers";
    
    private static final String          BORROWERS_TABLE_CREATE             = "CREATE TABLE IF NOT EXISTS  "
                                                                                    + BORROWERS_TABLE
                                                                                    + " ("
                                                                                    + BORROWERS_COLUMN_ID
                                                                                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                                                    + BORROWERS_COLUMN_NAME
                                                                                    + " TEXT NOT NULL,"
                                                                                    + BORROWERS_COLUMN_NUMBER_OF_LOANS
                                                                                    + " INTEGER,"
                                                                                    + BORROWERS_COLUMN_AVERAGE_LOAN_TIME
                                                                                    + " INTEGER"
                                                                                    + ");";
    
    private static final String          BORROWERS_TABLE_DROP               = " DROP TABLE "
                                                                                    + BORROWERS_TABLE
                                                                                    + ";";
    
    private static volatile BorrowersDAO instance                           = null;
    
    public static BorrowersDAO getInstance() {
        if (BorrowersDAO.instance == null) {
            throw new RuntimeException("The init method should be called first");
        }
        return BorrowersDAO.instance;
    }
    
    public static void init(Context context) {
        if (BorrowersDAO.instance == null) {
            synchronized (BorrowersDAO.class) {
                if (BorrowersDAO.instance == null) {
                    BorrowersDAO.instance = new BorrowersDAO(context);
                }
            }
        }
    }
    
    protected BorrowersDAO(Context context) {
        super(context);
    }
    
    public void addLoan(Borrower aBorrower) {
        ContentValues values = new ContentValues();
        values.put(BORROWERS_COLUMN_NUMBER_OF_LOANS,
                aBorrower.getNumberOfLoans() + 1);
        this.getWritableDatabase().update(BORROWERS_TABLE, values,
                BORROWERS_COLUMN_ID + " = ?",
                new String[] { aBorrower.getId() + "" });
        this.getWritableDatabase().close();
    }
    
    private Borrower createBorrowerFromCursor(Cursor c) {
        Borrower b = new Borrower();
        b.setId(Integer.parseInt(this.getStringFromColumn(c,
                BORROWERS_COLUMN_ID)));
        b.setName(this.getStringFromColumn(c, BORROWERS_COLUMN_NAME));
        b.setNumberOfLoans(Integer.parseInt(this.getStringFromColumn(c,
                BORROWERS_COLUMN_NUMBER_OF_LOANS)));
        b.setAverageTime(Integer.parseInt(this.getStringFromColumn(c,
                BORROWERS_COLUMN_AVERAGE_LOAN_TIME)));
        return b;
    }
    
    public List<Borrower> getAllBorrowers() {
        List<Borrower> borrowers = new ArrayList<Borrower>();
        Cursor borrower = this.getReadableDatabase().query(BORROWERS_TABLE,
                null, null, null, null, null, BORROWERS_COLUMN_ID);
        while (borrower.moveToNext()) {
            borrowers.add(this.createBorrowerFromCursor(borrower));
        }
        borrower.close();
        this.getReadableDatabase().close();
        return borrowers;
    }
    
    public Borrower getBorrowerById(int id) {
        Borrower b = new Borrower();
        Cursor c = this.getReadableDatabase().query(BORROWERS_TABLE, null,
                BORROWERS_COLUMN_ID + "=?", new String[] { "" + id }, null,
                null, BORROWERS_COLUMN_ID);
        if (c.getCount() > 0) {
            c.moveToFirst();
            b = this.createBorrowerFromCursor(c);
        }
        c.close();
        this.getReadableDatabase().close();
        return b;
    }
    
    public Borrower getBorrowerByName(String name) {
        name = name.toLowerCase(Locale.getDefault()).trim();
        Borrower b = null;
        Cursor c = this.getReadableDatabase().query(BORROWERS_TABLE, null,
                BORROWERS_COLUMN_NAME + " LIKE ?", new String[] { name }, null,
                null, BORROWERS_COLUMN_ID);
        if (c.getCount() > 0) {
            c.moveToFirst();
            b = this.createBorrowerFromCursor(c);
        }
        c.close();
        this.getReadableDatabase().close();
        return b;
    }
    
    public void insertBorrower(Borrower aBorrower) {
        ContentValues values = new ContentValues();
        // The id is automatically added
        // values.put(BORROWERS_COLUMN_ID, aBorrower.getId());
        values.put(BORROWERS_COLUMN_NAME,
                aBorrower.getName().toLowerCase(Locale.getDefault()).trim());
        values.put(BORROWERS_COLUMN_NUMBER_OF_LOANS,
                aBorrower.getNumberOfLoans());
        values.put(BORROWERS_COLUMN_AVERAGE_LOAN_TIME,
                aBorrower.getAverageTime());
        this.insertObject(BORROWERS_TABLE, values);
    }
    
    public int getNumberOfBorrowers() {
    	Cursor cursor = this.getReadableDatabase().query(
				BORROWERS_TABLE,
				new String[] { BORROWERS_COLUMN_ID },
				null , null,
				null, null, BORROWERS_COLUMN_ID);
		int amount = cursor.getCount();
		cursor.close();
		this.getReadableDatabase().close();
		return (amount >= 0) ? amount : 0;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BORROWERS_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BORROWERS_TABLE_DROP);
        this.onCreate(db);
    }
    
    public void updateAverageTime(Borrower aBorrower, long averageTime) {
        ContentValues values = new ContentValues();
        values.put(BORROWERS_COLUMN_AVERAGE_LOAN_TIME, averageTime);
        this.getWritableDatabase().update(BORROWERS_TABLE, values,
                BORROWERS_COLUMN_ID + " = ?",
                new String[] { aBorrower.getId() + "" });
        this.getWritableDatabase().close();
    }
    
}
