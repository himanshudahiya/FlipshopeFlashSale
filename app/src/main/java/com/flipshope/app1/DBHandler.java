package com.flipshope.app1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "products.db";
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCTNAME = "PRODUCTNAME";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_IMAGEURL = "IMAGEURL";
    public static final String COLUMN_PRODUCTPRICE = "PRODUCTPRICE";
    public static final String COLUMN_IS_ADDED = "IS_ADDED";
    public static final String COLUMN_PID = "PID";
    public static final String COLUMN_EMID = "EMID";
    public static final String COLUMN_SALE_DATE = "SALE_DATE";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_COOKIE = "COOKIE";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCTNAME + " TEXT,"
                + COLUMN_URL + " TEXT,"
                + COLUMN_IMAGEURL + "  TEXT,"
                + COLUMN_PRODUCTPRICE + " TEXT,"
                + COLUMN_IS_ADDED + " INTEGER DEFAULT 0,"
                + COLUMN_EMID + " TEXT,"
                + COLUMN_PID + " TEXT,"
                + COLUMN_SALE_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_COOKIE + " TEXT"
                + ");";

        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void insertTableProducts(ArrayList<Products2> productsList) {
        SQLiteDatabase db = getWritableDatabase();
        for (int i=0;i<productsList.size();i++){
            Products2 products2 = productsList.get(i);
            ContentValues initialValues = new ContentValues();
            initialValues.put(COLUMN_PRODUCTNAME,products2.getProductName());
            initialValues.put(COLUMN_URL,products2.getProductURL());
            initialValues.put(COLUMN_IMAGEURL, products2.getProductImageURL());
            initialValues.put(COLUMN_PRODUCTPRICE,products2.getProductPrice());
            initialValues.put(COLUMN_EMID, products2.getEmid());
            initialValues.put(COLUMN_PID, products2.getPid());
            initialValues.put(COLUMN_TIME, products2.getTime());
            initialValues.put(COLUMN_SALE_DATE, products2.getSaledate());
            initialValues.put(COLUMN_COOKIE, products2.getCookie());
            initialValues.put(COLUMN_IS_ADDED, products2.getIsAdded());
            db.insert(TABLE_PRODUCTS, null, initialValues);
            db.close();
        }

//        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_URL + " = \"" + products2.getProductURL() + "\";";
//
//        Cursor cursor = db.rawQuery(query, null);
//        if(cursor.getCount() <= 0){
//            cursor.close();
//            ContentValues initialValues = new ContentValues();
//            initialValues.put(COLUMN_PRODUCTNAME,products2.getProductName());
//            initialValues.put(COLUMN_URL,products2.getProductURL());
//            initialValues.put(COLUMN_IMAGEURL, products2.getProductImageURL());
//            initialValues.put(COLUMN_PRODUCTPRICE,products2.getProductPrice());
//            initialValues.put(COLUMN_EMID, products2.getEmid());
//            initialValues.put(COLUMN_PID, products2.getPid());
//            initialValues.put(COLUMN_TIME, products2.getTime());
//            initialValues.put(COLUMN_SALE_DATE, products2.getSaledate());
//            initialValues.put(COLUMN_COOKIE, products2.getCookie());
//            db.insert(TABLE_PRODUCTS, null, initialValues);
//        }
//        else{
//            query = "UPDATE " + TABLE_PRODUCTS + " SET "
//                    + COLUMN_IMAGEURL + " = \"" + products2.getProductImageURL() + "\", "
//                    + COLUMN_URL + " = \"" + products2.getProductURL() + "\", "
//                    + COLUMN_PRODUCTNAME + " = \"" + products2.getProductName() + "\", "
//                    + COLUMN_PRODUCTPRICE + " = \"" + products2.getProductPrice() + "\", "
//                    + COLUMN_TIME + " = \"" + products2.getTime() + "\", "
//                    + COLUMN_SALE_DATE + " = \"" + products2.getSaledate() + "\", "
//                    + COLUMN_PID + " = \"" + products2.getPid() + "\", "
//                    + COLUMN_EMID + " = \"" + products2.getEmid() + "\", "
//                    + COLUMN_COOKIE + " = \"" + products2.getCookie() + "\""
//                    + " WHERE " + COLUMN_URL + " = \"" + products2.getProductURL() + "\";";
//            db.execSQL(query);
//        }
//        cursor.close();
    }

    public ArrayList<Products2> returnProducts(String productName){
        ArrayList<Products2> dbstring = new ArrayList<Products2>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " LIKE '" + productName + "%'";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_URL))!=null){
                Products2 products2 = new Products2();
                products2.setProductURL(c.getString(c.getColumnIndex(COLUMN_URL)));
                products2.setPid(c.getString(c.getColumnIndex(COLUMN_PID)));
                products2.setProductPrice(c.getString(c.getColumnIndex(COLUMN_PRODUCTPRICE)));
                products2.setProductImageURL(c.getString(c.getColumnIndex(COLUMN_IMAGEURL)));
                products2.setProductName(c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)));
                products2.setSaledate(c.getString(c.getColumnIndex(COLUMN_SALE_DATE)));
                products2.setIsAdded(c.getInt(c.getColumnIndex(COLUMN_IS_ADDED)));
                products2.setTime(c.getString(c.getColumnIndex(COLUMN_TIME)));
                products2.setEmid(c.getString(c.getColumnIndex(COLUMN_EMID)));
                products2.setCookie(c.getString(c.getColumnIndex(COLUMN_COOKIE)));
                dbstring.add(products2);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public void updateProducts(ArrayList<Products2> products2){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db,DATABASE_VERSION,DATABASE_VERSION);
        for(int i=0;i<products2.size();i++) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COLUMN_PRODUCTNAME, products2.get(i).getProductName());
            initialValues.put(COLUMN_URL, products2.get(i).getProductURL());
            initialValues.put(COLUMN_IMAGEURL, products2.get(i).getProductImageURL());
            initialValues.put(COLUMN_PRODUCTPRICE, products2.get(i).getProductPrice());
            db.insert(TABLE_PRODUCTS, null, initialValues);
        }
    }

    public ArrayList<Products2> returnAddedProducts(){
        ArrayList<Products2> dbstring = new ArrayList<Products2>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_IS_ADDED + " = 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_URL))!=null){
                Products2 products2 = new Products2();
                products2.setProductURL(c.getString(c.getColumnIndex(COLUMN_URL)));
                products2.setProductPrice(c.getString(c.getColumnIndex(COLUMN_PRODUCTPRICE)));
                products2.setProductImageURL(c.getString(c.getColumnIndex(COLUMN_IMAGEURL)));
                products2.setProductName(c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)));
                products2.setPid(c.getString(c.getColumnIndex(COLUMN_PID)));
                products2.setEmid(c.getString(c.getColumnIndex(COLUMN_EMID)));
                products2.setTime(c.getString(c.getColumnIndex(COLUMN_TIME)));
                products2.setSaledate(c.getString(c.getColumnIndex(COLUMN_SALE_DATE)));
                products2.setCookie(c.getString(c.getColumnIndex(COLUMN_COOKIE)));
                products2.setIsAdded(c.getInt(c.getColumnIndex(COLUMN_IS_ADDED)));
                dbstring.add(products2);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public void addToCart(String url){
        String query = "UPDATE " + TABLE_PRODUCTS + " SET " + COLUMN_IS_ADDED + " = 1 WHERE " + COLUMN_URL + " = \"" + url + "\";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    public void removeFromCart(String url){
        String query = "UPDATE " + TABLE_PRODUCTS + " SET " + COLUMN_IS_ADDED + " = 0 WHERE " + COLUMN_URL + " = \"" + url + "\";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    public Boolean checkAdded(String url){
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_URL + " = \"" + url + "\" AND "+ COLUMN_IS_ADDED + " = 1;";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }cursor.close();
        db.close();
        return true;
    }



    public void onLogoutClearDB(){
        SQLiteDatabase db = getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_PRODUCTS, null, null);
        db.close();
    }


    public void delete(String Id)
    {
        SQLiteDatabase db = getWritableDatabase();
        try {

            db.delete(TABLE_PRODUCTS, COLUMN_PID + "= \""+Id + "\"", null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

}
