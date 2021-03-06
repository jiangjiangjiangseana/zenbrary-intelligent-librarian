package com.asus.zenbodialogsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.robot.asus.robotactivity.RobotActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.view.View;
import static java.lang.Integer.parseInt;

public class Register extends RobotActivity{

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    public static String registerUrl = "http://140.119.19.18:5001/api/v1/register/";
    static Register registerClass;
    static String responseState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerClass = Register.this;
        //backButton 初始化
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIT = new Intent();
                backIT.setClass(Register.this,Login.class);
                startActivity(backIT);
            }
        });
        //confirmButton初始化
        Button confirmBt = (Button) findViewById(R.id.confirmButton);
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_user_name = (EditText) findViewById(R.id.editText_user_name);
                EditText edit_student_id = (EditText) findViewById(R.id.editText_studentId);
                EditText edit_account = (EditText) findViewById(R.id.editText_account);
                EditText edit_pw = (EditText) findViewById(R.id.editText_pw);
                EditText edit_confirm = (EditText) findViewById(R.id.editText_confirm);
                EditText edit_department = (EditText) findViewById(R.id.editText_department);
                RadioButton gender_male = (RadioButton) findViewById(R.id.gender_male);

                String user_name = edit_user_name.getText().toString();
                String student_id = edit_student_id.getText().toString();
                String account = edit_account.getText().toString();
                String password = edit_pw.getText().toString();
                String confirm_pw = edit_confirm.getText().toString();
                String department = edit_department.getText().toString();
                String gender;
                if(gender_male.isChecked()){
                    gender = "male";
                }else{
                    gender = "female";
                }
                System.out.println("user register information: "+user_name+" "+student_id+account+password+confirm_pw+department+gender);
                //connect to server register function
                if(password.equals(confirm_pw)) {
                    registerAPI(user_name,student_id, account, password, department, gender);
                }else{
                    System.out.println("confirm fail");
                    robotAPI.robot.speak("兩次密碼不一樣唷!");
                }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //限制內建返回按鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }

    public void registerAPI(final String user_name,final String student_id, final String account, final String password, final String department, final String gender){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running register");
                String rawData = "{\"uid\":\""+ parseInt(student_id) +"\",\"name\":\""+user_name+"\",\"email\":\""+account+"\",\"gender\":\""+gender+"\",\"department\":\""+department+"\",\"password\":\""+password+"\"}";
                String charset = "UTF-8";
                System.out.println("register request: "+rawData);

                URLConnection connection = null;
                try {
                    connection = new URL(registerUrl).openConnection();
                } catch (Exception e) {
                    Log.d(TAG,"register connection failed");
                    e.printStackTrace();
                }
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
                try (OutputStream output = connection.getOutputStream()) {
                    Log.d("register output format",output.toString());
                    output.write(rawData.getBytes(charset));
                } catch (Exception e) {
                    System.out.println("error in receiving register result");
                    e.printStackTrace();
                }
                InputStream response = null;
                try {
                    response = connection.getInputStream();
                    System.out.println("receiving register_result : "+response.toString() + " " + response);
                } catch (Exception e) {
                    System.out.println("error in receiving register_result");
                    e.printStackTrace();
                }
                try {
                    System.out.println("start translate register response: ");
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
                    System.out.println("response register: "+text);
                    JSONObject resJson = new JSONObject(text);
                    System.out.println("resJson: "+ resJson);
                    responseState = resJson.getString("res");

                    System.out.println("response register state: "+responseState);
                } catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("error in translate register_response");
                }
                registerClass.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    if(responseState.equals("success")) {
                        System.out.println("切換回到Login");
                        //go back to login
                        Intent loginIt = new Intent();
                        loginIt.setClass(Register.this, Login.class);
                        startActivity(loginIt);
                        robotAPI.robot.speak("註冊成功");
                    }else{
                        System.out.println("register fail");
                        robotAPI.robot.speak("註冊失敗，請在試一次");
                    }
                    }
                });
            }
        }).start();
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


    public Register() {
        super(robotCallback, robotListenCallback);
    }

}