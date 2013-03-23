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
package org.pierrrrrrrot.loanmanager.activities;

import java.util.Date;
import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.dao.BorrowersDAO;
import org.pierrrrrrrot.loanmanager.dao.LoansDAO;
import org.pierrrrrrrot.loanmanager.dao.ProductsDAO;
import org.pierrrrrrrot.loanmanager.model.Borrower;
import org.pierrrrrrrot.loanmanager.model.Loan;
import org.pierrrrrrrot.loanmanager.model.Product;
import org.pierrrrrrrot.loanmanager.utils.InformationFinder;
import org.pierrrrrrrot.loanmanager.utils.Utils;
import org.pierrrrrrrot.loanmanager.view.BorrowerAdapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * @author Pierre Reliquet
 */
public class AddLoan extends Activity {
    
    /**
     * The intent barcode
     */
    private static final int             INTENT_CODE_BARCODE = 7777;
    
    /**
     * The button used to add a new loan
     */
    private Button                       addLoan;
    /**
     * Adds the loan in the DB when the button is triggered.
     */
    private final Button.OnClickListener addLoanListener     = new Button.OnClickListener() {
                                                                 private Borrower createBorrower(
                                                                         String borrowerName) {
                                                                     Borrower tmp = AddLoan.this.borrowerDAO
                                                                             .getBorrowerByName(borrowerName);
                                                                     if (tmp == null) {
                                                                         tmp = new Borrower();
                                                                         tmp.setName(borrowerName);
                                                                         AddLoan.this.borrowerDAO
                                                                                 .insertBorrower(tmp);
                                                                         tmp = AddLoan.this.borrowerDAO
                                                                                 .getBorrowerByName(borrowerName);
                                                                     }
                                                                     return tmp;
                                                                 }
                                                                 
                                                                 private Product createProduct(
                                                                         String barcode,
                                                                         String productName) {
                                                                     Product tmp = AddLoan.this.produtsDAO
                                                                             .getProductByBarcode(barcode);
                                                                     if (tmp == null) {
                                                                         tmp = new Product(
                                                                                 Long.parseLong(barcode),
                                                                                 productName);
                                                                         if (!Utils
                                                                                 .isNullOrEmpty(AddLoan.this.productInfo
                                                                                         .getText()
                                                                                         .toString())) {
                                                                             tmp.setInfo(AddLoan.this.productInfo
                                                                                     .getText()
                                                                                     .toString());
                                                                         }
                                                                         AddLoan.this.produtsDAO
                                                                                 .insertProduct(tmp);
                                                                         tmp = AddLoan.this.produtsDAO
                                                                                 .getProductByBarcode(barcode);
                                                                     }
                                                                     return tmp;
                                                                 }
                                                                 
                                                                 @Override
                                                                 public void onClick(
                                                                         View v) {
                                                                     String borrowerName = AddLoan.this.borrower
                                                                             .getText()
                                                                             .toString();
                                                                     String barcodeText = AddLoan.this.productBarcode
                                                                             .getText()
                                                                             .toString();
                                                                     String titleText = AddLoan.this.productTitle
                                                                             .getText()
                                                                             .toString();
                                                                     if (!Utils
                                                                             .isNullOrEmpty(borrowerName)
                                                                             && !Utils
                                                                                     .isNullOrEmpty(barcodeText)
                                                                             && !Utils
                                                                                     .isNullOrEmpty(titleText)) {
                                                                         
                                                                         Borrower selectedBorrower = this
                                                                                 .createBorrower(borrowerName);
                                                                         Product selectedProduct = this
                                                                                 .createProduct(
                                                                                         barcodeText,
                                                                                         titleText);
                                                                         Loan l = new Loan(
                                                                                 selectedProduct,
                                                                                 selectedBorrower,
                                                                                 new Date());
                                                                         AddLoan.this.loansDAO
                                                                                 .insertLoan(l);
                                                                         Toast.makeText(
                                                                                 AddLoan.this
                                                                                         .getApplicationContext(),
                                                                                 AddLoan.this
                                                                                         .getString(R.string.loan_successfully_added),
                                                                                 Toast.LENGTH_LONG)
                                                                                 .show();
                                                                         selectedBorrower = null;
                                                                         selectedProduct = null;
                                                                     }
                                                                 }
                                                             };
    /**
     * The field to autocomplete the available borrowers
     */
    private AutoCompleteTextView         borrower;
    private BorrowersDAO                 borrowerDAO;
    
    /**
     * The list of available borrowers
     */
    private List<Borrower>               borrowers;
    private LoansDAO                     loansDAO;
    
    /**
     * The input to store the barcode
     */
    private EditText                     productBarcode;
    
    /**
     * Additional info about the product
     */
    private EditText                     productInfo;
    /**
     * The input to store the title of the product
     */
    private EditText                     productTitle;
    private ProductsDAO                  produtsDAO;
    
    /**
     * The button to launch the barcode scanner
     */
    private ImageButton                  scanBarcode;
    
