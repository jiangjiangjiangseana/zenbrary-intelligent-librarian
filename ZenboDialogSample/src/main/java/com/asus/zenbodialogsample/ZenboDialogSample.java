package com.asus.zenbodialogsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
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
    private static TextView mTextView;
    public static String  resAnswer;
    static ZenboDialogSample zenboDialogSample;
    static JSONObject resJson;
    static String targetUrl;
    static boolean personDetected ;
    private Timer timer;
    private TimerTask task;
    private int currentTime;





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

    private void showRobotFace(){
        robotAPI.robot.setExpression(RobotFace.ACTIVE);
//        RelativeLayout rl = new RelativeLayout(this);
//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                robotAPI.robot.setExpression(RobotFace.HIDEFACE);
//                System.out.println("screen is touched");
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        startTimer();
//        // close faical
//        robotAPI.robot.setExpression(RobotFace.HIDEFACE);

        // jump dialog domain
        robotAPI.robot.jumpToPlan(DOMAIN, "lanuchHelloWolrd_Plan");

    }
//    private void initTimer() {
//        // 初始化计时器
//        task = new MyTask();
//        timer = new Timer();
//    }


//    class MyTask extends TimerTask {
//        @Override
//        public void run() {
//            // 初始化计时器
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    currentTime++;
//                    if (currentTime == 10) {
//                        //在这里弹窗然后停止计时
//                        showRobotFace();
//                        stopTimer();
//                    }
//                }
//            });
//        }
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //有按下动作时取消定时
//                stopTimer();
//                break;
//            case MotionEvent.ACTION_UP:
//                //抬起时启动定时
//                startTimer();
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//
//    }

//    private void startTimer() {
//        //启动计时器
//        /**
//         * java.util.Timer.schedule(TimerTask task, long delay, long period)：
//         * 这个方法是说，delay/1000秒后执行task,然后进过period/1000秒再次执行task，
//         * 这个用于循环任务，执行无数次，当然，你可以用timer.cancel();取消计时器的执行。
//         */
//        initTimer();
//        try {
//            timer.schedule(task, 10000, 1000);
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            initTimer();
//            timer.schedule(task, 10000, 1000);
//        }
//    }
//
//    private void stopTimer() {
//        if (timer != null) {
//            timer.cancel();
//        }
//        currentTime = 0;
//        }

    @Override
    protected void onPause() {
        robotAPI.robot.stopSpeakAndListen();
//        stopTimer();
        super.onPause();
        //stop listen user utterance



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
        robotAPI.vision.cancelDetectFace();
//        stopTimer();
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

        @Override
        public void onDetectPersonResult(java.util.List resultList){
            System.out.println("a person detected");
            personDetected = true;
            if(personDetected){
            //robotAPI.robot.setExpression(RobotFace.HIDEFACE);
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