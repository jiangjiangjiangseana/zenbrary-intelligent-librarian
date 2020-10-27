package com.asus.zenbodialogsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

public class Book extends RobotActivity{

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    static Book bookClass;


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
        System.out.println("book_info receive: "+resAuthor+resBookName+resLocandAvai+ resLocandAvai.getClass());
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
            System.out.println("on the shelf"+ state=="available");
            if(state =="\"available\""){
               avai = true;
               loc += loca_info.get(i);
            }
        }
        if(avai==true) {
            robotAPI.robot.speak("這本書現在在" + loc + "唷");
        }else{
            robotAPI.robot.speak("這本書現在不再各圖書館內唷");
        }


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
