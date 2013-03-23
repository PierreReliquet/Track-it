/**
 * 
 */
package org.pierrrrrrrot.loanmanager.view;

import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.dao.LoansDAO;
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
        holder.numberOfLoans = (TextView) row
                .findViewById(R.id.borrower_list_row_number_loans);
        holder.activeLoans = (TextView) row
                .findViewById(R.id.borrower_list_row_active_number_loans);

        Borrower borrower = borrowers.get(position);

        holder.id.setText(String.format(
                context.getString(R.string.borrower_id), borrower.getId()));
        holder.name.setText(String.format(
                context.getString(R.string.borrower_name_adapter),
                borrower.getName()));
        holder.average.setText(String.format(
                context.getString(R.string.borrower_average_time),
                borrower.getAverageTime()));
        holder.numberOfLoans.setText(String.format(
                context.getString(R.string.borrower_number_of_loans),
                borrower.getNumberOfLoans()));
        holder.activeLoans.setText(String.format(
                context.getString(R.string.borrower_number_of_active_loans),
                LoansDAO.getInstance().getNumberOfNonTerminatedLoanByBorrower(
                        borrower)));
        return row;
    }

    public List<Borrower> getBorrowers() {
        return borrowers;
    }

    class BorrowerHolder {
        TextView id;
        TextView name;
        TextView average;
        TextView numberOfLoans;
        TextView activeLoans;
    }

}
