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
    
    public static final int    INTENT_CODE_CLOSE_LOAN             = 7778;
    public static final String INTENT_CODE_CLOSE_LOAN_BORROWER_ID = "BORROWER";
    
    private LoansAdapter       adapter;
    private String             borrowerId;
    private ListView           loans;
    private LoansDAO           loansDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list_loans);
        // Show the Up button in the action bar.
        this.setupActionBar();
        this.borrowerId = this.getIntent().getStringExtra(
                INTENT_CODE_CLOSE_LOAN_BORROWER_ID);
        
        this.loansDAO = LoansDAO.getInstance();
        this.loans = (ListView) this.findViewById(R.id.list_loans_list);
        this.refreshUI();
        this.adapter = new LoansAdapter(this, R.layout.loan_row_layout,
                this.loansDAO.getAllNonFinishedLoans(this.borrowerId));
        this.loans.setAdapter(this.adapter);
        this.loans.setOnItemClickListener(new OnItemClickListener() {
            
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
                                Loan l = CloseLoan.this.adapter.getLoans().get(
                                        position);
                                CloseLoan.this.loansDAO.closeLoan(l);
                                CloseLoan.this.refreshUI();
                            }
                        }).show();
            }
        });
    }
    
    private void refreshUI() {
        List<Loan> loansList = this.loansDAO
                .getAllNonFinishedLoans(this.borrowerId);
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
}
