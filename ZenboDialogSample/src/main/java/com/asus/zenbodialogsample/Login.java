package com.asus.zenbodialogsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
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
import android.widget.EditText;

public class Login extends RobotActivity{

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    static Login loginClass;
    static String loginUrl = "http://140.119.19.18:5001/api/v1/userinfo/";
    static String user_info;
    static String responseState;
    static String u_id;
    static String email;
    static String uu_list;
    static String user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginClass = Login.this;

        //loginButton初始化
        Button loginBt = (Button) findViewById(R.id.loginButton);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText login_account = (EditText) findViewById(R.id.login_account);
                EditText login_pw = (EditText) findViewById(R.id.login_pw);
                String account = login_account.getText().toString();
                String password = login_pw.getText().toString();
                //connect to server login function
                login(account,password);
            }
        });

        // registerButton 初始化
        Button registerBt = findViewById(R.id.registerButton);
        registerBt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("change layout to register");
                Intent registerIt = new Intent();
                //loginIt.putExtra("resJson",resJson.toString());
                registerIt.setClass(Login.this,Register.class);
                startActivity(registerIt);
            }
        });

        //backButton 初始化
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIT = new Intent();
                backIT.setClass(Login.this,ZenboDialogSample.class);
                startActivity(backIT);
            }
        });



    }

    //限制內建返回按鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }


    public void login(final String account, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running login");
                String rawData = "{\"email\":\""+ account +"\",\"password\":\""+password+"\"}";
                String charset = "UTF-8";
                System.out.println("login request: "+rawData);

                URLConnection connection = null;
                try {
                    connection = new URL(loginUrl).openConnection();
                } catch (Exception e) {
                    Log.d(TAG,"login connection failed");
                    e.printStackTrace();
                }
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
                try (OutputStream output = connection.getOutputStream()) {
                    Log.d("login output format",output.toString());
                    output.write(rawData.getBytes(charset));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                InputStream response = null;
                try {
                    response = connection.getInputStream();
                    System.out.println("receiving login_result : "+response.toString() + " " + response);
                } catch (Exception e) {
                    System.out.println("error in receiving login_result");
                    e.printStackTrace();
                }
                try {
                    System.out.println("start translate user_info: ");

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
                        user_info =  writer.toString();
                    } else {
                        user_info =  "";
                    }
                    System.out.println("response user_info: "+user_info+ user_info.getClass());
                    JSONObject resJson = new JSONObject(user_info);
                    System.out.println("resJson: "+ resJson);
                    responseState = resJson.getString("res");
                    u_id = resJson.getString("uid");
                    email = resJson.getString("email");
                    uu_list = resJson.getString("book_info");
                    user_name = resJson.getString("name");
                } catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("error in translate user_info");
                }


                loginClass.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    if(responseState.equals("success")) {
                        System.out.println("切換到user");
                        //go back to login
                        Intent userIt = new Intent();
                        System.out.println("comfirm user info: " + user_info);
                        userIt.putExtra("change","login");
                        userIt.putExtra("user_name",user_name);
                        userIt.putExtra("u_id",u_id);
                        userIt.putExtra("email",email);
                        userIt.putExtra("uu_list",uu_list);
                        userIt.putExtra("user_info", user_info.toString());
                        userIt.setClass(Login.this, Personal.class);
                        startActivity(userIt);
                    }else{
                        //undo: show login fail message
                        robotAPI.robot.speak("登入失敗，請再試一次!");
                    }
                    }
                });
            }
        }).start();
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


    public Login() {
        super(robotCallback, robotListenCallback);
    }

}