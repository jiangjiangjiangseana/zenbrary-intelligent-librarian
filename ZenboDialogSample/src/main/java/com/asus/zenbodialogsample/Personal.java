package com.asus.zenbodialogsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.sql.Array;
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

public class Personal extends RobotActivity{
//public class Personal extends AppCompatActivity {

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    static String targetUrl;
    public static String  resAnswer;
    static JSONObject resJson;
    static String day;
    public String calendarUrl = "http://140.119.19.18:5000/api/v1/calendar/";
    private static TextView mTextView;
    static String rescurrentDate;
    static String resfirstWeek;
    static String ressecWeek;
    static Personal personal;
    static boolean isLogin;
    static ArrayList<String> uu_list_mms_id = new ArrayList<>();
    static ArrayList<String> uu_list_book_name = new ArrayList<>();
    static ArrayList<String> uu_list_book_author = new ArrayList<>();
    static ArrayList<String> uu_list_cover = new ArrayList<>();
    static ArrayList<String> top_ten_mms_id = new ArrayList<>();
    static ArrayList<String> top_ten_book_name = new ArrayList<>();
    static ArrayList<String> top_ten_book_author = new ArrayList<>();
    static ArrayList<String> top_ten_cover = new ArrayList<>();
    static ArrayList<ArrayList> browse_log = new ArrayList<>();
    static String u_id;
    static String user_name;
    static String email;
    static String uu_list;
    static String change_intent;
    static String resBookTopTen;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        System.out.println("success to change to user");
        personal = Personal.this;
        Intent userIt = this.getIntent();
        change_intent = userIt.getStringExtra("change");
        user_name = userIt.getStringExtra("user_name");
        u_id = userIt.getStringExtra("u_id");
        email = userIt.getStringExtra("email");
        uu_list = userIt.getStringExtra("uu_list");
        robotAPI.robot.speak("哈囉!"+user_name);
        if(change_intent.equals("book")){
            //undo:::::::
        }
        List<String> temp = new ArrayList<String>(Arrays.asList(uu_list.split("#@")));
        if(!uu_list.equals("null")) {
            System.out.println("start to translate uu_list");
            for (int i = 0; i < temp.size(); i++) {
                System.out.println("uu_list book: " + temp.get(i));
                String book = temp.get(i);
                int index1 = book.indexOf("@@");
                int index2 = book.indexOf("@#");
                int index3 = book.indexOf("##");
                String mms_id = book.substring(0, index1);
                String name = book.substring(index1 + 2, index2);
                String author = book.substring(index2 + 2, index3 - 1);
                String cover = book.substring(index3 + 2);
                uu_list_mms_id.add(mms_id);
                uu_list_book_name.add(name);
                uu_list_book_author.add(author);
                uu_list_cover.add(cover);
            }
        }else{
            System.out.println("no uu_list");
        }

        System.out.println("list of mms_id: "+uu_list_mms_id);
        System.out.println("list of book_name: "+uu_list_book_name);
        System.out.println("list of book_author: "+uu_list_book_author);
        System.out.println("list of cover: "+uu_list_cover);


        isLogin = true;
        System.out.println("user uu_list: "+uu_list);

        //user_info show on app
        TextView user_id_tv = (TextView) findViewById(R.id.user_id);
        TextView user_email_tv = (TextView) findViewById(R.id.user_email);
        TextView user_name_tv = (TextView) findViewById(R.id.user_name);
        user_id_tv.setText("學號: "+u_id);
        user_email_tv.setText("信箱: \n"+email);
        user_name_tv.setText("姓名: "+user_name);


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

        //logoutButton初始化
        Button logoutBt = (Button) findViewById(R.id.logoutBtn);
        logoutBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //undo: 清空user資訊，上船瀏覽資料，寫成一個function，因為超過時間也要這樣做



