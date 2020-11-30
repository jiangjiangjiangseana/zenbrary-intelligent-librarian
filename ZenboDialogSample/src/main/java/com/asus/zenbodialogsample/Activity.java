package com.asus.zenbodialogsample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class Activity extends RobotActivity {
    static int but ;
    static ArrayList<String> firstActivity;
    static ArrayList<String> events;
    static int day;
    static Activity activity;
    static String rescurrentDate;
    static String resfirstWeek;
    static String ressecWeek;
    public String calendarUrl = "http://140.119.19.18:5000/api/v1/calendar/";
    static int count;


    protected void onCreate(Bundle savedInstanceState) {
        count = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
//        TextView activityInfoText = (TextView)findViewById(R.id.activityInfoText);
//        robotAPI.robot.speak("今天有展覽喔!");


//        activityInfoText.setTextSize(20);
        activity = this;
        Intent it = this.getIntent();

        String resCurrentDate = it.getStringExtra("rescurrentDate");
        String resFirstWeek = it.getStringExtra("resfirstWeek");
        String resSecondWeek = it.getStringExtra("ressecWeek");
        System.out.println("Success to Activity with: "+resCurrentDate+" "+resFirstWeek+ " "+resSecondWeek);


        final ArrayList<String> firstWeek = new ArrayList<>(Arrays.asList(resFirstWeek.split("],")));
        System.out.println("First week:"+firstWeek);
        final ArrayList<String> dates = new ArrayList<>(Arrays.asList(firstWeek.get(0).substring(2).split(",")));
        firstActivity = new ArrayList<>(Arrays.asList(resFirstWeek.split("\\[\\[")));

        System.out.println("firstActivity:"+firstActivity);


//        for (int i = 0; i<dates.size(); i++){
//        System.out.println("Dates: "+dates.get(i));
//        }






        // set button
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                but = 0;
                System.out.println("Activities:"+firstActivity.get(but+2));
                events = new ArrayList<>(Arrays.asList(firstActivity.get(but+2).split("],")));
                System.out.println("events喔喔"+events);
                addActivity(events.size());

            }
        });
        button1.setText(dates.get(0).substring(1,11));
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                but = 1;
                System.out.println("Activities:"+firstActivity.get(but+2));
                events = new ArrayList<>(Arrays.asList(firstActivity.get(but+2).split("],")));
                System.out.println(events);
                addActivity(events.size());

            }
        });
        button2.setText(dates.get(1).substring(1,11));
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                but = 2;
                System.out.println("Activities:"+firstActivity.get(but+2));
                events = new ArrayList<>(Arrays.asList(firstActivity.get(but+2).split("],")));
                System.out.println(events);
                addActivity(events.size());

            }
        });
        button3.setText(dates.get(2).substring(1,11));
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                but = 3;
                System.out.println("Activities:"+firstActivity.get(but+2));
                events = new ArrayList<>(Arrays.asList(firstActivity.get(but+2).split("],")));
                System.out.println(events);
                addActivity(events.size());

            }
        });
        button4.setText(dates.get(3).substring(1,11));
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                but = 4;
                System.out.println("Activities:"+firstActivity.get(but+2));
                events = new ArrayList<>(Arrays.asList(firstActivity.get(but+2).split("],")));
                System.out.println(events);
                addActivity(events.size());

            }
        });
        button5.setText(dates.get(4).substring(1,11));
        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                but = 5;
                System.out.println("Activities:"+firstActivity.get(but+2));
                events = new ArrayList<>(Arrays.asList(firstActivity.get(but+2).split("],")));
                System.out.println(events);
                addActivity(events.size());

            }
        });
        button6.setText(dates.get(5).substring(1,11));
        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                but = 6;
                System.out.println("Activities:"+firstActivity.get(but+2));
                events = new ArrayList<>(Arrays.asList(firstActivity.get(but+2).split("],")));
                System.out.println(events);
                addActivity(events.size());

            }
        });
        button7.setText(dates.get(6).substring(1,11));

        Button button8 = (Button)findViewById(R.id.button8);
        button8.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                count += 1;
                System.out.println("count喔喔喔喔"+count);
                day = count*7;
                calendarAPI(day);

            }
        });



