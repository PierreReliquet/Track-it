package org.pierrrrrrrot.loanmanager.activities;

import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.dao.ProductsDAO;
import org.pierrrrrrrot.loanmanager.model.Product;
import org.pierrrrrrrot.loanmanager.view.ProductAdapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

public class ListProducts extends Activity {

    private ProductsDAO productsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        // Show the Up button in the action bar.
        setupActionBar();

        productsDAO = ProductsDAO.getInstance();

        ListView products = (ListView) findViewById(R.id.list_products_list);
        List<Product> productsList = productsDAO.getAllProducts();
        ProductAdapter adapter = new ProductAdapter(this,
                R.layout.product_list_row_layout, productsList);
        products.setAdapter(adapter);
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
