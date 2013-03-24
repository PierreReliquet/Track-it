/**
 * Copyright 2013 Pierre Reliquet©
 * 
 * Track-it is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Track-it is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Track-it. If not, see <http://www.gnu.org/licenses/>
 */
package fr.free.pierre.reliquet.trackit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import fr.free.pierre.reliquet.trackit.activities.AddLoan;
import fr.free.pierre.reliquet.trackit.activities.CloseLoan;
import fr.free.pierre.reliquet.trackit.activities.ListBorrowers;
import fr.free.pierre.reliquet.trackit.activities.ListLoans;
import fr.free.pierre.reliquet.trackit.activities.ListProducts;
import fr.free.pierre.reliquet.trackit.dao.DBHelper;

public class Menu extends Activity {
    
    private static final String[] AUTHOR_EMAIL             = { "pierre.reliquet@gmail.com" };
    private static final String   LOANS_MANAGER_MAIL_TITLE = "Track-it ";
    
    private void init() {
        // Create dbs if does not exists
        DBHelper.globalInit(this);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
        this.setContentView(R.layout.activity_menu);
        // Initialization of the DBHelper
        Button addLoan = (Button) this.findViewById(R.id.menu_add_loan);
        addLoan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu.this.startAddLoan();
            }
        });
        
        Button closeLoan = (Button) this.findViewById(R.id.menu_close_loan);
        closeLoan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu.this.startCloseLoan();
            }
        });
        
        Button listBorrowers = (Button) this
                .findViewById(R.id.menu_list_borrowers);
        listBorrowers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu.this.startListBorrowers();
            }
        });
        
        Button listProducts = (Button) this
                .findViewById(R.id.menu_list_products);
        listProducts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu.this.startListProducts();
            }
        });
        
        Button listLoans = (Button) this.findViewById(R.id.menu_list_loans);
        listLoans.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu.this.startListLoans();
            }
        });
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                try {
                    this.showAbout();
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
        PackageInfo pInfo = this.getPackageManager().getPackageInfo(
                this.getPackageName(), 0);
        final String version = pInfo.versionName;
        final SpannableString message = new SpannableString(String.format(
                Menu.this.getString(R.string.menu_about_message), version));
        Linkify.addLinks(message, Linkify.WEB_URLS);
        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_about_title)
                .setMessage(message)
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
                                Menu.this.startActivity(Intent.createChooser(
                                        sendIntent, "Email:"));
                            }
                        }).show();
    }
    
    private void startAddLoan() {
        Intent intent = new Intent(this, AddLoan.class);
        this.startActivity(intent);
    }
    
    private void startCloseLoan() {
        Intent intent = new Intent(this, CloseLoan.class);
        this.startActivity(intent);
    }
    
    private void startListBorrowers() {
        Intent intent = new Intent(this, ListBorrowers.class);
        this.startActivity(intent);
    }
    
    private void startListLoans() {
        Intent intent = new Intent(this, ListLoans.class);
        this.startActivity(intent);
    }
    
    private void startListProducts() {
        Intent intent = new Intent(this, ListProducts.class);
        this.startActivity(intent);
    }
}
