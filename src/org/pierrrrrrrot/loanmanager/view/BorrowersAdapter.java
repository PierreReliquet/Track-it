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
    
    class BorrowerHolder {
        TextView activeLoans;
        TextView average;
        TextView id;
        TextView name;
        TextView numberOfLoans;
    }
    
    private final List<Borrower> borrowers;
    private final Context        context;
    
    private final int            layoutResourceId;
    
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
        holder.activeLoans
                .setText(String.format(
                        this.context
                                .getString(R.string.borrower_number_of_active_loans),
                        LoansDAO.getInstance()
                                .getNumberOfNonTerminatedLoanByBorrower(
                                        borrower)));
        return row;
    }
    
}
