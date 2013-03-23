/**
 * 
 */
package org.pierrrrrrrot.loanmanager.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pierrrrrrrot.loanmanager.model.Borrower;
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

    private static final int DAY = 86400000;
    private static final String LOANS_TABLE = "loans";
    private static final String LOANS_COLUMN_ID = "ID";
    private static final String LOANS_COLUMN_BARCODE = "BARCODE";
    private static final String LOANS_COLUMN_BORROWER = "BORROWER";
    private static final String LOANS_COLUMN_DATE = "DATE";
    private static final String LOANS_COLUMN_END_DATE = "DATE_END";

    private static final String LOANS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + LOANS_TABLE
            + " ("
            + LOANS_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LOANS_COLUMN_BARCODE
            + " INTEGER,"
            + LOANS_COLUMN_BORROWER
            + " INTEGER,"
            + LOANS_COLUMN_DATE
            + " INTEGER NOT NULL,"
            + LOANS_COLUMN_END_DATE
            + " INTEGER);";

    private static final String LOANS_TABLE_DROP = " DROP TABLE " + LOANS_TABLE
            + ";";

    private final BorrowersDAO borrowerDAO;
    private final ProductsDAO productsDAO;

    private static volatile LoansDAO instance = null;

    protected LoansDAO(Context context) {
        super(context);
        borrowerDAO = BorrowersDAO.getInstance();
        productsDAO = ProductsDAO.getInstance();
    }

    public static void init(Context context) {
        if (LoansDAO.instance == null) {
            synchronized (LoansDAO.class) {
                if (LoansDAO.instance == null) {
                    LoansDAO.instance = new LoansDAO(context);
                }
            }
        }
    }

    public static LoansDAO getInstance() {
        if (LoansDAO.instance == null) {
            throw new RuntimeException("The init method should be called first");
        }
        return LoansDAO.instance;
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
        values.put(LOANS_COLUMN_END_DATE, 0);
        insertObject(LOANS_TABLE, values);
        borrowerDAO.addLoan(aLoan.getBorrower());
    }

    public void closeLoan(Loan aLoan) {
        String id = "" + aLoan.getId();
        ContentValues values = new ContentValues();
        long endDate = new Date().getTime();
        values.put(LOANS_COLUMN_END_DATE, endDate);
        // compute the day between today and the starting date.
        long lengthOfLoansInDays = (endDate - aLoan.getDate().getTime())
                / (DAY);
        if (lengthOfLoansInDays <= 0) { // if the loan lenth is equal to 0
            lengthOfLoansInDays = 1; // let's put to one day
        }
        long averageTime = aLoan.getBorrower().getAverageTime();
        // get the number of non-terminated loan
        int numberOfLoans = getNumberOfTerminatedLoanByBorrower(aLoan
                .getBorrower());

        long totalTime = averageTime * numberOfLoans;
        totalTime += lengthOfLoansInDays;
        // Compute the new average time
        averageTime = totalTime / (numberOfLoans + 1);
        borrowerDAO.updateAverageTime(aLoan.getBorrower(), averageTime);
        // Let's update the loan with the new values
        getWritableDatabase().update(LOANS_TABLE, values,
                LOANS_COLUMN_ID + " = ?", new String[] { id });
    }

    public List<Loan> getAllNonFinishedLoans(String borrowerId) {
        Cursor loan;
        if (borrowerId == null) {
            loan = getReadableDatabase().query(LOANS_TABLE, null,
                    LOANS_COLUMN_END_DATE + " = 0", null, null, null,
                    LOANS_COLUMN_ID);
        } else {
            loan = getReadableDatabase().query(
                    LOANS_TABLE,
                    null,
                    LOANS_COLUMN_END_DATE + " = 0 AND " + LOANS_COLUMN_BORROWER
                            + " = ?", new String[] { borrowerId }, null, null,
                    LOANS_COLUMN_ID);
        }
        List<Loan> loans = new ArrayList<Loan>();

        while (loan.moveToNext()) {
            loans.add(createLoanFromCursor(loan));
        }
        loan.close();
        getReadableDatabase().close();
        return loans;
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
        l.setId(Long.parseLong(getStringFromColumn(c, LOANS_COLUMN_ID)));
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

    public int getNumberOfTerminatedLoanByBorrower(Borrower aBorrower) {
        Cursor cursor = getReadableDatabase().query(
                LOANS_TABLE,
                new String[] { LOANS_COLUMN_ID },
                LOANS_COLUMN_BORROWER + " = ? AND " + LOANS_COLUMN_END_DATE
                        + " <> 0", new String[] { aBorrower.getId() + "" },
                null, null, LOANS_COLUMN_ID);
        int amount = cursor.getCount();
        cursor.close();
        getReadableDatabase().close();
        return (amount >= 0) ? amount : 0;
    }

    public int getNumberOfNonTerminatedLoanByBorrower(Borrower aBorrower) {
        Cursor cursor = getReadableDatabase().query(
                LOANS_TABLE,
                new String[] { LOANS_COLUMN_ID },
                LOANS_COLUMN_BORROWER + " = ?  AND " + LOANS_COLUMN_END_DATE
                        + " = 0", new String[] { aBorrower.getId() + "" },
                null, null, LOANS_COLUMN_ID);
        int amount = cursor.getCount();
        cursor.close();
        getReadableDatabase().close();
        return (amount >= 0) ? amount : 0;
    }
}
