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

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.free.pierre.reliquet.trackit.R;
import fr.free.pierre.reliquet.trackit.dao.BorrowersDAO;
import fr.free.pierre.reliquet.trackit.enums.CloseLoanFilter;
import fr.free.pierre.reliquet.trackit.model.Borrower;
import fr.free.pierre.reliquet.trackit.view.BorrowersAdapter;

public class ListBorrowers extends Activity {
    
    private BorrowersAdapter adapter;
    private BorrowersDAO     borrowersDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list_borrowers);
        // Show the Up button in the action bar.
        this.setupActionBar();
        
        this.borrowersDAO = BorrowersDAO.getInstance();
        
        ListView borrowers = (ListView) this
                .findViewById(R.id.list_borrowers_list);
        List<Borrower> borrowersList = this.borrowersDAO.getAllBorrowers();
        this.adapter = new BorrowersAdapter(this,
                R.layout.borrower_list_row_layout, borrowersList);
        borrowers.setAdapter(this.adapter);
        borrowers.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                ListBorrowers.this.startCloseLoan(position);
            }
            
        });
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
    
    private void startCloseLoan(int positionClicked) {
        Intent closeLoan = new Intent(this, CloseLoan.class);
        closeLoan.putExtra(CloseLoan.CLOSE_LOAN_FILTER_EXTRA, CloseLoanFilter.BORROWER.name());
        closeLoan.putExtra(CloseLoan.BORROWER_ID_EXTRA, ""
                + this.adapter.getBorrowers().get(positionClicked).getId());
        this.startActivity(closeLoan);
    }
    
}
