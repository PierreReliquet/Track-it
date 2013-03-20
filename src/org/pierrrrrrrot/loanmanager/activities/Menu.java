package org.pierrrrrrrot.loanmanager.activities;

import org.pierrrrrrrot.loanmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Initialization of the DBHelper
        Button addLoan = (Button) findViewById(R.id.menu_add_loan);
        addLoan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddLoan();
            }
        });

        Button listBorrowers = (Button) findViewById(R.id.menu_list_borrowers);
        listBorrowers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startListBorrowers();
            }
        });

        Button listLoans = (Button) findViewById(R.id.menu_list_loans);
        listLoans.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startListLoans();
            }
        });

    }

    private void startAddLoan() {
        Intent intent = new Intent(this, AddLoan.class);
        startActivity(intent);
    }

    private void startListBorrowers() {
        Intent intent = new Intent(this, ListBorrowers.class);
        startActivity(intent);
    }

    private void startListLoans() {
        Intent intent = new Intent(this, ListLoans.class);
        startActivity(intent);
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.menu, menu);
    // return true;
    // }

}
