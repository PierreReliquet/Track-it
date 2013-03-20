/**
 * 
 */
package org.pierrrrrrrot.loanmanager.view;

import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.model.Loan;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Pierre Reliquet
 * 
 */
public class LoansAdapter extends ArrayAdapter<Loan> {

    private final List<Loan> loans;
    private final Context context;
    private final int layoutResourceId;

    public LoansAdapter(Context context, int textViewResourceId,
            List<Loan> objects) {
        super(context, textViewResourceId, objects);
        loans = objects;
        this.context = context;
        this.layoutResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LoanHolder holder = new LoanHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        holder.borrower = (TextView) row
                .findViewById(R.id.loan_row_borrower_name);
        holder.product = (TextView) row
                .findViewById(R.id.loan_row_product_name);
        holder.startDate = (TextView) row
                .findViewById(R.id.loan_row_start_date);

        Loan loan = loans.get(position);

        holder.borrower.setText(context.getString(R.string.borrower_display)
                + loan.getBorrower().getName());
        holder.product.setText(context.getString(R.string.product_display)
                + loan.getProduct().getTitle());
        holder.startDate
                .setText(context.getString(R.string.loan_start_date)
                        + DateFormat.getDateFormat(getContext()).format(
                                loan.getDate()));
        return row;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    class LoanHolder {
        TextView borrower;
        TextView product;
        TextView startDate;
    }

}
