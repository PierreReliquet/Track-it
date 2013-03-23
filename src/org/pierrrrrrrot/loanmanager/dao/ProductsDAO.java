package org.pierrrrrrrot.loanmanager.dao;

import java.util.ArrayList;
import java.util.List;

import org.pierrrrrrrot.loanmanager.model.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProductsDAO extends DBHelper {

    private static final String PRODUCTS_TABLE = "products";
    private static final String PRODUCTS_COLUMN_ID = "ID";
    private static final String PRODUCTS_COLUMN_NAME = "NAME";
    private static final String PRODUCTS_COLUMN_INFO = "INFO";

    private static final String PRODUCTS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS  "
            + PRODUCTS_TABLE
            + " ("
            + PRODUCTS_COLUMN_ID
            + " INTEGER PRIMARY KEY,"
            + PRODUCTS_COLUMN_NAME
            + " TEXT NOT NULL," + PRODUCTS_COLUMN_INFO + " TEXT" + ");";

    private static final String PRODUCTS_TABLE_DROP = " DROP TABLE "
            + PRODUCTS_TABLE + ";";

    private static volatile ProductsDAO instance = null;

    protected ProductsDAO(Context context) {
        super(context);
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

    public static ProductsDAO getInstance() {
        if (ProductsDAO.instance == null) {
            throw new RuntimeException("The init method should be called first");
        }
        return ProductsDAO.instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PRODUCTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PRODUCTS_TABLE_DROP);
        onCreate(db);
    }

    public void insertProduct(Product aProduct) {
        ContentValues values = new ContentValues();
        values.put(PRODUCTS_COLUMN_ID, aProduct.getBarcode());
        values.put(PRODUCTS_COLUMN_NAME, aProduct.getTitle());
        values.put(PRODUCTS_COLUMN_INFO, aProduct.getInfo());
        insertObject(PRODUCTS_TABLE, values);
    }

    public Product getProductByBarcode(String barcode) {
        Product p = null;
        Cursor c = getReadableDatabase().query(PRODUCTS_TABLE, null,
                PRODUCTS_COLUMN_ID + "=?", new String[] { barcode }, null,
                null, PRODUCTS_COLUMN_ID);
        if (c.getCount() > 0) {
            c.moveToFirst();
            p = createProductFromCursor(c);
        } else {
            c.close();
            getReadableDatabase().close();
            return (Product) NO_MATCHING;
        }
        c.close();
        getReadableDatabase().close();
        return p;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        Cursor product = getReadableDatabase().query(PRODUCTS_TABLE, null,
                null, null, null, null, PRODUCTS_COLUMN_ID);
        while (product.moveToNext()) {
            products.add(createProductFromCursor(product));
        }
        product.close();
        getReadableDatabase().close();
        return products;
    }

    private Product createProductFromCursor(Cursor c) {
        Product p = new Product();
        p.setBarcode(Long.parseLong(getStringFromColumn(c, PRODUCTS_COLUMN_ID)));
        p.setTitle(getStringFromColumn(c, PRODUCTS_COLUMN_NAME));
        p.setInfo(getStringFromColumn(c, PRODUCTS_COLUMN_INFO));
        return p;
    }

}
