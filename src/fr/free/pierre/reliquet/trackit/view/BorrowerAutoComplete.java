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
import fr.free.pierre.reliquet.trackit.model.Borrower;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BorrowerAutoComplete extends ArrayAdapter<Borrower> {

    class BorrowerHolder {
        TextView name;
    }

    private final List<Borrower> borrowers;
    private final Context context;
    private final int layoutResourceId;

    Filter nameFilter = new BorrowerAutoCompleteFilter();

    private final List<Borrower> suggestions = new ArrayList<Borrower>();

    public BorrowerAutoComplete(Context context, int textViewResourceId,
                                List<Borrower> objects) {
        super(context, textViewResourceId, objects);
        this.borrowers = objects;
        this.context = context;
        this.layoutResourceId = textViewResourceId;
    }

    public List<Borrower> getBorrowers() {
        return this.borrowers;
    }

    @Override
    public Filter getFilter() {
        return this.nameFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BorrowerHolder holder = new BorrowerHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) this.context)
                    .getLayoutInflater();
            row = inflater.inflate(this.layoutResourceId, parent, false);
        }

        holder.name = (TextView) row.findViewById(R.id.autocomplete_row_name);

        Borrower borrower = this.borrowers.get(position);

        holder.name.setText(borrower.getName());
        return row;
    }

    private class BorrowerAutoCompleteFilter extends Filter {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Borrower) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                BorrowerAutoComplete.this.suggestions.clear();
                for (Borrower b : BorrowerAutoComplete.this.borrowers) {
                    if (b.getName()
                            .toLowerCase(Locale.getDefault())
                            .contains(
                                    constraint.toString().toLowerCase(
                                            Locale.getDefault()))) {
                        BorrowerAutoComplete.this.suggestions.add(b);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = BorrowerAutoComplete.this.suggestions;
                filterResults.count = BorrowerAutoComplete.this.suggestions
                        .size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            ArrayList<Borrower> filteredList = (ArrayList<Borrower>) results.values;
            if ((results != null) && (results.count > 0)) {
                BorrowerAutoComplete.this.clear();
                for (Borrower c : filteredList) {
                    BorrowerAutoComplete.this.add(c);
                }
                BorrowerAutoComplete.this.notifyDataSetChanged();
            }
        }
    }

}
