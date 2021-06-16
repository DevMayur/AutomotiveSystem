package com.mayur.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mayur.R;
import com.mayur.utils.ViewAnimation;

public class ActivityStepper extends AppCompatActivity {

    private int MAX_STEP = 1;
    private int current_step = 1;
    private TextView status;
    String step1,step2,step3,step4,step5,step6,step7,step8,step9,step10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper);

        MAX_STEP = getIntent().getIntExtra("max_step",5);
        step1 = getIntent().getStringExtra("step1");
        step2 = getIntent().getStringExtra("step2");
        step3 = getIntent().getStringExtra("step3");
        step4 = getIntent().getStringExtra("step4");
        step5 = getIntent().getStringExtra("step5");
        step6 = getIntent().getStringExtra("step6");
        step7 = getIntent().getStringExtra("step7");
        step8 = getIntent().getStringExtra("step8");
        step9 = getIntent().getStringExtra("step9");
        step10 = getIntent().getStringExtra("step10");

//        initToolbar();
        initComponent();



    }


    private void initComponent() {
        status = (TextView) findViewById(R.id.tv_main_text);

        ((LinearLayout) findViewById(R.id.lyt_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView)findViewById(R.id.text_back)).getText().equals("RETURN")) {
                    ActivityStepper.super.onBackPressed();
                }else {
                    backStep(current_step);
                }
            }
        });

        ((LinearLayout) findViewById(R.id.lyt_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView)findViewById(R.id.text_next)).getText().equals("FINISH")) {
                    ActivityStepper.super.onBackPressed();
                }else {
                    nextStep(current_step);
                }
            }
        });

        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.tv_steps)).setText(str_progress);
        status.setText(step1);
        ((TextView) findViewById(R.id.text_back)).setText("RETURN");

    }

    private void nextStep(int progress) {
        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
            ViewAnimation.fadeOutIn(status);
        }
        Log.d("progress_check","progress : " + progress);
        Log.d("progress_check","currentStep : " + current_step);

        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.tv_steps)).setText(str_progress);
        setStepperText(status,progress);


    }

    private void backStep(int progress) {
        if (progress > 1) {
            progress--;
            current_step = progress;
            ViewAnimation.fadeOutIn(status);
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.tv_steps)).setText(str_progress);
        setStepperText(status,progress);
    }

    private void setStepperText(TextView text, int progress) {
        if (progress == MAX_STEP) {
            ((TextView) findViewById(R.id.text_next)).setText("FINISH");
        }else {
            ((TextView) findViewById(R.id.text_next)).setText("NEXT");
        }

        if (progress == 1) {
            ((TextView) findViewById(R.id.text_back)).setText("RETURN");
        }else {
            ((TextView) findViewById(R.id.text_back)).setText("BACK");
        }

        switch (progress) {
            case 1:
                text.setText(step1);
                break;

            case 2:
                text.setText(step2);
                break;

            case 3:
                text.setText(step3);
                break;

            case 4 :
                text.setText(step4);
                break;

            case 5:
                text.setText(step5);
                break;

            case 6:
                text.setText(step6);
                break;

            case 7:
                text.setText(step7);
                break;

            case 8 :
                text.setText(step8);
                break;

            case 9:
                text.setText(step9);
                break;

            case 10 :
                text.setText(step10);
                break;
        }
    }


}