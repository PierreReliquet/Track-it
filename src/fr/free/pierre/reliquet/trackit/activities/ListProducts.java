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
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.free.pierre.reliquet.trackit.R;
import fr.free.pierre.reliquet.trackit.dao.LoansDAO;
import fr.free.pierre.reliquet.trackit.dao.ProductsDAO;
import fr.free.pierre.reliquet.trackit.enums.CloseLoanFilter;
import fr.free.pierre.reliquet.trackit.model.Product;
import fr.free.pierre.reliquet.trackit.view.ProductAdapter;

import java.util.List;

public class ListProducts extends Activity {

    private ProductsDAO productsDAO;
    private ListView products;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list_products);
        // Show the Up button in the action bar.
        this.setupActionBar();

        this.productsDAO = ProductsDAO.getInstance();

        initializeProductList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeProductList();
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

    private void initializeProductList() {
        this.products = (ListView) this.findViewById(R.id.list_products_list);
        List<Product> productsList = this.productsDAO.getAllProducts();
        this.productAdapter = new ProductAdapter(this,
                R.layout.product_list_row_layout, productsList);
        this.products.setAdapter(this.productAdapter);
        this.products.setOnItemClickListener(new ProductSelecter());
    }

    private class ProductSelecter implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Product selected = ListProducts.this.productAdapter.getProducts()
                    .get(position);
            if (LoansDAO.getInstance().isCurrentLoanForProduct(selected)) {
                // let's check and decline if the product is already lent
                Intent closeLoan = new Intent(ListProducts.this,
                        CloseLoan.class);
                closeLoan.putExtra(CloseLoan.CLOSE_LOAN_FILTER_EXTRA, CloseLoanFilter.PRODUCT.name());
                closeLoan.putExtra(CloseLoan.PRODUCT_ID_EXTRA,
                        selected.getBarcode() + "");
                ListProducts.this.startActivity(closeLoan);
            } else {
                Intent addLoan = new Intent(ListProducts.this, AddLoan.class);
                addLoan.putExtra(AddLoan.BARCODE_EXTRA, selected.getBarcode()
                        + "");
                addLoan.putExtra(AddLoan.TITLE_EXTRA, selected.getTitle());
                addLoan.putExtra(AddLoan.INFO_EXTRA, selected.getInfo());
                ListProducts.this.startActivity(addLoan);
            }
        }
    }
}
