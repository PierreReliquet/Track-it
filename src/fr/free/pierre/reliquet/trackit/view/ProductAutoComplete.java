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
package fr.free.pierre.reliquet.trackit.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import fr.free.pierre.reliquet.trackit.R;
import fr.free.pierre.reliquet.trackit.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAutoComplete extends ArrayAdapter<Product> {

    class ProductHolder {
        TextView name;
    }

    private final List<Product> products;
    private final Context context;
    private final int layoutResourceId;

    Filter nameFilter = new ProductAutoCompleteFilter();

    private final List<Product> suggestions = new ArrayList<Product>();

    public ProductAutoComplete(Context context, int textViewResourceId,
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
    public Filter getFilter() {
        return this.nameFilter;
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

        holder.name = (TextView) row.findViewById(R.id.autocomplete_row_name);

        Product product = this.products.get(position);

        holder.name.setText(product.getTitle());
        return row;
    }

    private class ProductAutoCompleteFilter extends Filter {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Product) (resultValue)).getTitle();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                ProductAutoComplete.this.suggestions.clear();
                for (Product p : ProductAutoComplete.this.products) {
                    if (p.getTitle()
                            .toLowerCase(Locale.getDefault())
                            .contains(
                                    constraint.toString().toLowerCase(
                                            Locale.getDefault()))) {
                        ProductAutoComplete.this.suggestions.add(p);
                    }
                }
                filterResults.values = ProductAutoComplete.this.suggestions;
                filterResults.count = ProductAutoComplete.this.suggestions
                        .size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            ArrayList<Product> filteredList = (ArrayList<Product>) results.values;
            if ((results != null) && (results.count > 0)) {
                ProductAutoComplete.this.clear();
                for (Product c : filteredList) {
                    ProductAutoComplete.this.add(c);
                }
                ProductAutoComplete.this.notifyDataSetChanged();
            }
        }
    }

}
