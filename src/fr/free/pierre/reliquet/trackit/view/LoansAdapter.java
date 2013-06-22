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
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.free.pierre.reliquet.trackit.R;
import fr.free.pierre.reliquet.trackit.model.Loan;

import java.util.List;

/**
 * @author Pierre Reliquet
 */
public class LoansAdapter extends ArrayAdapter<Loan> {

    private class LoanHolder {
        TextView borrower;
        TextView endDate;
        TextView product;
        TextView startDate;
        ImageView actionButton;
    }

    private final Context context;
    private final int layoutResourceId;

    private List<Loan> loans;

    public LoansAdapter(Context context, int textViewResourceId,
                        List<Loan> objects) {
        super(context, textViewResourceId, objects);
        this.loans = objects;
        this.context = context;
        this.layoutResourceId = textViewResourceId;
    }

    public List<Loan> getLoans() {
        return this.loans;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LoanHolder holder = new LoanHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) this.context)
                    .getLayoutInflater();
            row = inflater.inflate(this.layoutResourceId, parent, false);
        }

        holder.borrower = (TextView) row
                .findViewById(R.id.loan_row_borrower_name);
        holder.product = (TextView) row
                .findViewById(R.id.loan_row_product_name);
        holder.startDate = (TextView) row
                .findViewById(R.id.loan_row_start_date);
        holder.endDate = (TextView) row.findViewById(R.id.loan_row_end_date);
        holder.actionButton = (ImageView) row.findViewById(R.id.loan_row_action_button);

        Loan loan = this.loans.get(position);

        holder.borrower.setText(this.context
                .getString(R.string.borrower_display)
                + loan.getBorrower().getName());
        holder.product.setText(this.context.getString(R.string.product_display)
                + loan.getProduct().getTitle());
        holder.startDate.setText(this.context
                .getString(R.string.loan_start_date)
                + DateFormat.getDateFormat(this.getContext()).format(
                loan.getDate()));
        if (loan.getEndDate().getTime() != 0) {
            holder.endDate.setText(this.context
                    .getString(R.string.loan_end_date)
                    + DateFormat.getDateFormat(this.getContext()).format(
                    loan.getEndDate()));
            holder.actionButton.setVisibility(View.INVISIBLE);
        } else {
            holder.endDate.setText(this.context
                    .getString(R.string.loan_end_date)
                    + this.context.getString(R.string.still_active));
            holder.actionButton.setVisibility(View.VISIBLE);
        }

        return row;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

}
