package com.asus.zenbodialogsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.asus.robotframework.API.DialogSystem;
import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.asus.robotframework.API.RobotFace;
import com.asus.robotframework.API.RobotUtil;
import com.asus.robotframework.API.SpeakConfig;
import com.robot.asus.robotactivity.RobotActivity;
import com.asus.robotframework.API.VisionConfig.PersonDetectConfig;
import com.asus.robotframework.API.VisionConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import android.widget.Button;




public class ZenboDialogSample extends RobotActivity {
    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    private static TextView mTextView;
    public static String  resAnswer;
    static ZenboDialogSample zenboDialogSample;
    static JSONObject resJson;
    static String targetUrl;
    static boolean personDetected ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zenbo_dialog_sample);
        zenboDialogSample = ZenboDialogSample.this;
        personDetected = false;
        // loginButton 初始化
        Button lButton = findViewById(R.id.loginButton);
        lButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("change layout to login");
                Intent loginIt = new Intent();
                //loginIt.putExtra("resJson",resJson.toString());
                loginIt.setClass(ZenboDialogSample.this,Login.class);
                startActivity(loginIt);
            }
        });

        // guestButton 初始化
        Button gButton = findViewById(R.id.guestButton);
        gButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotAPI.vision.cancelDetectFace();
                System.out.println("change layout to guest");
                Intent guestIt = new Intent();
                //equipIt.putExtra("resJson",resJson.toString());
                guestIt.setClass(ZenboDialogSample.this,Guest.class);
                startActivity(guestIt);
            }
        });

        int ttt = robotAPI.vision.requestDetectPerson(new VisionConfig.PersonDetectConfig());
        System.out.println("ttt: "+ttt);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // close faical
        robotAPI.robot.setExpression(RobotFace.HIDEFACE);

        // jump dialog domain
        robotAPI.robot.jumpToPlan(DOMAIN, "lanuchHelloWolrd_Plan");

    }


    @Override
    protected void onPause() {
        super.onPause();
        //stop listen user utterance
        robotAPI.robot.stopSpeakAndListen();
        robotAPI.vision.cancelDetectFace();


    }

    //限制內建返回按鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        robotAPI.vision.cancelDetectFace();

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

        @Override
        public void onDetectPersonResult(java.util.List resultList){
            System.out.println("a person detected");
            personDetected = true;
            if(personDetected){
            int sayActivity = robotAPI.robot.speak("歡迎~!");
            personDetected = false;
            }

//            robotAPI.robot.stopSpeak();

        }
    };


    public static final RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {

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


    public ZenboDialogSample() {
        super(robotCallback, robotListenCallback);
    }


}