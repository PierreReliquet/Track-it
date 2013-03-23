package org.pierrrrrrrot.loanmanager.activities;

import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.dao.LoansDAO;
import org.pierrrrrrrot.loanmanager.model.Loan;
import org.pierrrrrrrot.loanmanager.view.LoansAdapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

public class ListLoans extends Activity {

    private ListView loans;
    private LoansDAO loansDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_loans);
        // Show the Up button in the action bar.
        setupActionBar();
        loansDAO = LoansDAO.getInstance();
        loans = (ListView) findViewById(R.id.list_loans_list);
        List<Loan> loansList = loansDAO.getAllLoans();
        LoansAdapter loansAdapter = new LoansAdapter(this,
                R.layout.loan_row_layout, loansList);
        loans.setAdapter(loansAdapter);

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
