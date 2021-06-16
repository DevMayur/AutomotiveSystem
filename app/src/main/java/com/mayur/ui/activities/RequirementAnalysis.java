package com.mayur.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mayur.R;

public class RequirementAnalysis extends AppCompatActivity {


    Button bt_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_analysis);

        bt_details = findViewById(R.id.bt_details);
        bt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequirementAnalysis.this,DetailedRequirementsActivity.class));
            }
        });


    }
}