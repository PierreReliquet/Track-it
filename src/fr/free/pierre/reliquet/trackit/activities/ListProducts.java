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
package fr.free.pierre.reliquet.trackit.activities;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import fr.free.pierre.reliquet.trackit.R;
import fr.free.pierre.reliquet.trackit.dao.ProductsDAO;
import fr.free.pierre.reliquet.trackit.model.Product;
import fr.free.pierre.reliquet.trackit.view.ProductAdapter;

public class ListProducts extends Activity {
    
    private ProductsDAO productsDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list_products);
        // Show the Up button in the action bar.
        this.setupActionBar();
        
        this.productsDAO = ProductsDAO.getInstance();
        
        ListView products = (ListView) this
                .findViewById(R.id.list_products_list);
        List<Product> productsList = this.productsDAO.getAllProducts();
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
            this.getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
