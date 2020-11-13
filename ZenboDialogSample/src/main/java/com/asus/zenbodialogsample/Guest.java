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
import com.google.gson.JsonObject;
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

import static java.lang.Integer.parseInt;


public class Guest extends RobotActivity {
    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    public String calendarUrl = "http://140.119.19.18:5000/api/v1/calendar/";
    private static TextView mTextView;
    public static String  resAnswer;
    static String rescurrentDate;
    static String resfirstWeek;
    static String ressecWeek;
    static Guest guest;
    static JSONObject resJson;
    static String targetUrl;
    static boolean personDetected ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        guest = Guest.this;
        mTextView = (TextView) findViewById(R.id.textview_info);
        personDetected = false;

        // bookButton 初始化
        Button bButton = findViewById(R.id.bookButton);
        bButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotAPI.robot.stopSpeak();
                robotAPI.vision.cancelDetectFace();
                targetUrl = "http://140.119.19.18:5000/api/v1/book_list/";
                int aaaab = robotAPI.robot.speakAndListen("您想找什麼書呢?", new SpeakConfig().timeout(15));
            }
        });
        // equipmentButton 初始化
        Button eButton = findViewById(R.id.equipmentButton);
        eButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotAPI.robot.stopSpeak();
                robotAPI.vision.cancelDetectFace();
                targetUrl = "http://140.119.19.18:5000/api/v1/facility/";
                int aaaab = robotAPI.robot.speakAndListen("您想找什麼呢?", new SpeakConfig().timeout(15));
            }
        });
        // questionButton 初始化
        Button qButton = findViewById(R.id.questionButton);
        qButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotAPI.vision.cancelDetectFace();
                targetUrl = "http://140.119.19.18:5000/api/v1/other/";
                int aaaab = robotAPI.robot.speakAndListen("有什麼是我能幫你的嗎?", new SpeakConfig().timeout(15));
            }
        });
        // activityButton 初始化
        Button aButton = findViewById(R.id.activityButton);
        aButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotAPI.vision.cancelDetectFace();
                System.out.println("change layout to activity");
                calendarAPI();
            }
        });


        //backButton 初始化
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIT = new Intent();
                backIT.setClass(Guest.this,ZenboDialogSample.class);
                startActivity(backIT);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // close faical
        robotAPI.robot.setExpression(RobotFace.HIDEFACE);

        // jump dialog domain
        robotAPI.robot.jumpToPlan(DOMAIN, "lanuchHelloWolrd_Plan");

        // show hint
        mTextView.setText(getResources().getString(R.string.dialog_example));
    }



    public static void doAnswerStuff(String answer){
        resAnswer = answer;
        System.out.println("already in doanswerstuff");
        if(resAnswer == null){
            System.out.println("the answer is null");
            return;
        }
        //判斷回傳的答案是哪種類別並且作適當的回答
        System.out.println("you can say the answer");
        //int sayNum = robotAPI.robot.speakAndListen(resAnswer, new SpeakConfig().timeout(8));
        int sayNum = robotAPI.robot.speak(resAnswer);
        resAnswer = null;
    }


    public void changeToBookList(){
        Intent it = new Intent();
        it.putExtra("resJson",resJson.toString());
        it.setClass(Guest.this,BookList.class);
        startActivity(it);
    }

    public void changeToFacility(){
        Intent facIt = new Intent();
        facIt.putExtra("resJson",resJson.toString());
        facIt.setClass(Guest.this,EquipmentIntro.class);
        startActivity(facIt);
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

    public void calendarAPI(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running calendar");
                String uniqueID = UUID.randomUUID().toString();
                String rawData = "{\"session_id\":\""+ uniqueID +"\"}";
                String charset = "UTF-8";
                System.out.println("calendar request: "+rawData);

                URLConnection connection = null;
                try {
                    connection = new URL(calendarUrl).openConnection();
                } catch (Exception e) {
                    System.out.println("calendar connection failed");
                    e.printStackTrace();
                }
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
                try (OutputStream output = connection.getOutputStream()) {
                    Log.d("calendar output format",output.toString());
                    output.write(rawData.getBytes(charset));
                } catch (Exception e) {
                    System.out.println("error in receiving calendar result");
                    e.printStackTrace();
                }
                InputStream response = null;
                try {
                    response = connection.getInputStream();
                    System.out.println("receiving calendar_result : "+response.toString() + " " + response);
                } catch (Exception e) {
                    System.out.println("error in receiving calendar_result");
                    e.printStackTrace();
                }
                try {
                    System.out.println("start translate calendar_result: ");
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
                    System.out.println("response calendar_info: "+text);
                    JSONObject resJson;
                    resJson = new JSONObject(text);
                    System.out.println("calendar_resJson: "+ resJson);

                    rescurrentDate = resJson.getString("current_date");
                    resfirstWeek = resJson.getString("first_week_calendar");
                    ressecWeek = resJson.getString("second_week_calendar");
                    System.out.println("response calendar_info: "+rescurrentDate +" "+resfirstWeek+ " "+ ressecWeek);
                } catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("error in translate calendar_info");
                }


                guest.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("切換到Activity");
                        Intent calendarIt = new Intent();
                        calendarIt.putExtra("rescurrentData",rescurrentDate);
                        calendarIt.putExtra("resfirstWeek",resfirstWeek);
                        calendarIt.putExtra("ressecWeek",ressecWeek);
                        calendarIt.setClass(Guest.this,Activity.class);
                        startActivity(calendarIt);

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
            //robotAPI.vision.cancelDetectFace();
            String text;
            final String[] resClass = new String[1];
            final String question;
            try {
                text = "onResult: " + jsonObject.getJSONObject("event_slu_query").getString("user_utterance");
                String str[] = text.split(",");
                List<String> al = new ArrayList<String>();
                al = Arrays.asList(str);
                int end = al.get(1).lastIndexOf("]");
                question = al.get(1).substring(11, end - 3);
                Log.d(TAG, "my question is : " + question);
                Log.d(TAG, "start connect");


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("start runningggggg");
                        String uniqueID = UUID.randomUUID().toString();
                        String rawData = "{\"question\":\""+question+"\",\"session_id\":\""+uniqueID+"\"}";
                        Log.d("show raw Data: ",rawData);
                        String charset = "UTF-8";

                        URLConnection connection = null;
                        try {
                            connection = new URL(targetUrl).openConnection();
                        } catch (Exception e) {
                            Log.d(TAG,"connection failed");
                            e.printStackTrace();
                        }
                        connection.setDoOutput(true); // Triggers POST.
                        connection.setRequestProperty("Accept-Charset", charset);
                        connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
                        try (OutputStream output = connection.getOutputStream()) {
                            Log.d("output format",output.toString());
                            output.write(rawData.getBytes(charset));
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("fail to receive response");
                        }
                        InputStream response = null;
                        try {
                            response = connection.getInputStream();
                            System.out.println("receiving : "+response.toString());
                        } catch (Exception e) {
                            System.out.println("error in receiving");
                            e.printStackTrace();
                        }

                        try {
                            System.out.println("start translate:(65536)");
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
                            System.out.println("response text: "+text);
                            resJson = new JSONObject(text);
                            resClass[0] = resJson.getString("class");
                            System.out.println("resClass: "+ resClass[0]);
                        } catch (IOException | JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            System.out.println("error in tell class");
                        }

                        guest.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(resClass[0].equals("answer")){
                                    System.out.println("enter class answer");
                                    String answer = null;
                                    try {
                                        answer = resJson.getString("answer");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        System.out.println("get String error");
                                    }
                                    System.out.println("receive response is : " + answer);
                                    //this.answer = answer;
                                    guest.doAnswerStuff(answer);
                                }else if(resClass[0].equals("book_list")){
                                    System.out.println("切換頁面到booklist");
                                    guest.changeToBookList();

                                }else if(resClass[0].equals("facility")){
                                    System.out.println("change to facility layout");
                                    guest.changeToFacility();
                                }
                            }
                        });
                    }
                }).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };


    public Guest() {
        super(robotCallback, robotListenCallback);
    }


}