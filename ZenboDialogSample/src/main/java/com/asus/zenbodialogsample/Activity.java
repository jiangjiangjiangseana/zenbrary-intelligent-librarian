package com.asus.zenbodialogsample;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.Button;
import android.widget.TextView;

import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

public class Activity extends RobotActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        TextView activityInfoText = (TextView)findViewById(R.id.activityInfoText);


        activityInfoText.setTextSize(20);

        Intent it = this.getIntent();

        String resCurrentDate = it.getStringExtra("rescurrentDate");
        String resFirstWeek = it.getStringExtra("resfirstWeek");
        String resSecondWeek = it.getStringExtra("ressecWeek");
//        System.out.println("Success to Activity with: "+resCurrentDate+" "+resFirstWeek+ " "+resSecondWeek);

        // set button
        Button button1 = (Button) findViewById(R.id.button1);


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
//
//        Button button1 = (Button) findViewById(R.id.button1);
//
//        Spanned span = Html.fromHtml("<img src=\""+R.drawable.cloud_server+"\"/><font color = \"ffffff\">test</font>",imgGetter,null);
//        button1.setText(span);
//
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
