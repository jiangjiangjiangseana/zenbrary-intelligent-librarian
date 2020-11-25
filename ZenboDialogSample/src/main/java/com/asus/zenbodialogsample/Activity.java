package com.asus.zenbodialogsample;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.google.gson.JsonObject;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Activity extends RobotActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        TextView activityInfoText = (TextView)findViewById(R.id.activityInfoText);
        robotAPI.robot.speak("今天有展覽喔!");


//        activityInfoText.setTextSize(20);

        Intent it = this.getIntent();

        String resCurrentDate = it.getStringExtra("rescurrentDate");
        String resFirstWeek = it.getStringExtra("resfirstWeek");
        String resSecondWeek = it.getStringExtra("ressecWeek");
        System.out.println("Success to Activity with: "+resCurrentDate+" "+resFirstWeek+ " "+resSecondWeek);


        final ArrayList<String> firstWeek = new ArrayList<>(Arrays.asList(resFirstWeek.split("],")));
        System.out.println("First week:"+firstWeek);
        final ArrayList<String> dates = new ArrayList<>(Arrays.asList(firstWeek.get(0).substring(2).split(",")));
        final ArrayList<String> firstActivity = new ArrayList<>(Arrays.asList(resFirstWeek.split("\\[\\[")));

        System.out.println("firstActivity:"+firstActivity);
        for (int i = 0; i<dates.size(); i++){
            System.out.println("Dates: "+dates.get(i));


            for (int j = 1; j<firstActivity.size();j++){
                System.out.println("Activities:"+firstActivity.get(i+2));
                final ArrayList<String> event = new ArrayList<>(Arrays.asList(firstActivity.get(i+2).split("],")));
                System.out.println("Event:"+event);
                addActivity(1);
            }


            break;

        }





        // set button
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setText(dates.get(0).substring(1,11));
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setText(dates.get(1).substring(1,11));
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setText(dates.get(2).substring(1,11));
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setText(dates.get(3).substring(1,11));
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setText(dates.get(4).substring(1,11));
        Button button6 = (Button) findViewById(R.id.button6);
        button6.setText(dates.get(5).substring(1,11));
        Button button7 = (Button) findViewById(R.id.button7);
        button7.setText(dates.get(6).substring(1,11));



        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = null;
                drawable = Activity.this.getResources().getDrawable(
                        Integer.parseInt(source)
                );
                drawable.setBounds(0,0,120,100);
                return drawable;

            }
        };

//        Button button1 = (Button) findViewById(R.id.button1);


//        Spanned span = Html.fromHtml("<img src=\""+R.drawable.cloud_server+"\"/><font color = \"ffffff\">test</font>",imgGetter,null);
//        button1.setText(span);

//
//        Spanned span = Html.fromHtml("<img src=\""+R.drawable.cloud_server+"\"/><font color = \"ffffff\">test</font>",imgGetter,null);
//        button1.setText(span);

        //backButton 初始化
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIT = new Intent();
                backIT.setClass(Activity.this,Guest.class);
                startActivity(backIT);
            }
        });

    }

    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

    public void addActivity(int size){
        layout.removeAllViews();
        for (int i = 0; i < size; i++){
            ImageView imageView = new ImageView(this);
            Drawable drawable = getResources().getDrawable(R.drawable.notes);
            imageView.setImageDrawable(drawable);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(730,350);
            param.leftMargin = 10;
            param.topMargin = 55;
            layout.addView(imageView,param);

        }

    }



    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onPause() {
        super.onPause();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static RobotCallback robotCallback = new RobotCallback() {
        @Override
        public void onResult(int cmd, int serial, RobotErrorCode err_code, Bundle result) {
            super.onResult(cmd, serial, err_code, result);
        }

        @Override
        public void onStateChange(int cmd, int serial, RobotErrorCode err_code, RobotCmdState state) {
            super.onStateChange(cmd, serial, err_code, state);
        }

        @Override
        public void initComplete() {
            super.initComplete();

        }
    };


    public static RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {

        @Override
        public void onFinishRegister() {

        }

        @Override
        public void onVoiceDetect(JSONObject jsonObject) {

        }

        @Override
        public void onSpeakComplete(String s, String s1) {

        }

        @Override
        public void onEventUserUtterance(JSONObject jsonObject) {

        }

        @Override
        public void onResult(JSONObject jsonObject) {

        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };


    public Activity() {
        super(robotCallback, robotListenCallback);
    }


}
