package com.asus.zenbodialogsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.asus.robotframework.API.RobotFace;
import com.robot.asus.robotactivity.RobotActivity;
import com.asus.robotframework.API.VisionConfig;
import org.json.JSONObject;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;


public class ZenboDialogSample extends RobotActivity {
    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    static ZenboDialogSample zenboDialogSample;
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

//        //zenbo detect person
//        int ttt = robotAPI.vision.requestDetectPerson(new VisionConfig.PersonDetectConfig());
    }


    @Override
    protected void onResume() {
        super.onResume();
        // jump dialog domain
        robotAPI.robot.jumpToPlan(DOMAIN, "lanuchHelloWolrd_Plan");

    }

    @Override
    protected void onPause() {
        robotAPI.robot.stopSpeakAndListen();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        robotAPI.vision.cancelDetectFace();
        super.onDestroy();
    }

    //限制內建返回按鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
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
            //robotAPI.robot.setExpression(RobotFace.HIDEFACE);
            personDetected = false;
            }
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