/**
 * 
 */
package org.pierrrrrrrot.loanmanager.view;

import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.model.Borrower;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Pierre Reliquet
 * 
 */
public class BorrowersAdapter extends ArrayAdapter<Borrower> {

    private final List<Borrower> borrowers;
    private final Context context;
    private final int layoutResourceId;

    public BorrowersAdapter(Context context, int textViewResourceId,
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

        holder.id = (TextView) row.findViewById(R.id.borrower_list_row_id);
        holder.name = (TextView) row.findViewById(R.id.borrower_list_row_name);
        holder.average = (TextView) row
                .findViewById(R.id.borrower_list_row_average_time);
        holder.average = (TextView) row
                .findViewById(R.id.borrower_list_row_number_loans);

        Borrower borrower = borrowers.get(position);

        holder.id.setText("" + borrower.getId());
        holder.name.setText(borrower.getName());
        // TODO 03-20 21:20:41.273: E/AndroidRuntime(2694):
        // android.content.res.Resources$NotFoundException: String resource ID
        // #0x0
        holder.average.setText("" + borrower.getAverageTime());
        holder.numberOfLoans.setText("" + borrower.getLoansAmount());
        return row;
    }

    public List<Borrower> getLoans() {
        return borrowers;
    }

    class BorrowerHolder {
        TextView id;
        TextView name;
        TextView average;
        TextView numberOfLoans;
    }

}
