package com.mayur.shortmessage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mayur.shortmessage.data.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db_short_message";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contact_table_";
    private static final String TABLE_NUMBERS = "numbers_table_";

    // Contacts Table Columns names TABLE_CONTACTS;
    private static final String _ID = "_id";
    private static final String DISPLAY_NAME = "display_name";
    private static final String PHOTO_URI = "photo_url";

    // Contacts Table Columns names TABLE_NUMBERS;
    private static final String PHONE_NUMBER = "phone_number";
    private static final String C_ID = "_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        Log.d("DB", "Constructor");
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "onCreate");
        createTableContacts(db);
        createTableNumbers(db);
    }

    private void createTableContacts(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + _ID + " TEXT PRIMARY KEY,"
                + DISPLAY_NAME + " TEXT,"
                + PHOTO_URI + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    private void createTableNumbers(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NUMBERS + "("
                + PHONE_NUMBER + " TEXT PRIMARY KEY,"
                + C_ID + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS);
        // Create tables again

    }

    public void truncateDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


    // Adding One New contact
    public void addListContact(List<Contact> modelList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < modelList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(_ID, modelList.get(i).id);
            values.put(DISPLAY_NAME, modelList.get(i).name);
            values.put(PHOTO_URI, modelList.get(i).photoUri);
            db.insert(TABLE_CONTACTS, null, values);
            addPhoneNumber(db, modelList.get(i).id + "", modelList.get(i).allPhoneNumber);
        }
        db.close(); // Closing database connection
    }

    // Adding new location by Category
    public Contact findContactByNumber(String number){
        SQLiteDatabase db = this.getReadableDatabase();

        // prevent for prefix number
        String number2 = number;
        String number3 = "+" + number;
        String number4 = number;
        if(number.length()>=3){
            number4 = "0" + number.substring(2);
            if(number.contains("+")){
                number2 = "0" + number.substring(3);
            }
        }

        Log.d("db", number2);

        Cursor cur;
        cur = db.rawQuery(
                "SELECT DISTINCT c.* FROM "
                + TABLE_CONTACTS + " c,"
                + TABLE_NUMBERS + " n "
                + "WHERE c." + _ID + " = n." + C_ID
                + " AND ( n." + PHONE_NUMBER + " = ? OR"
                        +" n." + PHONE_NUMBER + " = ? OR"
                        +" n." + PHONE_NUMBER + " = ? OR"
                        +" n." + PHONE_NUMBER + " = ? )", new String[]{number, number2, number3, number4 });
        List<Contact> ctxs = getContactObjct(db, cur, number);
        Contact c ;
        if(ctxs.size()>0){
            c = ctxs.get(0);
        }else{
            c = new Contact(-1, null, number);
        }

        db.close();
        return c;
    }

    public List<Contact> getAllContacts(){
        List<Contact> itemlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS +" ORDER BY "+DISPLAY_NAME+" ASC", null);
        itemlist = getContactObjct(db, cur, "");
        return itemlist ;
    }

    public Contact getDetailsContact(Contact c){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS + " WHERE " + C_ID + " = ?", new String[]{c.id + ""});
        Contact contact = c;
        contact.allPhoneNumber = getAllPhoneNumber(db, c.id+"");
        db.close();
        return contact;
    }


    private List<Contact> getContactObjct(SQLiteDatabase db, Cursor cur, String number){
        List<Contact> itemlist = new ArrayList<>();
        // looping through all rows and adding to list
        if (cur.moveToFirst()) {
            do {
                Contact c = new Contact();
                c.id    = cur.getLong(0);
                c.name  = cur.getString(1);
                try {
                    c.photoUri  = cur.getString(2).trim();
                } catch (Exception e) {
                    c.photoUri  = "";
                }
                c.number = number.equals("") ? null : number;
                // Adding contact to list
                itemlist.add(c);
            } while (cur.moveToNext());
        }
        return itemlist;
    }


    private void addPhoneNumber(SQLiteDatabase db, String id, List<String> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            if(!isNumberExist(db, numbers.get(i))){
                ContentValues values = new ContentValues();
                // prevent for prefix number
                String number = numbers.get(i);
                if(numbers.get(i).contains("+") && numbers.get(i).length()>=3){
                    number = "0" + numbers.get(i).substring(3);
                }
                values.put(PHONE_NUMBER, number);
                values.put(C_ID, id);
                // Inserting Row
//                db.insert(TABLE_NUMBERS, null, values);
            }
        }
    }

    private List<String> getAllPhoneNumber(SQLiteDatabase db, String id) {
        List<String> numbers = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NUMBERS + " WHERE " + C_ID + " = ?", new String[]{id});
        if (cur.moveToFirst()) {
            do {
                numbers.add(cur.getString(0));
            } while (cur.moveToNext());
        }
        return numbers;
    }


    public boolean isNumberExist(SQLiteDatabase db, String number) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NUMBERS + " WHERE " + PHONE_NUMBER + " = ?", new String[]{number});
        int count = cursor.getCount();
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    public boolean isDatabaseEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        int count = cursor.getCount();
        if(count<=0){
            return true;
        }else{
            return false;
        }
    }

}
