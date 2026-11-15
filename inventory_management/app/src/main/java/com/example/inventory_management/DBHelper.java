package com.example.inventory_management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper {
    // Database Name and Version
    public static final String DBName = "InventoryManager.db";
    public static final int DB_VERSION = 1;

    // Table Names
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_VENDORS = "vendors";

    public DBHelper(Context context) {
        super(context, DBName, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Products Table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "price REAL, " +
                "quantity INTEGER, " +
                "vendor TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Create Vendors Table

        String CREATE_VENDORS_TABLE = "CREATE TABLE " + TABLE_VENDORS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "phone TEXT, " +
                "address TEXT)";
        db.execSQL(CREATE_VENDORS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDORS);
        onCreate(db);
    }

    //  PRODUCT METHODS


    public long insertProduct(String name, double price, int quantity, String vendor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("quantity", quantity);
        cv.put("vendor", vendor);
        return db.insert(TABLE_PRODUCTS, null, cv);
    }

    // Get All Products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    // Update Product
    public boolean updateProduct(int id, String name, double price, int quantity, String vendor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("quantity", quantity);
        cv.put("vendor", vendor);
        int result = db.update(TABLE_PRODUCTS, cv, "id = ?", new String[]{String.valueOf(id)});
        return result > 0; // Returns true if updated successfully
    }

    // Delete Product
    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // vendor methods

    // Insert Vendor
    public long insertVendor(String name, String phone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("phone", phone);
        cv.put("address", address);
        return db.insert(TABLE_VENDORS, null, cv);
    }

    // Get All Vendors
    public Cursor getAllVendors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_VENDORS, null);
    }

    // Delete Vendor

    public boolean deleteVendor(int id, String vendorName) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Check if products are linked to this vendor
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE vendor = ?", new String[]{vendorName});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Stop deletion if products exist
        }
        cursor.close();

        // Safe to delete
        int result = db.delete(TABLE_VENDORS, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
    // Update Vendor
    public boolean updateVendor(int id, String name, String phone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("phone", phone);
        cv.put("address", address);
        int result = db.update(TABLE_VENDORS, cv, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Get product names or details for Sale Spinner
    public Cursor getAllProductsForSale() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, name, price, quantity FROM " + TABLE_PRODUCTS, null);
    }

    // Reduce stock quantity after a sale
    public boolean updateProductStock(int productId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("quantity", newQuantity);
        int result = db.update(TABLE_PRODUCTS, cv, "id = ?", new String[]{String.valueOf(productId)});
        return result > 0;
    }
}