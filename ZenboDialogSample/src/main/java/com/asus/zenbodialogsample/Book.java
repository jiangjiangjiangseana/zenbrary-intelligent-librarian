package com.asus.zenbodialogsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class Book extends RobotActivity {

    public final static String TAG = "ZenboDialogSample";
    public final static String DOMAIN = "9EF85697FF064D54B32FF06D21222BA2";
    static Book bookClass;
    static String bookUrl = "http://140.119.19.18:5000/api/v1/book/";
    static String resrecAuthor;
    static String resrecBookName;
    static String resrecLocandAvai;
    static String resrecRecommendation;
    static String resrecCover;
    static String resrecHashtag;
    static String resrecIntroduction;
    static String resrecRating;
    static float score;
    static String hashtagReview;
    static String requestMms_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        robotAPI.vision.cancelDetectFace();
        System.out.println("book create score: "+score+" "+"hashtags: "+hashtagReview);
        //undo: book hashtag book score
        System.out.println("sucess to change to book");
        setContentView(R.layout.activity_book);
        bookClass = Book.this;
        Intent bookIt = this.getIntent();
        requestMms_id = bookIt.getStringExtra("mms_id");
        String resAuthor = bookIt.getStringExtra("resAuthor");
        String resBookName = bookIt.getStringExtra("resBookName");
        String resLocandAvai = bookIt.getStringExtra("resLocandAvai");
        String resRecommendation = bookIt.getStringExtra("resRecommendation");
        String resCover = bookIt.getStringExtra("resCover");
        String resHashtag = bookIt.getStringExtra("resHashtag");
        String resRating = bookIt.getStringExtra("resRating");
        final String resIntroduction = bookIt.getStringExtra("resIntroduction");
        System.out.println("book_info sucess receive: " + resAuthor + resBookName + resCover+" "+resHashtag+" "+resIntroduction+" "+resRating );
        //(todo)處理loca and avai,讓他分開
        //change string to arraylist
        List<String> localAndAvai = new ArrayList<String>(Arrays.asList(resLocandAvai.split("]")));
        System.out.println("split result: " + localAndAvai);
        List<String> loca_info = new ArrayList<String>();
        List<String> avai_info = new ArrayList<String>();
        for (int i = 0; i < localAndAvai.size(); i++) {
            System.out.println("loc第" + i + "組: " + localAndAvai.get(i).substring(2));
            String couple = localAndAvai.get(i).substring(2);
            List<String> temp = new ArrayList<String>(Arrays.asList(couple.split(",")));
            loca_info.add(temp.get(0));
            avai_info.add(temp.get(1));
        }
        System.out.println("local: " + loca_info);
        System.out.println("avai:" + avai_info);

        System.out.println("recommendation: " + resRecommendation + resRecommendation.getClass());
        final List<String> recommendation = new ArrayList<String>(Arrays.asList(resRecommendation.split("@@")));
        System.out.println("recommendation result: " + recommendation);
        final List<String> recommendation_bookName = new ArrayList<String>();
        final List<String> recommendation_number = new ArrayList<String>();
        for (int i = 0; i < recommendation.size(); i++) {
            System.out.println("recommendation第" + i + "組: " + recommendation.get(i));
            String reco_couple = recommendation.get(i);
            List<String> temp = new ArrayList<String>(Arrays.asList(reco_couple.split("##")));
            recommendation_bookName.add(temp.get(0));
            recommendation_number.add(temp.get(1));
        }
        System.out.println("reco_bookName: " + recommendation_bookName);
        System.out.println("reco_number:" + recommendation_number);


        TextView bookName = (TextView) findViewById(R.id.bookName);
        TextView authorName = (TextView) findViewById(R.id.authorName);
        TextView location = (TextView) findViewById(R.id.location);
        TextView available = (TextView) findViewById(R.id.available);
        TextView rating = (TextView) findViewById(R.id.score_tv);

        bookName.setText("書名: " + resBookName);
        authorName.setText("作者: " + resAuthor);
        location.setText("位置: " + loca_info);
        available.setText("狀態: " + avai_info);

        rating.setText("評分: \n"+"   "+resRating);
        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                .execute(resCover);

        boolean avai = false;
        String loc = "";
        for (int i = 0; i < avai_info.size(); i++) {
            System.out.println("substring: " + avai_info.get(i));
            String state = avai_info.get(i);
            System.out.println("on the shelf" + state.equals("\"available\""));
            if (state.equals("\"available\"")) {
                avai = true;
                loc += loca_info.get(i);
            }
        }
        if (avai == true) {
            robotAPI.robot.speak("這本書現在在" + loc + "唷");
        } else {
            robotAPI.robot.speak("這本書現在不再各圖書館內唷");
        }
        //推薦清單initial
        ListView recommendList = (ListView) findViewById(R.id.recommendList);
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
                backIT.setClass(Book.this, ZenboDialogSample.class);
                startActivity(backIT);
            }
        });


        //hashButton 初始化
        Button hashButton = findViewById(R.id.hashtagBtn);
        hashButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = 0;
                hashtag_alert();
            }
        });


        //introduceButton 初始化
        Button introButton = findViewById(R.id.introduceButton);
        introButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noIntro = "暫無簡介";
                if(resIntroduction.equals("")){
                    introduce_alert(noIntro);
                }else{
                introduce_alert(resIntroduction);}
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

    public void hashtag_alert(){
        robotAPI.robot.speak("填寫hashtag請用空格隔開");
        System.out.println("hash function called");
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
                        EditText hashtag = (EditText) (v.findViewById(R.id.editText_hashtag));
                        Toast.makeText(getApplicationContext(), "Hashtag上傳成功!", Toast.LENGTH_SHORT).show();
                        hashtagReview = hashtag.getText().toString();
                        System.out.println("score: "+score+" "+"hashtags: "+hashtagReview);
                        uploadReview(Float.toString(score),hashtagReview);

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

        //rateBar 初始化
        RatingBar mRatingBar = (RatingBar) dialog.findViewById(R.id.ratingBar1);
        RatingBar.OnRatingBarChangeListener ratingBarOnRatingBarChange
                = new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(), "rating: " + rating, Toast.LENGTH_LONG).show();
                score = rating;
            }
        };
        mRatingBar.setOnRatingBarChangeListener(ratingBarOnRatingBarChange);//設定監聽器

    }
    public void introduce_alert(String introduction){
        System.out.println("introduce function called");
        //語法一：new AlertDialog.Builder(主程式類別).XXX.XXX.XXX;
        AlertDialog dialog = new AlertDialog.Builder(Book.this)
                .setTitle("書籍簡介")
                .setMessage(introduction)
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


    private void uploadReview(final String score, final String hashtags){
        new Thread(new Runnable() {
            @Override
            public void run() {
        System.out.println("start to upload book review.");
        System.out.println("receive review infomation: "+score+ " "+ hashtags);
        String uploadReviewUrl = "http://140.119.19.18:5000/api/v1/book_upload/";
        String uniqueID = UUID.randomUUID().toString();
        String rawData = "{\"mms_id\":\""+ requestMms_id +"\",\"session_id\":\""+uniqueID+"\",\"hashtag\":\""+hashtags+"\",\"rating\":\""+score+"\"}";
        String charset = "UTF-8";
        System.out.println("upload info request: "+rawData);

        URLConnection connection = null;
        try {
            connection = new URL(uploadReviewUrl).openConnection();
        } catch (Exception e) {
            Log.d(TAG,"upload review connection failed");
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
            System.out.println("receiving book_upload_info : "+response.toString() + " " + response);
        } catch (Exception e) {
            System.out.println("error in receiving book_info");
            e.printStackTrace();
        }
            }

    }).start();
    }

    public void requestBook(final String resrecmms_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running");
                String uniqueID = UUID.randomUUID().toString();
                String rawData = "{\"mms_id\":\""+ resrecmms_id +"\",\"session_id\":\""+uniqueID+"\"}";
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
                    resrecRecommendation = resJson.getString("item_recommendation");
                    resrecCover = resJson.getString("cover");
                    resrecHashtag = resJson.getString("hashtag");
                    resrecIntroduction = resJson.getString("introduction");
                    resrecRating = resJson.getString("rating");
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
                        bookIt.putExtra("mms_id",resrecmms_id);
                        bookIt.putExtra("resAuthor",resrecAuthor);
                        bookIt.putExtra("resBookName",resrecBookName);
                        bookIt.putExtra("resLocandAvai",resrecLocandAvai.toString());
                        bookIt.putExtra("resRecommendation",resrecRecommendation.toString());
                        bookIt.putExtra("resCover",resrecCover);
                        bookIt.putExtra("resHashtag",resrecHashtag);
                        bookIt.putExtra("resIntroduction",resrecIntroduction);
                        bookIt.putExtra("resRating",resrecRating);
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
