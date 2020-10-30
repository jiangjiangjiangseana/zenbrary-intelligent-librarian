package com.asus.zenbodialogsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
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

public class Book extends RobotActivity{

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    static Book bookClass;
    static String bookUrl = "http://140.119.19.18:5000/api/v1/book/";
    static String resrecAuthor ;
    static String resrecBookName ;
    static String resrecLocandAvai ;
    static String resrecRecommendation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        robotAPI.vision.cancelDetectFace();
        System.out.println("sucess to change to book");
        setContentView(R.layout.activity_book);
        bookClass = Book.this;
        Intent bookIt = this.getIntent();
        String resAuthor = bookIt.getStringExtra("resAuthor");
        String resBookName = bookIt.getStringExtra("resBookName");
        String resLocandAvai = bookIt.getStringExtra("resLocandAvai");
        String resRecommendation = bookIt.getStringExtra("resRecommendation");
        System.out.println("book_info receive: "+resAuthor+resBookName+resLocandAvai+ resLocandAvai.getClass()+ " "+resRecommendation);
        //(todo)處理loca and avai,讓他分開
        //change string to arraylist
        List<String> localAndAvai = new ArrayList<String>(Arrays.asList(resLocandAvai.split("]")));
        System.out.println("split result: "+ localAndAvai);
        List<String> loca_info = new ArrayList<String>();
        List<String> avai_info = new ArrayList<String>();
        for(int i=0;i<localAndAvai.size();i++){
            System.out.println("loc第"+i+"組: "+localAndAvai.get(i).substring(2));
            String couple = localAndAvai.get(i).substring(2);
            List<String> temp = new ArrayList<String>(Arrays.asList(couple.split(",")));
            loca_info.add(temp.get(0));
            avai_info.add(temp.get(1));
        }
        System.out.println("local: "+loca_info);
        System.out.println("avai:" +avai_info);

        System.out.println("recommendation: "+resRecommendation+resRecommendation.getClass());
        final List<String> recommendation = new ArrayList<String>(Arrays.asList(resRecommendation.split("@@")));
        System.out.println("recommendation result: "+ recommendation);
        final List<String> recommendation_bookName = new ArrayList<String>();
        final List<String> recommendation_number = new ArrayList<String>();
        for(int i=0;i<recommendation.size();i++){
            System.out.println("recommendation第"+i+"組: "+recommendation.get(i));
            String reco_couple = recommendation.get(i);
            List<String> temp = new ArrayList<String>(Arrays.asList(reco_couple.split("##")));
            recommendation_bookName.add(temp.get(0));
            recommendation_number.add(temp.get(1));
        }
        System.out.println("reco_bookName: "+recommendation_bookName);
        System.out.println("reco_number:" +recommendation_number);


        TextView bookName = (TextView)findViewById(R.id.bookName);
        TextView authorName = (TextView)findViewById(R.id.authorName);
        TextView location = (TextView)findViewById(R.id.location);
        TextView available = (TextView)findViewById(R.id.available);
        TextView introduce = (TextView)findViewById(R.id.introduce);

        bookName.setText("書名: "+resBookName);
        authorName.setText("作者: "+resAuthor);
        location.setText("位置: "+ loca_info);
        available.setText("狀態: "+avai_info);
        boolean avai = false;
        String loc = "";
        for(int i=0;i<avai_info.size();i++){
            System.out.println("substring: "+avai_info.get(i));
            String state = avai_info.get(i);
            System.out.println("on the shelf"+ state.equals("\"available\""));
            if(state.equals("\"available\"")){
               avai = true;
               loc += loca_info.get(i);
            }
        }
        if(avai==true) {
            robotAPI.robot.speak("這本書現在在" + loc + "唷");
        }else{
            robotAPI.robot.speak("這本書現在不再各圖書館內唷");
        }
        //推薦清單initial
        ListView recommendList = (ListView)findViewById(R.id.recommendList);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recommendation_bookName);
        recommendList.setAdapter(adapter);
        recommendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                     Toast.makeText(Book.this, "點選第 " + (i + 1) + " 個 \n" + recommendation_bookName.get(i), Toast.LENGTH_SHORT).show();
                                                     String recommendationName = recommendation_bookName.get(i);
                                                     System.out.println("1111111");
                                                     //change string to arraylist
                                                     String recommendationNumber = recommendation_number.get(i);
                                                     System.out.println("recommendation_Name: " + recommendationName.getClass() + "  " + recommendationNumber);
                                                     requestBook(recommendationNumber);
                                                 }
                                             });

        //backButton 初始化
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIT = new Intent();
                backIT.setClass(Book.this,ZenboDialogSample.class);
                startActivity(backIT);
            }
        });


        //backButton 初始化
        Button hashButton = findViewById(R.id.hashtagBtn);
        hashButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashtag_alert();
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

    public void hashtag_alert(){

        //inflate目的是把自己設計xml的Layout轉成View，作用類似於findViewById，它用於一個沒有被載入或者想要動態

        //載入的介面，當被載入Activity後才可以用findViewById來獲得其中界面的元素

        LayoutInflater inflater = LayoutInflater.from(Book.this);
        final View v = inflater.inflate(R.layout.activity_hashtag, null);

        //語法一：new AlertDialog.Builder(主程式類別).XXX.XXX.XXX;
        AlertDialog dialog = new AlertDialog.Builder(Book.this)
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
                        Toast.makeText(Book.this, "我還尚未了解",Toast.LENGTH_SHORT).show();
                    }

                })
                .setNeutralButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(Book.this, "取消",Toast.LENGTH_SHORT).show();
                    }

                })
                .show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.show();
    }

    public void requestBook(final String mms_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running");
                String uniqueID = UUID.randomUUID().toString();
                String rawData = "{\"mms_id\":\""+ mms_id +"\",\"session_id\":\""+uniqueID+"\"}";
                String charset = "UTF-8";
                System.out.println("book info request: "+rawData);

                URLConnection connection = null;
                try {
                    connection = new URL(bookUrl).openConnection();
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
                    System.out.println("receiving book_info : "+response.toString() + " " + response);
                } catch (Exception e) {
                    System.out.println("error in receiving book_info");
                    e.printStackTrace();
                }
                try {
                    System.out.println("start translate book_info: ");
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
                    System.out.println("response book_info: "+text);

                    JSONObject resJson;
                    resJson = new JSONObject(text);
                    System.out.println("resJson: "+ resJson);

                    resrecAuthor = resJson.getString("author");
                    resrecBookName = resJson.getString("book_name");
                    resrecLocandAvai = resJson.getString("location_and_available");
                    resrecRecommendation = resJson.getString("recommendation");
                    System.out.println("response chinese: "+resrecAuthor +" "+resrecBookName+ " "+ resrecLocandAvai+ " "+ resrecRecommendation);
                } catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("error in translate book_info");
                }


                bookClass.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        System.out.println("切換頁面到Book");
                        Intent bookIt = new Intent();
                        bookIt.putExtra("resAuthor",resrecAuthor);
                        bookIt.putExtra("resBookName",resrecBookName);
                        bookIt.putExtra("resLocandAvai",resrecLocandAvai.toString());
                        bookIt.putExtra("resRecommendation",resrecRecommendation.toString());
                        bookIt.setClass(Book.this,Book.class);
                        startActivity(bookIt);


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


    public Book() {
        super(robotCallback, robotListenCallback);
    }

}
