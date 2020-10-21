package com.asus.zenbodialogsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
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

public class BookList extends RobotActivity{

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    private static TextView mTextView;
    private ListView listView;
    static String bookUrl = "http://140.119.19.18:5000/api/v1/book/";
    static BookList bookListClass;
    static String bookName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        listView = (ListView) findViewById(R.id.bookList);
        bookListClass = BookList.this;
        Intent it = this.getIntent();
        String resJsonString = it.getStringExtra("resJson");
        try {
            JSONObject resJson = new JSONObject(resJsonString);
            JSONArray bookListJson = resJson.getJSONArray("book_list");
            final ArrayList<String> bookList = new ArrayList<String>();
            if (bookListJson != null) {
                for (int i = 0; i < bookListJson.length(); i++) {
                    bookList.add(bookListJson.getString(i));
                }
            }
            if (bookList.isEmpty()) {
                Intent backIT = new Intent();
                backIT.setClass(BookList.this,ZenboDialogSample.class);
                startActivity(backIT);
                robotAPI.robot.speakAndListen("不好意思，請重複一次問題",new SpeakConfig().timeout(15));
            } else {
                final ArrayList<String> booknameList = new ArrayList<String>();
                for (int i = 0; i < bookList.size(); i++) {
                    String text = bookList.get(i);
                    String[] textList = text.split(",");
                    System.out.println(textList[0].substring(2, textList[0].length() - 1));
                    booknameList.add(textList[0].substring(2, textList[0].length() - 1));
                }
                ;
                System.out.println("Book name list: " + booknameList);
                ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, booknameList);
                listView.setAdapter(adapter);
                robotAPI.robot.speak("請點選你想要的書籍!");

            //設定listview的listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(BookList.this,"點選第 "+(i +1) +" 個 \n內容："+booknameList.get(i), Toast.LENGTH_SHORT).show();
                    bookName = bookList.get(i);
                }
            });

            }
            } catch(JSONException e){
                e.printStackTrace();
                System.out.println("error in listing book");
            }

        //返回鍵初始化
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIT = new Intent();
                backIT.setClass(BookList.this,ZenboDialogSample.class);
                startActivity(backIT);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //start search a book
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running");
                String uniqueID = UUID.randomUUID().toString();
                String rawData = "{\"question\":\""+bookName+"\",\"session_id\":\""+uniqueID+"\"}";
                String charset = "UTF-8";

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
                    System.out.println("receiving : "+response.toString());
                } catch (Exception e) {
                    System.out.println("error in receiving");
                    e.printStackTrace();
                }



                bookListClass.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                            System.out.println("切換頁面到Book");
                            Intent bookIt = new Intent();
                            //loginIt.putExtra("resJson",resJson.toString());
                            bookIt.setClass(BookList.this,Book.class);
                            startActivity(bookIt);


                    }
                });
            }
        }).start();
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


    public BookList() {
        super(robotCallback, robotListenCallback);
    }

}
