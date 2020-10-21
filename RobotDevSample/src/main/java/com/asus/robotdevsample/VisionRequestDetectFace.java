package com.asus.robotdevsample;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.asus.robotframework.API.VisionConfig;
import com.asus.robotframework.API.results.DetectFaceResult;
import com.asus.robotframework.API.results.DetectPersonResult;
import com.asus.robotframework.API.results.GesturePointResult;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

import java.util.List;

public class VisionRequestDetectFace extends RobotActivity {
    private Button mBtnDetectFace;
    private Button mBtnCancelDetectFace;

    private TextView mTextViewDetectFace;
    private TextView mTextViewCancelDetectFace;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vision_request_detect_face_and_cancel);

        //title
        TextView mTextViewTitle = (TextView)findViewById(R.id.textview_title);
        mTextViewTitle.setText(getString(R.string.toolbar_title_subclass_vision_title));

        context = getApplicationContext();

        mTextViewDetectFace = (TextView) findViewById(R.id.textview_vision_requestDetectFace);
        mBtnDetectFace = (Button) findViewById(R.id.DetectFace);
        mBtnDetectFace.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetectFace();

                mBtnDetectFace.setEnabled(false);
                mBtnCancelDetectFace.setEnabled(true);

                mTextViewDetectFace.setEnabled(false);
                mTextViewCancelDetectFace.setEnabled(true);
            }
        });



        mTextViewCancelDetectFace = (TextView) findViewById(R.id.textview_vision_cancelDetectFace);
        mBtnCancelDetectFace = (Button) findViewById(R.id.CancelDetectFace);
        mBtnCancelDetectFace.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDetectFace();

                mBtnDetectFace.setEnabled(true);
                mBtnCancelDetectFace.setEnabled(false);

                mTextViewDetectFace.setEnabled(true);
                mTextViewCancelDetectFace.setEnabled(false);
            }
        });



        mBtnDetectFace.setEnabled(true);
        mBtnCancelDetectFace.setEnabled(false);
        mTextViewDetectFace.setEnabled(true);
        mTextViewCancelDetectFace.setEnabled(false);
    }

    private void startDetectFace() {
        // start detect face
        VisionConfig.FaceDetectConfig config = new VisionConfig.FaceDetectConfig();
        config.enableDebugPreview = true;  // set to true if you need preview screen
        config.intervalInMS = 1000;
        config.enableDetectHead = true;
        robotAPI.vision.requestDetectFace(config);
    }

    private void stopDetectFace() {
        // stop detect face
        robotAPI.vision.cancelDetectFace();
    }


    @Override
    protected void onPause() {
        super.onPause();

        robotAPI.vision.cancelDetectFace();

        mBtnDetectFace.setEnabled(true);
        mBtnCancelDetectFace.setEnabled(false);

        mTextViewDetectFace.setEnabled(true);
        mTextViewCancelDetectFace.setEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();

        robotAPI.vision.cancelDetectFace();

        mBtnDetectFace.setEnabled(true);
        mBtnCancelDetectFace.setEnabled(false);

        mTextViewDetectFace.setEnabled(true);
        mTextViewCancelDetectFace.setEnabled(false);

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
        public void onDetectPersonResult(List<DetectPersonResult> resultList) {
            super.onDetectPersonResult(resultList);

        }

        @Override
        public void onGesturePoint(GesturePointResult result) {
            super.onGesturePoint(result);
        }

        @Override
        public void onDetectFaceResult(List<DetectFaceResult> resultList) {
            super.onDetectFaceResult(resultList);

            Log.d("RobotDevSample", "onDetectFaceResult: " + resultList.get(0));

            //use toast to show detected faces
            String toast_result = "Detect Face";
            Toast toast = Toast.makeText(context, toast_result, Toast.LENGTH_SHORT);
            toast.show();
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



    public VisionRequestDetectFace() {
        super(robotCallback, robotListenCallback);
    }

}
