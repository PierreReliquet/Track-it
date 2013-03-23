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
    
    private static final int         DAY                   = 86400000;
    private static volatile LoansDAO instance              = null;
    private static final String      LOANS_COLUMN_BARCODE  = "BARCODE";
    private static final String      LOANS_COLUMN_BORROWER = "BORROWER";
    private static final String      LOANS_COLUMN_DATE     = "DATE";
    private static final String      LOANS_COLUMN_END_DATE = "DATE_END";
    private static final String      LOANS_COLUMN_ID       = "ID";
    
    private static final String      LOANS_TABLE           = "loans";
    
    private static final String      LOANS_TABLE_CREATE    = "CREATE TABLE IF NOT EXISTS "
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
    
    private static final String      LOANS_TABLE_DROP      = " DROP TABLE "
                                                                   + LOANS_TABLE
                                                                   + ";";
    
    public static LoansDAO getInstance() {
        if (LoansDAO.instance == null) {
            throw new RuntimeException("The init method should be called first");
        }
        return LoansDAO.instance;
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
    
    private final BorrowersDAO borrowerDAO;
    
    private final ProductsDAO  productsDAO;
    
    protected LoansDAO(Context context) {
        super(context);
        this.borrowerDAO = BorrowersDAO.getInstance();
        this.productsDAO = ProductsDAO.getInstance();
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
        int numberOfLoans = this.getNumberOfTerminatedLoanByBorrower(aLoan
                .getBorrower());
        
        long totalTime = averageTime * numberOfLoans;
        totalTime += lengthOfLoansInDays;
        // Compute the new average time
        averageTime = totalTime / (numberOfLoans + 1);
        this.borrowerDAO.updateAverageTime(aLoan.getBorrower(), averageTime);
        // Let's update the loan with the new values
        this.getWritableDatabase().update(LOANS_TABLE, values,
                LOANS_COLUMN_ID + " = ?", new String[] { id });
    }
    
    private Loan createLoanFromCursor(Cursor c) {
        Loan l = new Loan();
        l.setId(Long.parseLong(this.getStringFromColumn(c, LOANS_COLUMN_ID)));
        l.setProduct(this.productsDAO.getProductByBarcode(this
                .getStringFromColumn(c, LOANS_COLUMN_BARCODE)));
        l.setBorrower(this.borrowerDAO.getBorrowerById(Integer.parseInt(this
                .getStringFromColumn(c, LOANS_COLUMN_BORROWER))));
        l.setDate(new Date(Long.parseLong(this.getStringFromColumn(c,
                LOANS_COLUMN_DATE))));
        String endDate = this.getStringFromColumn(c, LOANS_COLUMN_END_DATE);
        if (!Utils.isNullOrEmpty(endDate)) {
            l.setEndDate(new Date(Long.parseLong(endDate)));
        }
        return l;
    }
    
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<Loan>();
        Cursor loan = this.getReadableDatabase().query(LOANS_TABLE, null, null,
                null, null, null, LOANS_COLUMN_ID);
        while (loan.moveToNext()) {
            loans.add(this.createLoanFromCursor(loan));
        }
        loan.close();
        this.getReadableDatabase().close();
        return loans;
    }
    
    public List<Loan> getAllNonFinishedLoans(String borrowerId) {
        Cursor loan;
        if (borrowerId == null) {
            loan = this.getReadableDatabase().query(LOANS_TABLE, null,
                    LOANS_COLUMN_END_DATE + " = 0", null, null, null,
                    LOANS_COLUMN_ID);
        } else {
            loan = this.getReadableDatabase().query(
                    LOANS_TABLE,
                    null,
                    LOANS_COLUMN_END_DATE + " = 0 AND " + LOANS_COLUMN_BORROWER
                            + " = ?", new String[] { borrowerId }, null, null,
                    LOANS_COLUMN_ID);
        }
        List<Loan> loans = new ArrayList<Loan>();
        
        while (loan.moveToNext()) {
            loans.add(this.createLoanFromCursor(loan));
        }
        loan.close();
        this.getReadableDatabase().close();
        return loans;
    }
    
    public int getNumberOfNonTerminatedLoanByBorrower(Borrower aBorrower) {
        Cursor cursor = this.getReadableDatabase().query(
                LOANS_TABLE,
                new String[] { LOANS_COLUMN_ID },
                LOANS_COLUMN_BORROWER + " = ?  AND " + LOANS_COLUMN_END_DATE
                        + " = 0", new String[] { aBorrower.getId() + "" },
                null, null, LOANS_COLUMN_ID);
        int amount = cursor.getCount();
        cursor.close();
        this.getReadableDatabase().close();
        return (amount >= 0) ? amount : 0;
    }
    
    public int getNumberOfTerminatedLoanByBorrower(Borrower aBorrower) {
        Cursor cursor = this.getReadableDatabase().query(
                LOANS_TABLE,
                new String[] { LOANS_COLUMN_ID },
                LOANS_COLUMN_BORROWER + " = ? AND " + LOANS_COLUMN_END_DATE
                        + " <> 0", new String[] { aBorrower.getId() + "" },
                null, null, LOANS_COLUMN_ID);
        int amount = cursor.getCount();
        cursor.close();
        this.getReadableDatabase().close();
        return (amount >= 0) ? amount : 0;
    }
    
    public void insertLoan(Loan aLoan) {
        ContentValues values = new ContentValues();
        values.put(LOANS_COLUMN_BARCODE, aLoan.getProduct().getBarcode());
        values.put(LOANS_COLUMN_BORROWER, aLoan.getBorrower().getId());
        values.put(LOANS_COLUMN_DATE, new Date().getTime());
        values.put(LOANS_COLUMN_END_DATE, 0);
        this.insertObject(LOANS_TABLE, values);
        this.borrowerDAO.addLoan(aLoan.getBorrower());
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOANS_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LOANS_TABLE_DROP);
        this.onCreate(db);
    }
}
