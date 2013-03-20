/**
 * 
 */
package org.pierrrrrrrot.loanmanager.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pierrrrrrrot.loanmanager.model.Loan;
import org.pierrrrrrrot.loanmanager.utils.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Pierre Reliquet
 * 
 */
public class LoansDAO extends DBHelper {

    private static final String LOANS_TABLE = "loans";
    private static final String LOANS_COLUMN_ID = "ID";
    private static final String LOANS_COLUMN_BARCODE = "BARCODE";
    private static final String LOANS_COLUMN_BORROWER = "BORROWER";
    private static final String LOANS_COLUMN_DATE = "DATE";
    private static final String LOANS_COLUMN_END_DATE = "DATE_END";

    private static final String LOANS_TABLE_CREATE = "CREATE TABLE "
            + LOANS_TABLE + " (" + LOANS_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + LOANS_COLUMN_BARCODE
            + " INTEGER," + LOANS_COLUMN_BORROWER + " INTEGER,"
            + LOANS_COLUMN_DATE + " INTEGER NOT NULL," + LOANS_COLUMN_END_DATE
            + " INTEGER);";

    private static final String LOANS_TABLE_DROP = " DROP TABLE " + LOANS_TABLE
            + ";";

    private final BorrowersDAO borrowerDAO;
    private final ProductsDAO productsDAO;

    public LoansDAO(Context context) {
        super(context);
        borrowerDAO = new BorrowersDAO(context);
        productsDAO = new ProductsDAO(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOANS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LOANS_TABLE_DROP);
        onCreate(db);
    }

    public void insertLoan(Loan aLoan) {
        ContentValues values = new ContentValues();
        values.put(LOANS_COLUMN_BARCODE, aLoan.getProduct().getBarcode());
        values.put(LOANS_COLUMN_BORROWER, aLoan.getBorrower().getId());
        values.put(LOANS_COLUMN_DATE, new Date().getTime());
        insertObject(LOANS_TABLE, values);
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<Loan>();
        Cursor loan = getReadableDatabase().query(LOANS_TABLE, null, null,
                null, null, null, LOANS_COLUMN_ID);
        while (loan.moveToNext()) {
            loans.add(createLoanFromCursor(loan));
        }
        loan.close();
        getReadableDatabase().close();
        return loans;
    }

    private Loan createLoanFromCursor(Cursor c) {
        Loan l = new Loan();
        l.setProduct(productsDAO.getProductByBarcode(getStringFromColumn(c,
                LOANS_COLUMN_BARCODE)));
        l.setBorrower(borrowerDAO.getBorrowerById(Integer
                .parseInt(getStringFromColumn(c, LOANS_COLUMN_BORROWER))));
        l.setDate(new Date(Long.parseLong(getStringFromColumn(c,
                LOANS_COLUMN_DATE))));
        String endDate = getStringFromColumn(c, LOANS_COLUMN_END_DATE);
        if (!Utils.isNullOrEmpty(endDate))
            l.setEndDate(new Date(Long.parseLong(endDate)));
        return l;
    }
}
