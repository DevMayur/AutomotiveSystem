package com.mayur.shortmessage.data.store;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.mayur.shortmessage.data.DatabaseHandler;
import com.mayur.shortmessage.data.model.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactStore {
    private static Context ctx;
    private static ContentResolver sResolver;
    private static final String TAG = "ContactStore";

    private DatabaseHandler db;
    private List<Contact> allContacts = new ArrayList<>();

    public ContactStore(Context context) {
        ctx = context;
        sResolver = ctx.getContentResolver();
        db = new DatabaseHandler(context);

        // populate contact to database when first time
        if(db.isDatabaseEmpty()){
            //init contact data
            retriveAllContact();
            // save to database
            db.addListContact(allContacts);
        }else{
            allContacts.clear();
            allContacts = db.getAllContacts();
        }
        Collections.sort(allContacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                String l1 = c1.name.toLowerCase();
                String l2 = c2.name.toLowerCase();
                return l1.compareTo(l2);
            }
        });
    }

    public Contact getByRecipientId(long recipientId) {
        Cursor addrCursor = sResolver.query(Uri.parse("content://mms-sms/canonical-address/" + recipientId), null,null, null, null);
        addrCursor.moveToFirst();
        String number = addrCursor.getString(0); // we got number here
        number=number.replace(" ", "");
        number=number.replace("-", "");
        Contact c = db.findContactByNumber(number);
        return c;
    }

    public List<Contact> getAllContacts() {
        return allContacts;
    }

    public Contact getDetailsContact(Contact c){
        return db.getDetailsContact(c);
    }

    private void retriveAllContact() {
        ContentResolver cr = ctx.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                long id = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String photoId;
                try {
                    photoId = cur.getString(cur.getColumnIndex(ContactsContract.Data.PHOTO_URI));
                } catch (Exception e) {
                    photoId = "";
                }

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    List<String> phones = new ArrayList<>();
                    Cursor cursor = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id+""}, null);

                    while (cursor.moveToNext()){
                        String p = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        p=p.replace(" ", "");
                        p=p.replace("-", "");
                        if(!phones.contains(p)){
                            phones.add(p);
                        }
                    }
                    Contact c = new Contact();
                    c.id = id;
                    c.name = name;
                    c.photoUri = photoId;
                    c.allPhoneNumber = phones;
                    allContacts.add(c);
                    cursor.close();
                }
            }
        }
    }
}
