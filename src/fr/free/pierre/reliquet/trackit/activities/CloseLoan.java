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
package fr.free.pierre.reliquet.trackit.activities;

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
import android.widget.Toast;
import fr.free.pierre.reliquet.trackit.R;
import fr.free.pierre.reliquet.trackit.dao.LoansDAO;
import fr.free.pierre.reliquet.trackit.enums.CloseLoanFilter;
import fr.free.pierre.reliquet.trackit.model.Loan;
import fr.free.pierre.reliquet.trackit.view.LoansAdapter;

import java.util.List;

public class CloseLoan extends Activity implements OnItemClickListener {

    public static final String CLOSE_LOAN_FILTER_EXTRA = "CLOSE_LOAN_FILTER_EXTRA";
    /**
     * The constant used to provide the borrower id when the close loan activity
     * should display only a specific borrower
     */
    public static final String BORROWER_ID_EXTRA = "BORROWER";
    /**
     * This constant is used to provide the product_id when the close loan
     * screen should display only a specific product
     */
    public static final String PRODUCT_ID_EXTRA = "PRODUCT_ID";

    private LoansAdapter adapter;
    private CloseLoanFilter type;
    private String id;
    private ListView loans;
    private LoansDAO loansDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list_loans);
        // Show the Up button in the action bar.
        this.setupActionBar();
        this.loansDAO = LoansDAO.getInstance();
        this.loans = (ListView) this.findViewById(R.id.list_loans_list);

        this.type = CloseLoanFilter.valueOf(this.getIntent().getStringExtra(CLOSE_LOAN_FILTER_EXTRA));
        switch (type) {
            case PRODUCT:
                this.id = this.getIntent().getStringExtra(PRODUCT_ID_EXTRA);
                this.adapter = new LoansAdapter(this, R.layout.loan_row_layout,
                        this.loansDAO.getLoanByProductId(id));
                break;
            case BORROWER:
                this.id = this.getIntent().getStringExtra(BORROWER_ID_EXTRA);
                this.adapter = new LoansAdapter(this, R.layout.loan_row_layout,
                        this.loansDAO.getAllNonFinishedLoans(this.id));
                break;
            case ALL:
                this.adapter = new LoansAdapter(this, R.layout.loan_row_layout,
                        this.loansDAO.getAllNonFinishedLoans());
                break;
            default:
                throw new RuntimeException("This case should not append!");
        }
        this.loans.setAdapter(this.adapter);
        this.loans.setOnItemClickListener(this);
    }

    private void refreshUI() {
        List<Loan> loansList;
        switch (type) {
            case PRODUCT:
                loansList = this.loansDAO.getLoanByProductId(this.id);
                break;
            case BORROWER:
                loansList = this.loansDAO.getAllNonFinishedLoans(this.id);
                break;
            default:
                loansList = this.loansDAO.getAllNonFinishedLoans(null);
                break;
        }
        if (loansList.isEmpty()) {
            Toast.makeText(this, R.string.no_more_loans, Toast.LENGTH_LONG).show();
            this.finish();
        }
        this.adapter = new LoansAdapter(this, R.layout.loan_row_layout,
                loansList);
        this.loans.setAdapter(this.adapter);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            final int position, long id) {
        new AlertDialog.Builder(CloseLoan.this)
                .setTitle(R.string.confirm_closing_title)
                .setMessage(R.string.confirm_closing_content)
                .setNegativeButton(R.string.no, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing to do stay on the page
                    }
                }).setPositiveButton(R.string.yes, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Loan l = CloseLoan.this.adapter.getLoans()
                        .get(position);
                CloseLoan.this.loansDAO.closeLoan(l);
                CloseLoan.this.refreshUI();
            }
        }).show();

    }
}
