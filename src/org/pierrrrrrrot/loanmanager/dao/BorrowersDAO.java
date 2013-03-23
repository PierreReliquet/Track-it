/**
 * 
 */
package org.pierrrrrrrot.loanmanager.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.pierrrrrrrot.loanmanager.model.Borrower;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Pierre Reliquet
 * 
 */
public class BorrowersDAO extends DBHelper {

    private static final String BORROWERS_TABLE = "borrowers";

    private static final String BORROWERS_COLUMN_ID = "ID";
    private static final String BORROWERS_COLUMN_NAME = "NAME";
    private static final String BORROWERS_COLUMN_AVERAGE_LOAN_TIME = "AVERAGE_LOAN_TIME";
    private static final String BORROWERS_COLUMN_NUMBER_OF_LOANS = "NUMBER_LOAN";

    private static final String BORROWERS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS  "
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

    private static final String BORROWERS_TABLE_DROP = " DROP TABLE "
            + BORROWERS_TABLE + ";";

    private static volatile BorrowersDAO instance = null;

    protected BorrowersDAO(Context context) {
        super(context);
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

    public static BorrowersDAO getInstance() {
        if (BorrowersDAO.instance == null) {
            throw new RuntimeException("The init method should be called first");
        }
        return BorrowersDAO.instance;
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
        name = name.toLowerCase(Locale.getDefault()).trim();
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
        // The id is automatically added
        // values.put(BORROWERS_COLUMN_ID, aBorrower.getId());
        values.put(BORROWERS_COLUMN_NAME,
                aBorrower.getName().toLowerCase(Locale.getDefault()).trim());
        values.put(BORROWERS_COLUMN_NUMBER_OF_LOANS,
                aBorrower.getNumberOfLoans());
        values.put(BORROWERS_COLUMN_AVERAGE_LOAN_TIME,
                aBorrower.getAverageTime());
        insertObject(BORROWERS_TABLE, values);
    }

    public void addLoan(Borrower aBorrower) {
        ContentValues values = new ContentValues();
        values.put(BORROWERS_COLUMN_NUMBER_OF_LOANS,
                aBorrower.getNumberOfLoans() + 1);
        getWritableDatabase().update(BORROWERS_TABLE, values,
                BORROWERS_COLUMN_ID + " = ?",
                new String[] { aBorrower.getId() + "" });
        getWritableDatabase().close();
    }

    public void updateAverageTime(Borrower aBorrower, long averageTime) {
        ContentValues values = new ContentValues();
        values.put(BORROWERS_COLUMN_AVERAGE_LOAN_TIME, averageTime);
        getWritableDatabase().update(BORROWERS_TABLE, values,
                BORROWERS_COLUMN_ID + " = ?",
                new String[] { aBorrower.getId() + "" });
        getWritableDatabase().close();
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
        b.setNumberOfLoans(Integer.parseInt(getStringFromColumn(c,
                BORROWERS_COLUMN_NUMBER_OF_LOANS)));
        b.setAverageTime(Integer.parseInt(getStringFromColumn(c,
                BORROWERS_COLUMN_AVERAGE_LOAN_TIME)));
        return b;
    }

}
