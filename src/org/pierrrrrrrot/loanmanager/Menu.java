package org.pierrrrrrrot.loanmanager;

import org.pierrrrrrrot.loanmanager.activities.AddLoan;
import org.pierrrrrrrot.loanmanager.activities.CloseLoan;
import org.pierrrrrrrot.loanmanager.activities.ListBorrowers;
import org.pierrrrrrrot.loanmanager.activities.ListLoans;
import org.pierrrrrrrot.loanmanager.activities.ListProducts;
import org.pierrrrrrrot.loanmanager.dao.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity {

    private static final String LOANS_MANAGER_MAIL_TITLE = "Loans Manager ";
    private static final String[] AUTHOR_EMAIL = { "pierre.reliquet@gmail.com" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_menu);
        // Initialization of the DBHelper
        Button addLoan = (Button) findViewById(R.id.menu_add_loan);
        addLoan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddLoan();
            }
        });

        Button closeLoan = (Button) findViewById(R.id.menu_close_loan);
        closeLoan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startCloseLoan();
            }
        });

        Button listBorrowers = (Button) findViewById(R.id.menu_list_borrowers);
        listBorrowers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startListBorrowers();
            }
        });

        Button listProducts = (Button) findViewById(R.id.menu_list_products);
        listProducts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startListProducts();
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

    private void init() {
        // Create dbs if does not exists
        DBHelper.globalInit(this);
    }

    private void startAddLoan() {
        Intent intent = new Intent(this, AddLoan.class);
        startActivity(intent);
    }

    private void startCloseLoan() {
        Intent intent = new Intent(this, CloseLoan.class);
        startActivity(intent);
    }

    private void startListBorrowers() {
        Intent intent = new Intent(this, ListBorrowers.class);
        startActivity(intent);
    }

    private void startListProducts() {
        Intent intent = new Intent(this, ListProducts.class);
        startActivity(intent);
    }

    private void startListLoans() {
        Intent intent = new Intent(this, ListLoans.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_about:
            try {
                showAbout();
                return true;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void showAbout() throws NameNotFoundException {
        PackageInfo pInfo = getPackageManager().getPackageInfo(
                getPackageName(), 0);
        final String version = pInfo.versionName;
        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_about_title)
                .setMessage(
                        String.format(Menu.this
                                .getString(R.string.menu_about_message),
                                version))
                .setNegativeButton(R.string.close,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                // Nothing to do just close the dialog
                            }
                        })
                .setPositiveButton(R.string.contact,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                Intent sendIntent = new Intent(
                                        Intent.ACTION_SEND);
                                sendIntent.setType("text/plain");
                                sendIntent.putExtra(Intent.EXTRA_EMAIL,
                                        AUTHOR_EMAIL);
                                sendIntent.putExtra(Intent.EXTRA_SUBJECT,
                                        LOANS_MANAGER_MAIL_TITLE + version);
                                startActivity(Intent.createChooser(sendIntent,
                                        "Email:"));
                            }
                        }).show();
    }
}
