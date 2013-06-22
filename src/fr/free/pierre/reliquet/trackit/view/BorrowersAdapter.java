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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.free.pierre.reliquet.trackit.R;
import fr.free.pierre.reliquet.trackit.activities.CloseLoan;
import fr.free.pierre.reliquet.trackit.dao.LoansDAO;
import fr.free.pierre.reliquet.trackit.enums.CloseLoanFilter;
import fr.free.pierre.reliquet.trackit.model.Borrower;

import java.util.List;

/**
 * @author Pierre Reliquet
 */
public class BorrowersAdapter extends ArrayAdapter<Borrower> {

    private class BorrowerHolder {
        TextView activeLoans;
        TextView average;
        TextView id;
        TextView name;
        TextView numberOfLoans;
        ImageView actionButton;
    }

    private final List<Borrower> borrowers;
    private final Context context;

    private final int layoutResourceId;

    public BorrowersAdapter(Context context, int textViewResourceId,
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BorrowerHolder holder = new BorrowerHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) this.context)
                    .getLayoutInflater();
            row = inflater.inflate(this.layoutResourceId, parent, false);
        }

        holder.id = (TextView) row.findViewById(R.id.borrower_list_row_id);
        holder.name = (TextView) row.findViewById(R.id.borrower_list_row_name);
        holder.average = (TextView) row
                .findViewById(R.id.borrower_list_row_average_time);
        holder.numberOfLoans = (TextView) row
                .findViewById(R.id.borrower_list_row_number_loans);
        holder.activeLoans = (TextView) row
                .findViewById(R.id.borrower_list_row_active_number_loans);
        holder.actionButton = (ImageView) row.findViewById(R.id.borrower_list_row_action_button);

        Borrower borrower = this.borrowers.get(position);

        holder.id
                .setText(String.format(
                        this.context.getString(R.string.borrower_id),
                        borrower.getId()));
        holder.name.setText(String.format(
                this.context.getString(R.string.borrower_name_adapter),
                borrower.getName()));
        holder.average.setText(String.format(
                this.context.getString(R.string.borrower_average_time),
                borrower.getAverageTime()));
        holder.numberOfLoans.setText(String.format(
                this.context.getString(R.string.borrower_number_of_loans),
                borrower.getNumberOfLoans()));
        int numberOfNonTerminatedLoans = LoansDAO.getInstance()
                .getNumberOfNonTerminatedLoanByBorrower(
                        borrower);
        holder.activeLoans
                .setText(String.format(
                        this.context
                                .getString(R.string.borrower_number_of_active_loans),
                        numberOfNonTerminatedLoans));

        // If there is no current loan the action button should be disabled
        if (numberOfNonTerminatedLoans == 0) {
            holder.actionButton.setVisibility(View.INVISIBLE);
        } else {
            holder.actionButton.setOnClickListener(new InternalClickListener(borrower.getId()));
        }
        return row;
    }

    /**
     * The private onClick listener to manage the click
     */
    private class InternalClickListener implements View.OnClickListener {

        private final int borrowerId;

        public InternalClickListener(int borrowerId) {
            this.borrowerId = borrowerId;
        }

        @Override
        public void onClick(View v) {
            Intent closeLoan = new Intent(context, CloseLoan.class);
            closeLoan.putExtra(CloseLoan.CLOSE_LOAN_FILTER_EXTRA, CloseLoanFilter.BORROWER.name());
            closeLoan.putExtra(CloseLoan.BORROWER_ID_EXTRA, "" + borrowerId);
            context.startActivity(closeLoan);
        }
    }

}
