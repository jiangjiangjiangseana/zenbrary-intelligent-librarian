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
import com.asus.robotframework.API.results.DetectPersonResult;
import com.asus.robotframework.API.results.GesturePointResult;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

import java.util.List;

public class VisionRequestDetectPerson extends RobotActivity {
    private Button mBtnDetectPerson;
    private Button mBtnCancelDetectPerson;

    private TextView mTextViewDetectPerson;
    private TextView mTextViewCancelDetectPerson;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vision_request_detect_person_and_cancel);

        //title
        TextView mTextViewTitle = (TextView)findViewById(R.id.textview_title);
        mTextViewTitle.setText(getString(R.string.toolbar_title_subclass_vision_title));

        context = getApplicationContext();

        mTextViewDetectPerson = (TextView) findViewById(R.id.textview_vision_requestDetectPerson);
        mBtnDetectPerson = (Button) findViewById(R.id.DetectPerson);
        mBtnDetectPerson.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetectPerson();

                mBtnDetectPerson.setEnabled(false);
                mBtnCancelDetectPerson.setEnabled(true);

                mTextViewDetectPerson.setEnabled(false);
                mTextViewCancelDetectPerson.setEnabled(true);
            }
        });



        mTextViewCancelDetectPerson = (TextView) findViewById(R.id.textview_vision_cancelDetectPerson);
        mBtnCancelDetectPerson = (Button) findViewById(R.id.CancelDetectPerson);
        mBtnCancelDetectPerson.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDetectPerson();

                mBtnDetectPerson.setEnabled(true);
                mBtnCancelDetectPerson.setEnabled(false);

                mTextViewDetectPerson.setEnabled(true);
                mTextViewCancelDetectPerson.setEnabled(false);
            }
        });


        String stringtemp = (String) mBtnDetectPerson.getText();
        stringtemp = stringtemp + "( 1000 )";
        mBtnDetectPerson.setText(stringtemp);

        mBtnDetectPerson.setEnabled(true);
        mBtnCancelDetectPerson.setEnabled(false);
        mTextViewDetectPerson.setEnabled(true);
        mTextViewCancelDetectPerson.setEnabled(false);
    }

    private void startDetectPerson() {
        // start detect person
        robotAPI.vision.requestDetectPerson(1000);

    }

    private void stopDetectPerson() {
        // stop detect person
        robotAPI.vision.cancelDetectPerson();

    }


    @Override
    protected void onPause() {
        super.onPause();
        // stop detect person
        robotAPI.vision.cancelDetectPerson();

        mBtnDetectPerson.setEnabled(true);
        mBtnCancelDetectPerson.setEnabled(false);

        mTextViewDetectPerson.setEnabled(true);
        mTextViewCancelDetectPerson.setEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // stop detect person
        robotAPI.vision.cancelDetectPerson();

        mBtnDetectPerson.setEnabled(true);
        mBtnCancelDetectPerson.setEnabled(false);

        mTextViewDetectPerson.setEnabled(true);
        mTextViewCancelDetectPerson.setEnabled(false);

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

            Log.d("RobotDevSample", "onDetectPersonResult: " + resultList.get(0).getBodyLoc().toString());

            // use toast to show detected persons
            String toast_result = "Detect Person";
            Toast toast = Toast.makeText(context, toast_result, Toast.LENGTH_SHORT);
            toast.show();

        }

        @Override
        public void onGesturePoint(GesturePointResult result) {
            super.onGesturePoint(result);
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



    public VisionRequestDetectPerson() {
        super(robotCallback, robotListenCallback);
    }

}
