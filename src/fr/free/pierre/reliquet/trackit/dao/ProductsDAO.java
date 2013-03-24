/**
 * Copyright 2013 Pierre Reliquet©
 * 
 * Track-it is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Track-it is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Track-it. If not, see <http://www.gnu.org/licenses/>
 */
package fr.free.pierre.reliquet.trackit.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.free.pierre.reliquet.trackit.model.Product;

public class ProductsDAO extends DBHelper {
    
    private static volatile ProductsDAO instance              = null;
    private static final String         PRODUCTS_COLUMN_ID    = "ID";
    private static final String         PRODUCTS_COLUMN_INFO  = "INFO";
    private static final String         PRODUCTS_COLUMN_NAME  = "NAME";
    
    private static final String         PRODUCTS_TABLE        = "products";
    
    private static final String         PRODUCTS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS  "
                                                                      + PRODUCTS_TABLE
                                                                      + " ("
                                                                      + PRODUCTS_COLUMN_ID
                                                                      + " INTEGER PRIMARY KEY,"
                                                                      + PRODUCTS_COLUMN_NAME
                                                                      + " TEXT NOT NULL,"
                                                                      + PRODUCTS_COLUMN_INFO
                                                                      + " TEXT"
                                                                      + ");";
    
    private static final String         PRODUCTS_TABLE_DROP   = " DROP TABLE "
                                                                      + PRODUCTS_TABLE
                                                                      + ";";
    
    public static ProductsDAO getInstance() {
        if (ProductsDAO.instance == null) {
            throw new RuntimeException("The init method should be called first");
        }
        return ProductsDAO.instance;
    }
    
    public static void init(Context context) {
        if (ProductsDAO.instance == null) {
            synchronized (ProductsDAO.class) {
                if (ProductsDAO.instance == null) {
                    ProductsDAO.instance = new ProductsDAO(context);
                }
            }
        }
    }
    
    protected ProductsDAO(Context context) {
        super(context);
    }
    
    private Product createProductFromCursor(Cursor c) {
        Product p = new Product();
        p.setBarcode(Long.parseLong(this.getStringFromColumn(c,
                PRODUCTS_COLUMN_ID)));
        p.setTitle(this.getStringFromColumn(c, PRODUCTS_COLUMN_NAME));
        p.setInfo(this.getStringFromColumn(c, PRODUCTS_COLUMN_INFO));
        return p;
    }
    
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        Cursor product = this.getReadableDatabase().query(PRODUCTS_TABLE, null,
                null, null, null, null, PRODUCTS_COLUMN_ID);
        while (product.moveToNext()) {
            products.add(this.createProductFromCursor(product));
        }
        product.close();
        this.getReadableDatabase().close();
        return products;
    }
    
    public Product getProductByBarcode(String barcode) {
        Product p = null;
        Cursor c = this.getReadableDatabase().query(PRODUCTS_TABLE, null,
                PRODUCTS_COLUMN_ID + "=?", new String[] { barcode }, null,
                null, PRODUCTS_COLUMN_ID);
        if (c.getCount() > 0) {
            c.moveToFirst();
            p = this.createProductFromCursor(c);
        } else {
            c.close();
            this.getReadableDatabase().close();
            return (Product) NO_MATCHING;
        }
        c.close();
        this.getReadableDatabase().close();
        return p;
    }
    
    public void insertProduct(Product aProduct) {
        ContentValues values = new ContentValues();
        values.put(PRODUCTS_COLUMN_ID, aProduct.getBarcode());
        values.put(PRODUCTS_COLUMN_NAME, aProduct.getTitle());
        values.put(PRODUCTS_COLUMN_INFO, aProduct.getInfo());
        this.insertObject(PRODUCTS_TABLE, values);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PRODUCTS_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PRODUCTS_TABLE_DROP);
        this.onCreate(db);
    }
    
}
