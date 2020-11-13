package com.asus.zenbodialogsample;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asus.ctc.tool.DSAPI_Result;
import com.asus.robotframework.API.DialogSystem;
import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.asus.robotframework.API.RobotFace;
import com.asus.robotframework.API.RobotUtil;
import com.asus.robotframework.API.SpeakConfig;
import com.robot.asus.robotactivity.RobotActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

public class EquipmentIntro extends RobotActivity{

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    static EquipmentIntro facilityClass;
    static String faci_name;
    static int faci_floor;
    static String faci_introduce;
    static int faci_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip);
        robotAPI.vision.cancelDetectFace();
        System.out.println("sucess to change to facility");

        //backButton 初始化
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIT = new Intent();
                backIT.setClass(EquipmentIntro.this,Guest.class);
                startActivity(backIT);
            }
        });


        //layout資訊初始化
        facilityClass = EquipmentIntro.this;
        Intent it = this.getIntent();
        String resJsonString = it.getStringExtra("resJson");
        System.out.println("facility success receive: " + resJsonString);
        try {
            final JSONObject[] resJson = {new JSONObject(resJsonString)};
            System.out.println("resfacilityJson:" + resJson);
            faci_name = resJson[0].getString("faci_name");
            faci_floor = resJson[0].getInt("floor");
            faci_introduce = resJson[0].getString("introduce");
            faci_number = resJson[0].getInt("number");
            System.out.println("facility_name: " + faci_name + " floor: " + faci_floor + " introduce: " + faci_introduce);
        } catch (JSONException e) {
            System.out.println("error in change string into json");
            e.printStackTrace();
        }
        if(faci_name==""||faci_floor==0){
            Intent failIt = new Intent();
            failIt.setClass(EquipmentIntro.this,Guest.class);
            startActivity(failIt);
            robotAPI.robot.speakAndListen("不好意思，請重複一次",new SpeakConfig().timeout(15));
        }else{
        TextView faci_name_tv = (TextView) findViewById(R.id.faci_name_textView);
        faci_name_tv.setText(faci_name);
        TextView faci_intro_tv = (TextView) findViewById(R.id.faci_intro_textView);
        faci_intro_tv.setText(faci_introduce);
        ImageView floor_image = (ImageView) findViewById(R.id.floor_image);
        if(faci_floor == 1){
        floor_image.setImageResource(R.drawable.t1);
        }else if(faci_floor == 2){
            floor_image.setImageResource(R.drawable.t2);
        }else if(faci_floor == 3){
            floor_image.setImageResource(R.drawable.t3);
        }else if(faci_floor == 4){
            floor_image.setImageResource(R.drawable.t4);
        }else if(faci_floor == 5){
            floor_image.setImageResource(R.drawable.t5);
        }else if(faci_floor == 7){
            floor_image.setImageResource(R.drawable.t7);
        }else if(faci_floor == 8){
            floor_image.setImageResource(R.drawable.t8);
        }
        robotAPI.robot.speak(faci_name+"在達賢"+faci_floor+"樓的"+faci_number+"號唷");
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


    public EquipmentIntro() {
        super(robotCallback, robotListenCallback);
    }

}