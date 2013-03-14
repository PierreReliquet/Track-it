package org.pierrrrrrrot.loanmanager.dao;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.pierrrrrrrot.loanmanager.model.Borrower;
import org.pierrrrrrrot.loanmanager.model.Loan;
import org.pierrrrrrrot.loanmanager.model.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

/**
 * @author Pierre Reliquet
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "loanmanager.db";
    private static final int DB_VERSION = 1;

    private static final String DATE_PATTERN_DB = "yyyy-MM-dd";
    private static final String DATE_PATTERN_USE = "dd/MM/yyyy";
    private static final DateFormat FORMATTER_DB = new DateFormat();

    private static final String LOANS_TABLE = "loans";
    private static final String BORROWERS_TABLE = "borrowers";
    private static final String PRODUCTS_TABLE = "products";

    private static final String BORROWERS_COLUMN_ID = "ID";
    private static final String BORROWERS_COLUMN_NAME = "NAME";
    private static final String BORROWERS_COLUMN_AVERAGE_LOAN_TIME = "AVERAGE_LOAN_TIME";
    private static final String BORROWERS_COLUMN_NUMBER_OF_LOANS = "NUMBER_LOAN";

    private static final String PRODUCTS_COLUMN_ID = "ID";
    private static final String PRODUCTS_COLUMN_NAME = "NAME";
    private static final String PRODUCTS_COLUMN_INFO = "INFO";

    private static final String LOANS_COLUMN_ID = "ID";
    private static final String LOANS_COLUMN_BARCODE = "BARCODE";
    private static final String LOANS_COLUMN_BORROWER = "BORROWER";
    private static final String LOANS_COLUMN_DATE = "DATE";
    private static final String LOANS_COLUMN_END_DATE = "DATE_END";

    private static final String BORROWERS_TABLE_CREATE = "CREATE TABLE "
            + BORROWERS_TABLE + " (" + BORROWERS_COLUMN_ID
            + " INTEGER PRIMARY KEY," + BORROWERS_COLUMN_NAME
            + " TEXT NOT NULL," + BORROWERS_COLUMN_NUMBER_OF_LOANS
            + " INTEGER," + BORROWERS_COLUMN_AVERAGE_LOAN_TIME + " INTEGER"
            + ");";

    private static final String LOANS_TABLE_CREATE = "CREATE TABLE "
            + LOANS_TABLE + " (" + LOANS_COLUMN_ID
            + " INTEGER PRIMARY KEY ASC," + LOANS_COLUMN_BARCODE + " INTEGER,"
            + LOANS_COLUMN_BORROWER + " INTEGER," + LOANS_COLUMN_DATE
            + " DATE NOT NULL," + LOANS_COLUMN_END_DATE + " DATE" + ");";

    private static final String PRODUCTS_TABLE_CREATE = "CREATE TABLE "
            + PRODUCTS_TABLE + " (" + PRODUCTS_COLUMN_ID
            + " INTEGER PRIMARY KEY," + PRODUCTS_COLUMN_NAME
            + " TEXT NOT NULL," + PRODUCTS_COLUMN_INFO + " TEXT" + ");";

    // private static final String POKEMON_QUERY_FILTER = POKEMON_COLUMN_NAME
    // + " LIKE ?";

    private static final String PRODUCTS_TABLE_DROP = " DROP TABLE "
            + PRODUCTS_TABLE + ";";

    private static final String BORROWERS_TABLE_DROP = " DROP TABLE "
            + BORROWERS_TABLE + ";";

    private static final String LOANS_TABLE_DROP = " DROP TABLE " + LOANS_TABLE
            + ";";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOANS_TABLE_CREATE);
        db.execSQL(BORROWERS_TABLE_CREATE);
        db.execSQL(PRODUCTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BORROWERS_TABLE_DROP);
        db.execSQL(LOANS_TABLE_DROP);
        db.execSQL(PRODUCTS_TABLE_DROP);
        onCreate(db);
    }

    public void insertProduct(SQLiteDatabase db, Product aProduct) {
        ContentValues values = new ContentValues();
        values.put(PRODUCTS_COLUMN_ID, aProduct.getBarcode());
        values.put(PRODUCTS_COLUMN_NAME, aProduct.getTitle());
        values.put(PRODUCTS_COLUMN_INFO, aProduct.getInfo());
        insertObject(db, PRODUCTS_TABLE, values);
    }

    public void insertBorrower(SQLiteDatabase db, Borrower aBorrower) {
        ContentValues values = new ContentValues();
        // values.put(BORROWERS_COLUMN_ID, aBorrower.getId());
        values.put(BORROWERS_COLUMN_NAME, aBorrower.getName());
        values.put(BORROWERS_COLUMN_NUMBER_OF_LOANS, 0);
        values.put(BORROWERS_COLUMN_AVERAGE_LOAN_TIME, 0);
        insertObject(db, BORROWERS_TABLE, values);
    }

    public void insertLoan(SQLiteDatabase db, Loan aLoan) {
        ContentValues values = new ContentValues();
        values.put(LOANS_COLUMN_BARCODE, aLoan.getProduct().getBarcode());
        values.put(LOANS_COLUMN_BORROWER, aLoan.getBorrower().getId());
        values.put(LOANS_COLUMN_DATE, new Date().toString());
        insertObject(db, LOANS_TABLE, values);
    }

    private void insertObject(SQLiteDatabase db, String table,
            ContentValues values) {
        if (db == null) {
            getWritableDatabase().insert(table, null, values);
            getWritableDatabase().close();
        } else {
            db.insert(table, null, values);
            db.close();
        }
    }

    public Borrower getBorrowerByName(String name) {
        Borrower b = new Borrower();
        Cursor c = getReadableDatabase().query(BORROWERS_TABLE, null,
                BORROWERS_COLUMN_NAME + "=?", new String[] { name }, null,
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

    public Product getProductByBarcode(int barcode) {
        Product p = new Product();
        Cursor c = getReadableDatabase().query(PRODUCTS_TABLE, null,
                PRODUCTS_COLUMN_ID + "=?", new String[] { "" + barcode }, null,
                null, PRODUCTS_COLUMN_ID);
        if (c.getCount() > 0) {
            c.moveToFirst();
            p = createProductFromCursor(c);
        }
        c.close();
        getReadableDatabase().close();
        return p;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new LinkedList<Loan>();
        Cursor loan = getReadableDatabase().query(LOANS_TABLE, null, null,
                null, null, null, LOANS_COLUMN_ID);
        if (loan.getCount() > 0) {
            while (loan.moveToNext()) {
                loans.add(createLoanFromCursor(loan));
            }
        }
        loan.close();
        getReadableDatabase().close();
        return loans;
    }

    private String getStringFromColumn(Cursor c, String column) {
        return c.getString(c.getColumnIndex(column));
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

    private Product createProductFromCursor(Cursor c) {
        Product p = new Product();
        p.setBarcode(Integer
                .parseInt(getStringFromColumn(c, PRODUCTS_COLUMN_ID)));
        p.setTitle(getStringFromColumn(c, PRODUCTS_COLUMN_NAME));
        p.setInfo(getStringFromColumn(c, PRODUCTS_COLUMN_INFO));
        return p;
    }

    private Loan createLoanFromCursor(Cursor c) {
        Loan l = new Loan();
        l.setProduct(getProductByBarcode(Integer.parseInt(getStringFromColumn(
                c, LOANS_COLUMN_BARCODE))));
        l.setBorrower(getBorrowerById(Integer.parseInt(getStringFromColumn(c,
                LOANS_COLUMN_BORROWER))));
        return null;
    }
}