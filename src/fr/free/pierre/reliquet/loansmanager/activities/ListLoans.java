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
package fr.free.pierre.reliquet.loansmanager.activities;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import fr.free.pierre.reliquet.loansmanager.R;
import fr.free.pierre.reliquet.loansmanager.dao.LoansDAO;
import fr.free.pierre.reliquet.loansmanager.model.Loan;
import fr.free.pierre.reliquet.loansmanager.view.LoansAdapter;

public class ListLoans extends Activity {
    
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
        List<Loan> loansList = this.loansDAO.getAllLoans();
        LoansAdapter loansAdapter = new LoansAdapter(this,
                R.layout.loan_row_layout, loansList);
        this.loans.setAdapter(loansAdapter);
        
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
