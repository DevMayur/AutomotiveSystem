package com.mayur.shortmessage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mayur.R;
import com.mayur.shortmessage.data.DatabaseHandler;
import com.mayur.shortmessage.data.store.ContactStore;

import static android.Manifest.permission.CALL_PHONE;

public class ActivitySplash extends AppCompatActivity {

    private DatabaseHandler db;
    private ContactStore contact_store;
    private static final int REQUEST = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_messaging);

        db = new DatabaseHandler(getApplicationContext());

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS , Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, CALL_PHONE };
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST );
            } else {
                if(!db.isDatabaseEmpty()){
                    Intent i = new Intent(getApplicationContext(), ActivityMain.class);
                    startActivity(i);
                }else {
                    new PrepareData().execute("");
                }
            }
        } else {
            new PrepareData().execute("");
        }
        finish();


    }

    private class PrepareData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(10);
                db.truncateDB();
                contact_store = new ContactStore(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent i = new Intent(getApplicationContext(), ActivityMain.class);
            startActivity(i);
            super.onPostExecute(s);
        }
    }
}