                    isLogin = false;
                    Intent backIT = new Intent();
                    backIT.setClass(Personal.this,ZenboDialogSample.class);
                    startActivity(backIT);
                }
            });


        //recommendBtn初始化
        Button recommendBtn = findViewById(R.id.recommendBtn);
        recommendBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotAPI.vision.cancelDetectFace();
                robotAPI.robot.speak("請稍等");
                requestTopTen();
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


    public void requestTopTen(){
        final String topTenUrl = "http://140.119.19.18:5000/api/v1/book_top_ten/";
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running top ten");
                String uniqueID = UUID.randomUUID().toString();
                String rawData = "{\"session_id\":\""+uniqueID+"\"}";
                String charset = "UTF-8";
                System.out.println("top ten request: "+rawData);

                URLConnection connection = null;
                try {
                    connection = new URL(topTenUrl).openConnection();
                } catch (Exception e) {
                    System.out.println("top ten connection failed");
                    e.printStackTrace();
                }
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
                try (OutputStream output = connection.getOutputStream()) {
                    Log.d("top ten output format",output.toString());
                    output.write(rawData.getBytes(charset));
                } catch (Exception e) {
                    System.out.println("error in receiving top ten result");
                    e.printStackTrace();
                }
                InputStream response = null;
                try {
                    response = connection.getInputStream();
                    System.out.println("receiving top ten_result : "+response.toString() + " " + response);
                } catch (Exception e) {
                    System.out.println("error in receiving top ten_result");
                    e.printStackTrace();
                }
                try {
                    System.out.println("start translate top ten_result: ");
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
                    System.out.println("response top ten_info: "+text);
                    JSONObject resJson;
                    resJson = new JSONObject(text);
                    System.out.println("top ten_resJson: "+ resJson);
                    resBookTopTen = resJson.getString("book_top_ten");
                    System.out.println("response book_top_ten_info: "+resBookTopTen);
                } catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("error in translate book_top_ten_info");
                }
                if(!resBookTopTen.equals("null")) {
                    List<String> temp = new ArrayList<String>(Arrays.asList(resBookTopTen.split("#@")));
                    System.out.println("start to translate book_top_ten");
                    for (int i = 0; i < temp.size(); i++) {
                        System.out.println("book top ten: " + temp.get(i));
                        String book = temp.get(i);
                        int index1 = book.indexOf("@@");
                        int index2 = book.indexOf("@#");
                        int index3 = book.indexOf("##");
                        String mms_id = book.substring(0, index1);
                        String name = book.substring(index1 + 2, index2);
                        String author = book.substring(index2 + 2, index3 - 1);
                        String cover = book.substring(index3 + 2);
                        top_ten_mms_id.add(mms_id);
                        top_ten_book_name.add(name);
                        top_ten_book_author.add(author);
                        top_ten_cover.add(cover);

                    }
                }else{
                    System.out.println("no top_ten");
                }
                System.out.println("top ten cover: "+top_ten_cover);


                personal.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recommend_alert();
                    }
                });
            }
        }).start();

    }

    public void recommend_alert(){
        System.out.println("trigger recommend_alert");
        //inflate目的是把自己設計xml的Layout轉成View，作用類似於findViewById，它用於一個沒有被載入或者想要動態
        //載入的介面，當被載入Activity後才可以用findViewById來獲得其中界面的元素
        LayoutInflater inflater = LayoutInflater.from(Personal.this);
        final View v = inflater.inflate(R.layout.activity_recommend, null);

        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = null;
                drawable = v.getResources().getDrawable(
                        Integer.parseInt(source)
                );
                drawable.setBounds(0,0,120,100);
                return drawable;

            }
        };

        //語法一：new AlertDialog.Builder(主程式類別).XXX.XXX.XXX;
        final AlertDialog dialog = new AlertDialog.Builder(Personal.this)
                .setTitle("我的推薦")
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 1000;
        lp.height = 700;
        dialog.getWindow().setAttributes(lp);
        robotAPI.robot.speak("這些是熱門書籍");
            System.out.println("start to change recommendation top ten book list.");
            ImageView imageview1 = dialog.findViewById(R.id.imageview1);
            ImageView imageview2 = dialog.findViewById(R.id.imageview2);
            ImageView imageview3 = dialog.findViewById(R.id.imageview3);
            ImageView imageview4 = dialog.findViewById(R.id.imageview4);
            ImageView imageview5 = dialog.findViewById(R.id.imageview5);
            ImageView imageview6 = dialog.findViewById(R.id.imageview6);
            ImageView imageview7 = dialog.findViewById(R.id.imageview7);
            ImageView imageview8 = dialog.findViewById(R.id.imageview8);
            ImageView imageview9 = dialog.findViewById(R.id.imageview9);
            ImageView imageview10 = dialog.findViewById(R.id.imageview10);
            TextView tv1 = dialog.findViewById(R.id.textview1);
            TextView tv2 = dialog.findViewById(R.id.textview2);
            TextView tv3 = dialog.findViewById(R.id.textview3);
            TextView tv4 = dialog.findViewById(R.id.textview4);
            TextView tv5 = dialog.findViewById(R.id.textview5);
            TextView tv6 = dialog.findViewById(R.id.textview6);
            TextView tv7 = dialog.findViewById(R.id.textview7);
            TextView tv8 = dialog.findViewById(R.id.textview8);
            TextView tv9 = dialog.findViewById(R.id.textview9);
            TextView tv10 = dialog.findViewById(R.id.textview10);
            new Personal.DownloadImageTask(imageview1)
                    .execute(top_ten_cover.get(0));
            new Personal.DownloadImageTask(imageview2)
                    .execute(top_ten_cover.get(1));
            new Personal.DownloadImageTask(imageview3)
                    .execute(top_ten_cover.get(2));
            new Personal.DownloadImageTask(imageview4)
                    .execute(top_ten_cover.get(3));
            new Personal.DownloadImageTask(imageview5)
                    .execute(top_ten_cover.get(4));
            new Personal.DownloadImageTask(imageview6)
                    .execute(top_ten_cover.get(5));
            new Personal.DownloadImageTask(imageview7)
                    .execute(top_ten_cover.get(6));
            new Personal.DownloadImageTask(imageview8)
                    .execute(top_ten_cover.get(7));
            new Personal.DownloadImageTask(imageview9)
                    .execute(top_ten_cover.get(8));
            new Personal.DownloadImageTask(imageview10)
                    .execute(top_ten_cover.get(9));
            tv1.setText(top_ten_book_name.get(0));
            tv2.setText(top_ten_book_name.get(1));
            tv3.setText(top_ten_book_name.get(2));
            tv4.setText(top_ten_book_name.get(3));
            tv5.setText(top_ten_book_name.get(4));
            tv6.setText(top_ten_book_name.get(5));
            tv7.setText(top_ten_book_name.get(6));
            tv8.setText(top_ten_book_name.get(7));
            tv9.setText(top_ten_book_name.get(8));
            tv10.setText(top_ten_book_name.get(9));

        //hotBtn初始化
        Button hotBtn = dialog.findViewById(R.id.hotBtn);
        hotBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!resBookTopTen.equals("null")) {
                    System.out.println("start to change recommendation top ten book list.");
                    ImageView imageview1 = dialog.findViewById(R.id.imageview1);
                    ImageView imageview2 = dialog.findViewById(R.id.imageview2);
                    ImageView imageview3 = dialog.findViewById(R.id.imageview3);
                    ImageView imageview4 = dialog.findViewById(R.id.imageview4);
                    ImageView imageview5 = dialog.findViewById(R.id.imageview5);
                    ImageView imageview6 = dialog.findViewById(R.id.imageview6);
                    ImageView imageview7 = dialog.findViewById(R.id.imageview7);
                    ImageView imageview8 = dialog.findViewById(R.id.imageview8);
                    ImageView imageview9 = dialog.findViewById(R.id.imageview9);
                    ImageView imageview10 = dialog.findViewById(R.id.imageview10);
                    TextView tv1 = dialog.findViewById(R.id.textview1);
                    TextView tv2 = dialog.findViewById(R.id.textview2);
                    TextView tv3 = dialog.findViewById(R.id.textview3);
                    TextView tv4 = dialog.findViewById(R.id.textview4);
                    TextView tv5 = dialog.findViewById(R.id.textview5);
                    TextView tv6 = dialog.findViewById(R.id.textview6);
                    TextView tv7 = dialog.findViewById(R.id.textview7);
                    TextView tv8 = dialog.findViewById(R.id.textview8);
                    TextView tv9 = dialog.findViewById(R.id.textview9);
                    TextView tv10 = dialog.findViewById(R.id.textview10);
                    new Personal.DownloadImageTask(imageview1)
                            .execute(top_ten_cover.get(0));
                    new Personal.DownloadImageTask(imageview2)
                            .execute(top_ten_cover.get(1));
                    new Personal.DownloadImageTask(imageview3)
                            .execute(top_ten_cover.get(2));
                    new Personal.DownloadImageTask(imageview4)
                            .execute(top_ten_cover.get(3));
                    new Personal.DownloadImageTask(imageview5)
                            .execute(top_ten_cover.get(4));
                    new Personal.DownloadImageTask(imageview6)
                            .execute(top_ten_cover.get(5));
                    new Personal.DownloadImageTask(imageview7)
                            .execute(top_ten_cover.get(6));
                    new Personal.DownloadImageTask(imageview8)
                            .execute(top_ten_cover.get(7));
                    new Personal.DownloadImageTask(imageview9)
                            .execute(top_ten_cover.get(8));
                    new Personal.DownloadImageTask(imageview10)
                            .execute(top_ten_cover.get(9));
                    tv1.setText(top_ten_book_name.get(0));
                    tv2.setText(top_ten_book_name.get(1));
                    tv3.setText(top_ten_book_name.get(2));
                    tv4.setText(top_ten_book_name.get(3));
                    tv5.setText(top_ten_book_name.get(4));
                    tv6.setText(top_ten_book_name.get(5));
                    tv7.setText(top_ten_book_name.get(6));
                    tv8.setText(top_ten_book_name.get(7));
                    tv9.setText(top_ten_book_name.get(8));
                    tv10.setText(top_ten_book_name.get(9));
                }else{
                    System.out.println("has no top_ten");
                }

            }
        });

        //uuBtn初始化
        Button uuBtn = dialog.findViewById(R.id.uuBtn);
        uuBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!uu_list.equals("null")) {
                    System.out.println("start to change recommendation book list.");
                    ImageView imageview1 = dialog.findViewById(R.id.imageview1);
                    ImageView imageview2 = dialog.findViewById(R.id.imageview2);
                    ImageView imageview3 = dialog.findViewById(R.id.imageview3);
                    ImageView imageview4 = dialog.findViewById(R.id.imageview4);
                    ImageView imageview5 = dialog.findViewById(R.id.imageview5);
                    ImageView imageview6 = dialog.findViewById(R.id.imageview6);
                    ImageView imageview7 = dialog.findViewById(R.id.imageview7);
                    ImageView imageview8 = dialog.findViewById(R.id.imageview8);
                    ImageView imageview9 = dialog.findViewById(R.id.imageview9);
                    ImageView imageview10 = dialog.findViewById(R.id.imageview10);
                    TextView tv1 = dialog.findViewById(R.id.textview1);
                    TextView tv2 = dialog.findViewById(R.id.textview2);
                    TextView tv3 = dialog.findViewById(R.id.textview3);
                    TextView tv4 = dialog.findViewById(R.id.textview4);
                    TextView tv5 = dialog.findViewById(R.id.textview5);
                    TextView tv6 = dialog.findViewById(R.id.textview6);
                    TextView tv7 = dialog.findViewById(R.id.textview7);
                    TextView tv8 = dialog.findViewById(R.id.textview8);
                    TextView tv9 = dialog.findViewById(R.id.textview9);
                    TextView tv10 = dialog.findViewById(R.id.textview10);
                    new Personal.DownloadImageTask(imageview1)
                            .execute(uu_list_cover.get(0));
                    new Personal.DownloadImageTask(imageview2)
                            .execute(uu_list_cover.get(1));
                    new Personal.DownloadImageTask(imageview3)
                            .execute(uu_list_cover.get(2));
                    new Personal.DownloadImageTask(imageview4)
                            .execute(uu_list_cover.get(3));
                    new Personal.DownloadImageTask(imageview5)
                            .execute(uu_list_cover.get(4));
                    new Personal.DownloadImageTask(imageview6)
                            .execute(uu_list_cover.get(5));
                    new Personal.DownloadImageTask(imageview7)
                            .execute(uu_list_cover.get(6));
                    new Personal.DownloadImageTask(imageview8)
                            .execute(uu_list_cover.get(7));
                    new Personal.DownloadImageTask(imageview9)
                            .execute(uu_list_cover.get(8));
                    new Personal.DownloadImageTask(imageview10)
                            .execute(uu_list_cover.get(9));
                    tv1.setText(uu_list_book_name.get(0));
                    tv2.setText(uu_list_book_name.get(1));
                    tv3.setText(uu_list_book_name.get(2));
                    tv4.setText(uu_list_book_name.get(3));
                    tv5.setText(uu_list_book_name.get(4));
                    tv6.setText(uu_list_book_name.get(5));
                    tv7.setText(uu_list_book_name.get(6));
                    tv8.setText(uu_list_book_name.get(7));
                    tv9.setText(uu_list_book_name.get(8));
                    tv10.setText(uu_list_book_name.get(9));
                }else{
                    System.out.println("has no uu_list");
                    robotAPI.robot.speak("尚無推薦唷!請多登入查詢書籍!");
                }

            }
        });

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
        it.putExtra("state","login");
        it.putExtra("user_name",user_name);
        it.putExtra("u_id",u_id);
        it.putExtra("email",email);
        it.putExtra("uu_list",uu_list);
        it.setClass(Personal.this,BookList.class);
        startActivity(it);
    }

    public void changeToFacility(){
        Intent facIt = new Intent();
        facIt.putExtra("resJson",resJson.toString());
        facIt.setClass(Personal.this,EquipmentIntro.class);
        startActivity(facIt);
    }

    public void calendarAPI(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running calendar");
                String uniqueID = UUID.randomUUID().toString();
                day = "0";
                String rawData = "{\"day\":\""+day+"\",\"session_id\":\""+uniqueID+"\"}";
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


                personal.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("切換到Activity");
                        Intent calendarIt = new Intent();
                        calendarIt.putExtra("rescurrentData",rescurrentDate);
                        calendarIt.putExtra("resfirstWeek",resfirstWeek);
                        calendarIt.putExtra("ressecWeek",ressecWeek);
                        calendarIt.setClass(Personal.this,Activity.class);
                        startActivity(calendarIt);

                    }
                });
            }
        }).start();}



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //轉url圖片的class
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
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

                        personal.runOnUiThread(new Runnable() {
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
                                    personal.doAnswerStuff(answer);
                                }else if(resClass[0].equals("book_list")){
                                    System.out.println("切換頁面到booklist");
                                    personal.changeToBookList();

                                }else if(resClass[0].equals("facility")){
                                    System.out.println("change to facility layout");
                                    personal.changeToFacility();
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


    public Personal() {
        super(robotCallback, robotListenCallback);
    }

}
