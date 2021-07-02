package com.mayur.document_vault;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mayur.R;

public class DocumentViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_viewer);

        String url = getIntent().getStringExtra("img_url");
        Glide.with(this).load(url).into((ImageView) findViewById(R.id.iv_image));
        ((TextView)findViewById(R.id.tv_text)).setText(getIntent().getStringExtra("title"));

    }
}