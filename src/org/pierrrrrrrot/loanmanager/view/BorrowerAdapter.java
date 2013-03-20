package org.pierrrrrrrot.loanmanager.view;

import java.util.ArrayList;
import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.model.Borrower;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class BorrowerAdapter extends ArrayAdapter<Borrower> {

    private final List<Borrower> borrowers;
    private final List<Borrower> suggestions = new ArrayList<Borrower>();
    private final Context context;
    private final int layoutResourceId;

    public BorrowerAdapter(Context context, int textViewResourceId,
            List<Borrower> objects) {
        super(context, textViewResourceId, objects);
        borrowers = objects;
        this.context = context;
        this.layoutResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BorrowerHolder holder = new BorrowerHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        holder.name = (TextView) row
                .findViewById(R.id.borrower_row_borrower_name);

        Borrower borrower = borrowers.get(position);

        holder.name.setText(borrower.getName());
        return row;
    }

    public List<Borrower> getBorrowers() {
        return borrowers;
    }

    class BorrowerHolder {
        TextView name;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Borrower) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Borrower b : borrowers) {
                    if (b.getName().toLowerCase()
                            .startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(b);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint,
                FilterResults results) {
            ArrayList<Borrower> filteredList = (ArrayList<Borrower>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Borrower c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}
