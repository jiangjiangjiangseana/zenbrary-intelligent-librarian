package com.asus.zenbodialogsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zenbo_dialog_sample);
        zenboDialogSample = ZenboDialogSample.this;
        mTextView = (TextView) findViewById(R.id.textview_info);
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
        // bookButton 初始化
        Button bButton = findViewById(R.id.bookButton);
        bButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetUrl = "http://140.119.19.18:5000/api/v1/book_list/";
                int aaaab = robotAPI.robot.speakAndListen("您想找什麼書呢?", new SpeakConfig().timeout(15));
            }
        });
        // equipmentButton 初始化
        Button eButton = findViewById(R.id.equipmentButton);
        eButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("change layout to equip");
                Intent equipIt = new Intent();
                //equipIt.putExtra("resJson",resJson.toString());
                equipIt.setClass(ZenboDialogSample.this,EquipmentIntro.class);
                startActivity(equipIt);
            }
        });
        // questionButton 初始化
        Button qButton = findViewById(R.id.questionButton);
        qButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetUrl = "http://140.119.19.18:5000/api/v1/other/";
                int aaaab = robotAPI.robot.speakAndListen("有什麼是我能幫你的嗎?", new SpeakConfig().timeout(15));
            }
        });
        // activityButton 初始化
        Button aButton = findViewById(R.id.activityButton);
        aButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("change layout to activity");
                Intent activityIt = new Intent();
                //equipIt.putExtra("resJson",resJson.toString());
                activityIt.setClass(ZenboDialogSample.this,Activity.class);
                startActivity(activityIt);
            }
        });
        // guidedButton 初始化
        Button gButton = findViewById(R.id.guidedButton);
        gButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("change layout to guided");
                Intent guidedIt = new Intent();
                //equipIt.putExtra("resJson",resJson.toString());
                guidedIt.setClass(ZenboDialogSample.this,Tour.class);
                startActivity(guidedIt);
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

        // listen user utterance
//        int abc = robotAPI.robot.speak("哈囉各位！我是 Zenbo！同時也是位聰明的圖書館館員唷！"+
//                  "達賢圖書館是我工作的地方！"+
//                  "，隨著資訊科技的迅速發展，達賢圖" +
//                  "書館，以提供資訊服務為主要理念，開創符合時代趨勢的科技化圖書館，並在 2020 年" +
//                  "正式開幕，是政治大學最新成立的圖書館唷！來看看我平常都在做哪些事吧！");


//        int second = robotAPI.robot.speak("首先是智慧問答！\n" +
//                "當使用者遇到問題時，只需要向我走來，並\n" +
//                "說出問題，我便會給予適當的答案!有了我，讓圖書查詢以及設備介紹更有效率，\n" +
//                "透過最新的NLP技術，以及厲害的 BERT 模型，我能精準的分析讀者的\n" +
//                "意圖，並提供最迅速且相關的回答！\n"
//                );
//
//        int third = robotAPI.robot.speak("平常我也會擔任導覽員的絞色，比起枯燥無味的介紹，\n" +
//                "透過我可愛的外型、活潑的因掉，及讀者對科技的好奇，我會帶領各位翱游答閒" +
//                "圖書館，這個衝滿科技與美感的閱讀世界唷!");
//
//        int forth = robotAPI.robot.speak("此外，各位是否常不知道字幾想看什麼書呢？" +
//                "透過 USER BASE 與 iTEM BASE 機制，我會創造你最可能感姓趣的書籍推薦。" +
//                "根據你的喜好，尋找與" +
//                "你相像的讀者，推薦他們閱讀過的書，也會根據你過去借閱的書，找出" +
//                "相似度最高的圖書推薦給你唷。");

//           int fifth = robotAPI.robot.speak("除了圖書館單芳麵的推薦，我還有紀錄評論的工作！讀者在\n" +
//                   "閱讀完書籍後可以直接給予書籍形容詞標籤\n" +
//                   " ，讓書籍之間有更多關聯性，也可以藉由這些標籤達到\n" +
//                   "多樣化的搜尋，推薦給你更多你可能有興趣的圖書清單唷！");
//
//           int sixth = robotAPI.robot.speak("最後，聰明的我能夠同步圖書館的活動資訊，將豐富的活動資源都讓各位知道，不讓各位\n" +
//                   "漏掉任何依個可能感興趣的活動唷！\n" +
//                   "同時，也可以根據活動資訊來選擇適合的閱讀空間，以減少因活動受\n" +
//                   "到的乾擾。");


//            int seventh = robotAPI.robot.speak("以上就是我的工作內容，希望我的出現，能夠讓答閒圖書館更加科技化，也能夠帶給各位更優質的閱讀環境，快來找我玩吧~!");


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
        it.setClass(ZenboDialogSample.this,BookList.class);
        startActivity(it);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop listen user utterance
        robotAPI.robot.stopSpeakAndListen();

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

                            zenboDialogSample.runOnUiThread(new Runnable() {
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
                                    ZenboDialogSample.doAnswerStuff(answer);
                                }else if(resClass[0].equals("book_list")){
                                    System.out.println("切換頁面到booklist");
                                    zenboDialogSample.changeToBookList();

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


    public ZenboDialogSample() {
        super(robotCallback, robotListenCallback);
    }


}