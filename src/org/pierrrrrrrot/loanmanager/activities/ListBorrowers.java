package org.pierrrrrrrot.loanmanager.activities;

import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.dao.BorrowersDAO;
import org.pierrrrrrrot.loanmanager.model.Borrower;
import org.pierrrrrrrot.loanmanager.view.BorrowersAdapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListBorrowers extends Activity {

    private BorrowersDAO borrowersDAO;
    private BorrowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_borrowers);
        // Show the Up button in the action bar.
        setupActionBar();

        borrowersDAO = BorrowersDAO.getInstance();

        ListView borrowers = (ListView) findViewById(R.id.list_borrowers_list);
        List<Borrower> borrowersList = borrowersDAO.getAllBorrowers();
        adapter = new BorrowersAdapter(this, R.layout.borrower_list_row_layout,
                borrowersList);
        borrowers.setAdapter(adapter);
        borrowers.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                startCloseLoan(position);
            }

        });
    }

    private void startCloseLoan(int positionClicked) {
        Intent closeLoan = new Intent(this, CloseLoan.class);
        closeLoan.putExtra(CloseLoan.INTENT_CODE_CLOSE_LOAN_BORROWER_ID, ""
                + adapter.getBorrowers().get(positionClicked).getId());
        startActivity(closeLoan);
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
