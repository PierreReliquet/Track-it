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
package fr.free.pierre.reliquet.loansmanager.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.free.pierre.reliquet.loansmanager.R;
import fr.free.pierre.reliquet.loansmanager.model.Product;
import fr.free.pierre.reliquet.loansmanager.utils.Utils;

/**
 * @author Pierre Reliquet
 * 
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    
    private class ProductHolder {
        TextView barcode;
        TextView info;
        TextView title;
    }
    
    private final Context       context;
    private final int           layoutResourceId;
    
    private final List<Product> products;
    
    public ProductAdapter(Context context, int textViewResourceId,
            List<Product> objects) {
        super(context, textViewResourceId, objects);
        this.products = objects;
        this.context = context;
        this.layoutResourceId = textViewResourceId;
    }
    
    public List<Product> getProducts() {
        return this.products;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProductHolder holder = new ProductHolder();
        
        if (row == null) {
            LayoutInflater inflater = ((Activity) this.context)
                    .getLayoutInflater();
            row = inflater.inflate(this.layoutResourceId, parent, false);
        }
        
        holder.barcode = (TextView) row
                .findViewById(R.id.product_list_row_barcode);
        holder.title = (TextView) row.findViewById(R.id.product_list_row_title);
        holder.info = (TextView) row.findViewById(R.id.product_list_row_info);
        
        Product product = this.products.get(position);
        
        holder.barcode.setText(String.format(
                this.context.getString(R.string.product_barcode),
                product.getBarcode()));
        holder.title.setText(String.format(
                this.context.getString(R.string.product_title),
                product.getTitle()));
        if (!Utils.isNullOrEmpty(product.getInfo())) {
            holder.info.setText(String.format(
                    this.context.getString(R.string.product_additional_info),
                    product.getInfo()));
        } else {
            holder.info.setVisibility(View.GONE);
        }
        return row;
    }
    
}
