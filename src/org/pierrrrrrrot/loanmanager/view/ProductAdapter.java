/**
 * 
 */
package org.pierrrrrrrot.loanmanager.view;

import java.util.List;

import org.pierrrrrrrot.loanmanager.R;
import org.pierrrrrrrot.loanmanager.model.Product;
import org.pierrrrrrrot.loanmanager.utils.Utils;

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
public class ProductAdapter extends ArrayAdapter<Product> {

    private final List<Product> products;
    private final Context context;
    private final int layoutResourceId;

    public ProductAdapter(Context context, int textViewResourceId,
            List<Product> objects) {
        super(context, textViewResourceId, objects);
        products = objects;
        this.context = context;
        this.layoutResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProductHolder holder = new ProductHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        holder.barcode = (TextView) row
                .findViewById(R.id.product_list_row_barcode);
        holder.title = (TextView) row.findViewById(R.id.product_list_row_title);
        holder.info = (TextView) row.findViewById(R.id.product_list_row_info);

        Product product = products.get(position);

        holder.barcode.setText(String.format(
                context.getString(R.string.product_barcode),
                product.getBarcode()));
        holder.title.setText(String.format(
                context.getString(R.string.product_title), product.getTitle()));
        if (!Utils.isNullOrEmpty(product.getInfo())) {
            holder.info.setText(String.format(
                    context.getString(R.string.product_additional_info),
                    product.getInfo()));
        } else {
            holder.info.setVisibility(View.GONE);
        }
        return row;
    }

    public List<Product> getProducts() {
        return products;
    }

    class ProductHolder {
        TextView barcode;
        TextView title;
        TextView info;
    }

}