//        Html.ImageGetter imgGetter = new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String source) {
//                Drawable drawable = null;
//                drawable = Activity.this.getResources().getDrawable(
//                        Integer.parseInt(source)
//                );
//                drawable.setBounds(0,0,120,100);
//                return drawable;
//
//            }
//        };

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

    public void calendarAPI(int day){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running calendar");
                String uniqueID = UUID.randomUUID().toString();
                String rawData = "{\"day\":\""+ Activity.day +"\",\"session_id\":\""+uniqueID+"\"}";
                String charset = "UTF-8";
                System.out.println("calendar request: "+rawData);

                URLConnection connection = null;
                try {
                    connection = new URL(calendarUrl).openConnection();
                } catch (Exception e) {
                    System.out.println("calendar connection failed");
                    e.printStackTrace();
                }
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
                try (OutputStream output = connection.getOutputStream()) {
                    Log.d("calendar output format",output.toString());
                    output.write(rawData.getBytes(charset));
                } catch (Exception e) {
                    System.out.println("error in receiving calendar result");
                    e.printStackTrace();
                }
                InputStream response = null;
                try {
                    response = connection.getInputStream();
                    System.out.println("receiving calendar_result : "+response.toString() + " " + response);
                } catch (Exception e) {
                    System.out.println("error in receiving calendar_result");
                    e.printStackTrace();
                }
                try {
                    System.out.println("start translate calendar_result: ");
                    String text;
                    if (response != null) {
                        Writer writer = new StringWriter();
                        char[] buffer = new char[1024];
                        try {
                            Reader reader = new BufferedReader(
                                    new InputStreamReader(response, "UTF-8"));
                            int n;
                            while ((n = reader.read(buffer)) != -1) {
                                writer.write(buffer, 0, n);
                            }
                        } finally {
                            response.close();
                        }
                        text =  writer.toString();
                    } else {
                        text =  "";
                    }
                    System.out.println("response calendar_info: "+text);
                    JSONObject resJson;
                    resJson = new JSONObject(text);
                    System.out.println("calendar_resJson: "+ resJson);

                    rescurrentDate = resJson.getString("current_date");
                    resfirstWeek = resJson.getString("first_week_calendar");
                    ressecWeek = resJson.getString("second_week_calendar");
                    System.out.println("response calendar_info_next: "+rescurrentDate +" "+resfirstWeek+ " "+ ressecWeek);
                } catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("error in translate calendar_info");
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("切換到Activity");
                        final ArrayList<String> firstWeek = new ArrayList<>(Arrays.asList(resfirstWeek.split("],")));
                        System.out.println("First week:"+firstWeek);
                        final ArrayList<String> dates = new ArrayList<>(Arrays.asList(firstWeek.get(0).substring(2).split(",")));
                        firstActivity = new ArrayList<>(Arrays.asList(resfirstWeek.split("\\[\\[")));
                        System.out.println("firstActivity_next"+firstActivity);
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

                    }
                });
            }
        }).start();
    }


//    public final void getEvent(int e){
//        System.out.println("Activities:"+firstActivity.get(e+2));
//        events = new ArrayList<>(Arrays.asList(firstActivity.get(e+2).split("],")));
//        System.out.println(events);
//    }

    public void addActivity(int size){
        AbsoluteLayout layout = (AbsoluteLayout) findViewById(R.id.layout1);
        layout.removeAllViews();
        int y = 20;
        int z = 100;
        for (int i = 0; i < size; i++){
            ImageView imageView = new ImageView(this);
            Drawable drawable = getResources().getDrawable(R.drawable.notes);
            imageView.setImageDrawable(drawable);
            imageView.setMinimumWidth(750);
            AbsoluteLayout.LayoutParams param = new AbsoluteLayout.LayoutParams(750
                    ,400,10,y);
            y += 400;
//            param.leftMargin = 10;
//            param.topMargin = 55;
            layout.addView(imageView,param);

            System.out.println(events.size());
            System.out.println("Event:"+events.get(i));
            final ArrayList<String> event = new ArrayList<>(Arrays.asList(events.get(i).split(",")));
            TextView textView = new TextView(getApplicationContext());
            textView.setText("活動時間:"+event.get(0).substring(1)+" \n\n 活動資訊:"+event.get(2)+" \n\n 活動類別:"+event.get(3));
            System.out.println("活動時間:"+event.get(0)+" \n\n 活動資訊:"+event.get(2)+" \n\n 活動類別:"+event.get(3));
            textView.setPadding(10,10,10,10);
            textView.setTextColor(Color.rgb(0,0,0));
            textView.setTextSize(30);
            textView.setWidth(350);
            AbsoluteLayout.LayoutParams param1 = new AbsoluteLayout.LayoutParams(400,300,130,z);
            z += 400;
            layout.addView(textView,param1);


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
