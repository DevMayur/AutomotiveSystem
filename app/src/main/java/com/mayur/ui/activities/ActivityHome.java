package com.mayur.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mayur.R;

public class ActivityHome extends AppCompatActivity {

    LinearLayout ll_start_classifier,ll_project_details,ll_project_domain,ll_problem_defination,ll_literature_survey,ll_requirement_analysis,ll_system_design,ll_about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        ll_start_classifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this,DetectorActivity.class));
            }
        });

        ll_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this,ActivityAbout.class));
            }
        });

        ll_project_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityHome.this,ActivityStepper.class);
                intent.putExtra("max_step",4);
                intent.putExtra("step1","Tensorflow Object Detection API : \n\nCreating accurate machine learning models capable of localizing and identifying multiple objects in a single image remains a core challenge in computer vision. The TensorFlow Object Detection API is an open source framework built on top of TensorFlow that makes it easy to construct, train and deploy object detection models. ");
                intent.putExtra("step2","We used this Tensorflow object detection API to build our model and also convert that into TFLite model.\n" +
                        "(frozen_graph.pb -> model.tflite)\n");
                intent.putExtra("step3","there are SSDLite with MobileDet GPU backbone, which achieves 17% mAP higher than the MobileNetV2 SSDLite (27.5 mAP vs 23.5 mAP) on a NVIDIA Jetson Xavier at comparable latency (3.2ms vs 3.3ms).\n" +
                        "Along with the model definition, also there are model checkpoints trained on the COCO dataset.\n");
                intent.putExtra("step4","After creating tflite model usingTensorflow Object Detection API,  We used that model in android project.\n");

                startActivity(intent);
            }
        });

        ll_project_domain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityHome.this,ActivityStepper.class);
                intent.putExtra("max_step",8);
                intent.putExtra("step1","1. What is machine learning in autonomous vehicles? \n" +
                        "\n" +
                        "Autonomous or self-driving cars are beginning to occupy the same roads the general public drives on. That can make many people nervous about a vehicle’s ability to make safe decisions. Understanding one of the core technologies used in autonomous vehicles – machine learning – can help settle the minds of the wary.\n");
                intent.putExtra("step2","2.Why is machine learning important in AV?\n" +
                        "\n" +
                        "The intention is that self-driving cars will make roads safer because they can make better, more reliable decisions than a human mind. A car must ‘learn’ and adapt to the unpredictable behavior of other cars nearby. Machine learning algorithms make AVs capable of judgments in real time.\n" +
                        "This increases safety and trust in autonomous cars, which is the original goal. Without machine learning algorithms, an AV would always make the same decision based on its circumstances, even if variables that could change the outcome were different.\n");
                intent.putExtra("step3","3. Functions machine learning performs in autonomous driving\n" +
                        "\n" +
                        "Currently, machine learning is in an intermediate stage were it has begun to become mainstream thinking but has not yet become commonplace. Autonomous development has shown that machine learning can be successfully and reliably used for virtually all mobility functions when it’s been implemented. Here are a few of the real-world uses you can see today.\n");
                intent.putExtra("step4","4. Navigation\n" +
                        "A human drive can’t predict which routes are going to be congested based on a combination of real-time data and compiled data from the past. With machine learning algorithms, an AV’s navigation system can assign the fastest or shortest route based on the conditions surrounding the vehicle as well as any previous information, experienced or shared from other users. It can realistically trim minutes off a commute time.\n");
                intent.putExtra("step5","5. Safety maneuvers\n" +
                        "As an algorithm perpetually making decisions based on immediate surroundings and past experiences, machine learning can perform safety maneuvers faster than a driver can react. And while a human driver might be able to perform one evasive maneuver, AVs could potentially perform complex actions where a human could not avoid a collision. This can help keep pedestrians safer plus avoid distracted driving accidents more often.\n");
                intent.putExtra("step6","6. Entertainment\n" +
                        "A user’s in-cabin experience can be enhanced with machine learning. When you skip a song, it can change satellite radio stations for you when the disliked song is about to be played. It can also tune into your favorite podcast automatically or suggest a nearby fuel station when it detects your fuel level is low.\n");
                intent.putExtra("step7","7. Parking\n" +
                        "Undoubtedly, parallel parking and tight perpendicular parking are a source of frustration for many drivers. Powered by machine learning algorithms, an AV can detect its surroundings and park itself without driver input. It can also leave a parking space and return to the driver’s position driverless, allowing parking spots with tighter tolerances to be used.\n");
                intent.putExtra("step8","The implications for machine learning are vast and multifaceted. As autonomous driving progresses, you’ll start to see technology getting ‘smarter’ because of it.\n");

                startActivity(intent);
            }
        });

        ll_problem_defination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityHome.this,ActivityStepper.class);
                intent.putExtra("max_step",3);
                intent.putExtra("step1","TRAFFIC SIGNS RECOGNITION\n" +
                        "\n" +
                        "Traffic signs are an essential part of our day to day lives. They contain critical information that ensures the safety of all the people around us. Without traffic signs, all the drivers would be clueless about what might be ahead to them and roads can become a mess. The annual global roach crash statistics say that over 3,280 people die every day in a road accident. These numbers would be much higher in case if there were no traffic signs.\n");
                intent.putExtra("step2","The earlier Computer Vision techniques required lots of hard work in data processing and it took a lot of time to manually extract the features of the image. Now, deep learning techniques have come to the rescue\n");
                intent.putExtra("step3","The main objective of  our project is to design and construct a computer based system which can automatically detect the road signs so as to provide assistance to the user or the machine so that they can take appropriate actions.");

                startActivity(intent);
            }
        });

        ll_literature_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this, ArticleActivity.class));
            }
        });


        ll_requirement_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this, RequirementAnalysis.class));
            }
        });

        ll_system_design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this, ActivitySystemDesign.class));
            }
        });

    }

    private void initViews() {
        ll_start_classifier = findViewById(R.id.ll_start_classifier);
        ll_about = findViewById(R.id.ll_about);
        ll_project_details = findViewById(R.id.ll_project_details);
        ll_project_domain = findViewById(R.id.ll_project_domain);
        ll_problem_defination = findViewById(R.id.ll_problem_defination);
        ll_literature_survey = findViewById(R.id.ll_literature_survey);
        ll_requirement_analysis = findViewById(R.id.ll_requirement_analysis);
        ll_system_design = findViewById(R.id.ll_system_design);
    }
}