    /**
     * Sets the image button to trigger the barcode scanner.
     */
    private final Button.OnClickListener scanCodeListener    = new Button.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(
                                                                         View v) {
                                                                     try {
                                                                         // Let's
                                                                         // try
                                                                         // to
                                                                         // launch
                                                                         // the
                                                                         // barcode
                                                                         // scanner
                                                                         Intent intent = new Intent(
                                                                                 "com.google.zxing.client.android.SCAN");
                                                                         // ONE_D_MODE
                                                                         // expected
                                                                         intent.putExtra(
                                                                                 "SCAN_MODE",
                                                                                 "ONE_D_MODE");
                                                                         // the
                                                                         // results
                                                                         // is
                                                                         // awaited
                                                                         AddLoan.this
                                                                                 .startActivityForResult(
                                                                                         intent,
                                                                                         INTENT_CODE_BARCODE);
                                                                     } catch (Exception e) { // if
                                                                                             // no
                                                                                             // barcode
                                                                                             // scanner
                                                                                             // exists
                                                                                             // on
                                                                                             // the
                                                                                             // phone
                                                                         // the
                                                                         // exception
                                                                         // is
                                                                         // thrown
                                                                         Log.e("NO_SCANNER",
                                                                                 "No barcode scanner detected"); // log
                                                                                                                 // the
                                                                                                                 // error
                                                                         // create
                                                                         // the
                                                                         // link
                                                                         // to
                                                                         // the
                                                                         // market
                                                                         final Intent market = new Intent(
                                                                                 Intent.ACTION_VIEW,
                                                                                 Uri.parse("market://search?q=zxing"));
                                                                         // let's
                                                                         // get
                                                                         // the
                                                                         // user
                                                                         // authorization
                                                                         // for
                                                                         // the
                                                                         // redirection.
                                                                         new AlertDialog.Builder(
                                                                                 AddLoan.this)
                                                                                 .setTitle(
                                                                                         R.string.go_store)
                                                                                 .setMessage(
                                                                                         R.string.store_confirmation)
                                                                                 .setPositiveButton(
                                                                                         R.string.yes,
                                                                                         new OnClickListener() {
                                                                                             @Override
                                                                                             public void onClick(
                                                                                                     DialogInterface arg0,
                                                                                                     int arg1) {
                                                                                                 AddLoan.this
                                                                                                         .startActivity(market); // if
                                                                                                                                 // the
                                                                                                                                 // user
                                                                                                                                 // press
                                                                                                                                 // yes
                                                                                                 // we
                                                                                                 // redirect
                                                                                                 // towards
                                                                                                 // the
                                                                                                 // market
                                                                                             }
                                                                                         })
                                                                                 .setNegativeButton(
                                                                                         R.string.no,
                                                                                         new OnClickListener() {
                                                                                             @Override
                                                                                             public void onClick(
                                                                                                     DialogInterface arg0,
                                                                                                     int arg1) {
                                                                                                 // Nothing
                                                                                                 // is
                                                                                                 // done
                                                                                             }
                                                                                         })
                                                                                 .show();
                                                                     }
                                                                 }
                                                             };
    
    /**
     * Get and setup the add loan button
     */
    private void initializeAddLoanButton() {
        this.addLoan = (Button) this.findViewById(R.id.add_loan_button);
        this.addLoan.setOnClickListener(this.addLoanListener);
    }
    
    /**
     * Initializes the borrower name field.
     */
    private void initializeBorrowerFields() {
        this.borrower = (AutoCompleteTextView) this
                .findViewById(R.id.add_loan_borrower_name_input);
        this.borrowers = this.borrowerDAO.getAllBorrowers();
        BorrowerAdapter adapter = new BorrowerAdapter(this,
                R.layout.borrower_row_layout, this.borrowers);
        this.borrower.setThreshold(2);
        this.borrower.setAdapter(adapter);
    }
    
    /**
     * Initializes the products field.
     */
    private void initializeProductFields() {
        this.scanBarcode = (ImageButton) this.findViewById(R.id.scan_barcode);
        this.productBarcode = (EditText) this.findViewById(R.id.barcode);
        this.productTitle = (EditText) this
                .findViewById(R.id.add_loan_input_title);
        this.scanBarcode.setOnClickListener(this.scanCodeListener);
        this.productInfo = (EditText) this
                .findViewById(R.id.add_loan_product_additional_info_input);
        
        // Add an event to detect when the focus is lost
        // This event is useful if the barcode is typed manually!
        this.productBarcode
                .setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!AddLoan.this.productBarcode.hasFocus()) {
                            AddLoan.this
                                    .parseScanCodeResults(AddLoan.this.productBarcode
                                            .getText().toString());
                        }
                    }
                });
    }
    
    /**
     * Handles the reception of a result by the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if ((requestCode == INTENT_CODE_BARCODE)
                    && (resultCode == RESULT_OK) && (data != null)) {
                this.parseScanCodeResults(data.getStringExtra("SCAN_RESULT"));
            }
        }
    }
    
    /**
     * The initialization of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_loan);
        this.setupActionBar();
        
        this.borrowerDAO = BorrowersDAO.getInstance();
        this.produtsDAO = ProductsDAO.getInstance();
        this.loansDAO = LoansDAO.getInstance();
        
        this.initializeProductFields();
        this.initializeBorrowerFields();
        this.initializeAddLoanButton();
    }
    
    /**
     * Parses the result of the barcode activity
     */
    private void parseScanCodeResults(String contents) {
        this.productBarcode.setText(contents);
        try {
            Product tmp = this.produtsDAO.getProductByBarcode(contents);
            if (tmp == null) {
                Toast.makeText(this, this.getString(R.string.internet_looking),
                        Toast.LENGTH_LONG).show();
                tmp = new Product();
                this.productTitle.setText(InformationFinder
                        .findTitleFromBarcode(contents));
            } else {
                this.productTitle.setText(tmp.getTitle());
            }
        } catch (Exception e) {
            Log.e("BARCODE", "Error while decoding the barcode", e);
        }
    }
    
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
}
