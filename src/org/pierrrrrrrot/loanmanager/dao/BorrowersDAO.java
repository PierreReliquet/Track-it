/**
 * 
 */
package org.pierrrrrrrot.loanmanager.dao;

import java.util.ArrayList;
import java.util.List;

import org.pierrrrrrrot.loanmanager.model.Borrower;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author preliquet
 * 
 */
public class BorrowersDAO extends DBHelper {

    private static final String BORROWERS_TABLE = "borrowers";

    private static final String BORROWERS_COLUMN_ID = "ID";
    private static final String BORROWERS_COLUMN_NAME = "NAME";
    private static final String BORROWERS_COLUMN_AVERAGE_LOAN_TIME = "AVERAGE_LOAN_TIME";
    private static final String BORROWERS_COLUMN_NUMBER_OF_LOANS = "NUMBER_LOAN";

    private static final String BORROWERS_TABLE_CREATE = "CREATE TABLE "
            + BORROWERS_TABLE + " (" + BORROWERS_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + BORROWERS_COLUMN_NAME
            + " TEXT NOT NULL," + BORROWERS_COLUMN_NUMBER_OF_LOANS
            + " INTEGER," + BORROWERS_COLUMN_AVERAGE_LOAN_TIME + " INTEGER"
            + ");";

    private static final String BORROWERS_TABLE_DROP = " DROP TABLE "
            + BORROWERS_TABLE + ";";

    public BorrowersDAO(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BORROWERS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BORROWERS_TABLE_DROP);
        onCreate(db);
    }

    public Borrower getBorrowerByName(String name) {
        Borrower b = null;
        Cursor c = getReadableDatabase().query(BORROWERS_TABLE, null,
                BORROWERS_COLUMN_NAME + " LIKE ?", new String[] { name }, null,
                null, BORROWERS_COLUMN_ID);
        if (c.getCount() > 0) {
            c.moveToFirst();
            b = createBorrowerFromCursor(c);
        }
        c.close();
        getReadableDatabase().close();
        return b;
    }

    public Borrower getBorrowerById(int id) {
        Borrower b = new Borrower();
        Cursor c = getReadableDatabase().query(BORROWERS_TABLE, null,
                BORROWERS_COLUMN_ID + "=?", new String[] { "" + id }, null,
                null, BORROWERS_COLUMN_ID);
        if (c.getCount() > 0) {
            c.moveToFirst();
            b = createBorrowerFromCursor(c);
        }
        c.close();
        getReadableDatabase().close();
        return b;
    }

    public void insertBorrower(Borrower aBorrower) {
        ContentValues values = new ContentValues();
        // values.put(BORROWERS_COLUMN_ID, aBorrower.getId());
        values.put(BORROWERS_COLUMN_NAME, aBorrower.getName());
        values.put(BORROWERS_COLUMN_NUMBER_OF_LOANS, 0);
        values.put(BORROWERS_COLUMN_AVERAGE_LOAN_TIME, 0);
        insertObject(BORROWERS_TABLE, values);
    }

    public List<Borrower> getAllBorrowers() {
        List<Borrower> borrowers = new ArrayList<Borrower>();
        Cursor borrower = getReadableDatabase().query(BORROWERS_TABLE, null,
                null, null, null, null, BORROWERS_COLUMN_ID);
        while (borrower.moveToNext()) {
            borrowers.add(createBorrowerFromCursor(borrower));
        }
        borrower.close();
        getReadableDatabase().close();
        return borrowers;
    }

    private Borrower createBorrowerFromCursor(Cursor c) {
        Borrower b = new Borrower();
        b.setId(Integer.parseInt(getStringFromColumn(c, BORROWERS_COLUMN_ID)));
        b.setName(getStringFromColumn(c, BORROWERS_COLUMN_NAME));
        b.setLoansAmount(Integer.parseInt(getStringFromColumn(c,
                BORROWERS_COLUMN_NUMBER_OF_LOANS)));
        b.setAverageTime(Integer.parseInt(getStringFromColumn(c,
                BORROWERS_COLUMN_AVERAGE_LOAN_TIME)));
        return b;
    }

}
