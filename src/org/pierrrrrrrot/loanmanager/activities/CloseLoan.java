package org.pierrrrrrrot.loanmanager.activities;

import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.dao.LoansDAO;
import org.pierrrrrrrot.loanmanager.model.Loan;
import org.pierrrrrrrot.loanmanager.view.LoansAdapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CloseLoan extends Activity {

    public static final int INTENT_CODE_CLOSE_LOAN = 7778;
    public static final String INTENT_CODE_CLOSE_LOAN_BORROWER_ID = "BORROWER";

    private ListView loans;
    private LoansDAO loansDAO;
    private LoansAdapter adapter;
    private String borrowerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_loans);
        // Show the Up button in the action bar.
        setupActionBar();
        borrowerId = getIntent().getStringExtra(
                INTENT_CODE_CLOSE_LOAN_BORROWER_ID);

        loansDAO = LoansDAO.getInstance();
        loans = (ListView) findViewById(R.id.list_loans_list);
        refreshUI();
        adapter = new LoansAdapter(this, R.layout.loan_row_layout,
                loansDAO.getAllNonFinishedLoans(borrowerId));
        loans.setAdapter(adapter);
        loans.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    final int position, long id) {
                new AlertDialog.Builder(CloseLoan.this)
                        .setTitle(R.string.confirm_closing_title)
                        .setMessage(R.string.confirm_closing_content)
                        .setPositiveButton(R.string.no, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                // Nothing to do stay on the page
                            }
                        })
                        .setNegativeButton(R.string.yes, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                Loan l = adapter.getLoans().get(position);
                                loansDAO.closeLoan(l);
                                refreshUI();
                            }
                        }).show();
            }
        });
    }

    private void refreshUI() {
        List<Loan> loansList = loansDAO.getAllNonFinishedLoans(borrowerId);
        adapter = new LoansAdapter(this, R.layout.loan_row_layout, loansList);
        loans.setAdapter(adapter);
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
}
