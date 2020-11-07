package com.asus.zenbodialogsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import android.support.v7.widget.Toolbar;

//public class Personal extends RobotActivity{
public class Personal extends AppCompatActivity {

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    static Personal userClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        System.out.println("sucess to change to user");

        userClass = Personal.this;
        Intent userIt = this.getIntent();
        String user_info = userIt.getStringExtra("user_info");
        System.out.println("user_info sucess: "+ user_info);




        //logoutButton初始化
        Button logoutBt = (Button) findViewById(R.id.logoutBtn);
        logoutBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清空user資訊，上船瀏覽資料，寫成一個function，因為超過時間也要這樣做
                    Intent backIT = new Intent();
                    backIT.setClass(Personal.this,ZenboDialogSample.class);
                    startActivity(backIT);
                }
            });

        //hashButton 初始化
        Button hashButton = findViewById(R.id.hashtagBtn);
        hashButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashtag_alert();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    //限制內建返回按鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }

    public void hashtag_alert(){

        //inflate目的是把自己設計xml的Layout轉成View，作用類似於findViewById，它用於一個沒有被載入或者想要動態

        //載入的介面，當被載入Activity後才可以用findViewById來獲得其中界面的元素

        LayoutInflater inflater = LayoutInflater.from(Personal.this);
        final View v = inflater.inflate(R.layout.activity_hashtag, null);

        //語法一：new AlertDialog.Builder(主程式類別).XXX.XXX.XXX;
        AlertDialog dialog = new AlertDialog.Builder(Personal.this)
                .setTitle("請輸入Hashtag")
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) (v.findViewById(R.id.editText_alert));
                        Toast.makeText(getApplicationContext(), "你的id是" +
                                editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(Personal.this, "我還尚未了解",Toast.LENGTH_SHORT).show();
                    }

                })
                .setNeutralButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(Personal.this, "取消",Toast.LENGTH_SHORT).show();
                    }

                })
                .show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.show();
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


//    public Personal() {
//        super(robotCallback, robotListenCallback);
//    }

}
