/**
 * 
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
    private static final int INTENT_CODE_BARCODE = 7777;

    /**
     * The button to launch the barcode scanner
     */
    private ImageButton scanBarcode;
    /**
     * The input to store the barcode
     */
    private EditText productBarcode;
    /**
     * The input to store the title of the product
     */
    private EditText productTitle;
    /**
     * Additional info about the product
     */
    private EditText productInfo;

    /**
     * The field to autocomplete the available borrowers
     */
    private AutoCompleteTextView borrower;
    /**
     * The list of available borrowers
     */
    private List<Borrower> borrowers;

    /**
     * The button used to add a new loan
     */
    private Button addLoan;

    private LoansDAO loansDAO;
    private BorrowersDAO borrowerDAO;
    private ProductsDAO produtsDAO;

    /**
     * The initialization of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);
        setupActionBar();

        borrowerDAO = BorrowersDAO.getInstance();
        produtsDAO = ProductsDAO.getInstance();
        loansDAO = LoansDAO.getInstance();

        initializeProductFields();
        initializeBorrowerFields();
        initializeAddLoanButton();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Get and setup the add loan button
     */
    private void initializeAddLoanButton() {
        addLoan = (Button) findViewById(R.id.add_loan_button);
        addLoan.setOnClickListener(addLoanListener);
    }

    /**
     * Initializes the borrower name field.
     */
    private void initializeBorrowerFields() {
        borrower = (AutoCompleteTextView) findViewById(R.id.add_loan_borrower_name_input);
        borrowers = borrowerDAO.getAllBorrowers();
        BorrowerAdapter adapter = new BorrowerAdapter(this,
                R.layout.borrower_row_layout, borrowers);
        borrower.setThreshold(2);
        borrower.setAdapter(adapter);
    }

    /**
     * Initializes the products field.
     */
    private void initializeProductFields() {
        scanBarcode = (ImageButton) findViewById(R.id.scan_barcode);
        productBarcode = (EditText) findViewById(R.id.barcode);
        productTitle = (EditText) findViewById(R.id.add_loan_input_title);
        scanBarcode.setOnClickListener(scanCodeListener);
        productInfo = (EditText) findViewById(R.id.add_loan_product_additional_info_input);
    }

    /**
     * Handles the reception of a result by the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == INTENT_CODE_BARCODE && resultCode == RESULT_OK
                    && data != null) {
                parseScanCodeResults(data.getStringExtra("SCAN_RESULT"));
            }
        }
    }

    /**
     * Parses the result of the barcode activity
     */
    private void parseScanCodeResults(String contents) {
        productBarcode.setText(contents);
        try {
            Product tmp = produtsDAO.getProductByBarcode(contents);
            if (tmp == null) {
                tmp = new Product();
                Toast.makeText(this, getString(R.string.internet_looking),
                        Toast.LENGTH_LONG).show();
                productTitle.setText(InformationFinder
                        .findTitleFromBarcode(contents));
            } else {
                productTitle.setText(tmp.getTitle());
            }
        } catch (Exception e) {
            Log.e("BARCODE", "Error while decoding the barcode", e);
        }
    }

    /**
     * Sets the image button to trigger the barcode scanner.
     */
    private final Button.OnClickListener scanCodeListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                // Let's try to launch the barcode scanner
                Intent intent = new Intent(
                        "com.google.zxing.client.android.SCAN");
                // ONE_D_MODE expected
                intent.putExtra("SCAN_MODE", "ONE_D_MODE");
                // the results is awaited
                startActivityForResult(intent, INTENT_CODE_BARCODE);
            } catch (Exception e) { // if no barcode scanner exists on the phone
                // the exception is thrown
                Log.e("NO_SCANNER", "No barcode scanner detected"); // log the
                                                                    // error
                // create the link to the market
                final Intent market = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://search?q=zxing"));
                // let's get the user authorization for the redirection.
                new AlertDialog.Builder(AddLoan.this)
                        .setTitle(R.string.go_store)
                        .setMessage(R.string.store_confirmation)
                        .setPositiveButton(R.string.yes, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                startActivity(market); // if the user press yes
                                // we redirect towards
                                // the market
                            }
                        })
                        .setNegativeButton(R.string.no, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Nothing is done
                            }
                        }).show();
            }
        }
    };

    /**
     * Adds the loan in the DB when the button is triggered.
     */
    private final Button.OnClickListener addLoanListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String borrowerName = borrower.getText().toString();
            String barcodeText = productBarcode.getText().toString();
            String titleText = productTitle.getText().toString();
            if (!Utils.isNullOrEmpty(borrowerName)
                    && !Utils.isNullOrEmpty(barcodeText)
                    && !Utils.isNullOrEmpty(titleText)) {

                Borrower selectedBorrower = createBorrower(borrowerName);
                Product selectedProduct = createProduct(barcodeText, titleText);
                Loan l = new Loan(selectedProduct, selectedBorrower, new Date());
                loansDAO.insertLoan(l);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.loan_successfully_added),
                        Toast.LENGTH_LONG).show();
                selectedBorrower = null;
                selectedProduct = null;
            }
        }

        private Borrower createBorrower(String borrowerName) {
            Borrower tmp = borrowerDAO.getBorrowerByName(borrowerName);
            if (tmp == null) {
                tmp = new Borrower();
                tmp.setName(borrowerName);
                borrowerDAO.insertBorrower(tmp);
                tmp = borrowerDAO.getBorrowerByName(borrowerName);
            }
            return tmp;
        }

        private Product createProduct(String barcode, String productName) {
            Product tmp = produtsDAO.getProductByBarcode(barcode);
            if (tmp == null) {
                tmp = new Product(Long.parseLong(barcode), productName);
                if (!Utils.isNullOrEmpty(productInfo.getText().toString())) {
                    tmp.setInfo(productInfo.getText().toString());
                }
                produtsDAO.insertProduct(tmp);
                tmp = produtsDAO.getProductByBarcode(barcode);
            }
            return tmp;
        }
    };

}
