package com.asus.zenbodialogsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.robot.asus.robotactivity.RobotActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Arrays;
import org.json.JSONObject;
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
    static String resAuthor;
    static String resBookName;
    static String resLocandAvai;
    static String resRecommendation;
    static String resAssoRecommendation;
    static String resCover;
    static String resHashtag;
    static String resIntroduction;
    static String resRating;
    static String resrecAuthor;
    static String resrecBookName;
    static String resrecLocandAvai;
    static String resrecRecommendation;
    static String resrecAssoRecommendation;
    static String resrecCover;
    static String resrecHashtag;
    static String resrecIntroduction;
    static String resrecRating;
    static float score;
    static String hashtagReview;
    static String requestMms_id;
    static String user_name;
    static String u_id;
    static String email;
    static String uu_list;
    static CharSequence start_time;
    static CharSequence exit_time;
    static String visit_state;

    static ArrayList<String> ii_list_mms_id = new ArrayList<>();
    static ArrayList<String> ii_list_book_name = new ArrayList<>();
    static ArrayList<String> ii_list_book_author = new ArrayList<>();
    static ArrayList<String> ii_list_cover = new ArrayList<>();
    static ArrayList<String> asso_list_mms_id = new ArrayList<>();
    static ArrayList<String> asso_list_book_name = new ArrayList<>();
    static ArrayList<String> asso_list_book_author = new ArrayList<>();
    static ArrayList<String> asso_list_cover = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        robotAPI.vision.cancelDetectFace();
        ii_list_mms_id = new ArrayList<>();
        ii_list_book_name = new ArrayList<>();
        ii_list_book_author = new ArrayList<>();
        ii_list_cover = new ArrayList<>();
        asso_list_mms_id = new ArrayList<>();
        asso_list_book_name = new ArrayList<>();
        asso_list_book_author = new ArrayList<>();
        asso_list_cover = new ArrayList<>();
        System.out.println("book create score: "+score+" "+"hashtags: "+hashtagReview);
        //undo: book hashtag book score
        System.out.println("sucess to change to book");
        setContentView(R.layout.activity_book);
        bookClass = Book.this;
        Intent bookIt = this.getIntent();
        visit_state = bookIt.getStringExtra("state");
        requestMms_id = bookIt.getStringExtra("mms_id");
        resAuthor = bookIt.getStringExtra("resAuthor");
        resBookName = bookIt.getStringExtra("resBookName");
        resLocandAvai = bookIt.getStringExtra("resLocandAvai");
        resRecommendation = bookIt.getStringExtra("resRecommendation");
        System.out.println("new resRecommendation: "+resRecommendation);
        resAssoRecommendation = bookIt.getStringExtra("resAssoRecommendation");
        resCover = bookIt.getStringExtra("resCover");
        resHashtag = bookIt.getStringExtra("resHashtag");
        resRating = bookIt.getStringExtra("resRating");
        final String resIntroduction = bookIt.getStringExtra("resIntroduction");
        System.out.println("book_info sucess receive: " + resAuthor + resBookName  +resRating+" "+resAssoRecommendation );
        if(visit_state!=null && visit_state.equals("login")){
            user_name = bookIt.getStringExtra("user_name");
            u_id = bookIt.getStringExtra("u_id");
            email = bookIt.getStringExtra("email");
            uu_list = bookIt.getStringExtra("uu_list");
            System.out.println("book request state is login,start timer!");
            Calendar mCal = Calendar.getInstance();
            start_time = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());    // kk:24小時制, hh:12小時制
            System.out.println("starting request this book time is: "+start_time+", the book's mms_id is: "+requestMms_id);
        }


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


        List<String> temp1 = new ArrayList<String>(Arrays.asList(resRecommendation.split("#@")));
        System.out.println("start to translate iilist");
        for (int i = 0; i < temp1.size(); i++) {
            System.out.println("book top ten: " + temp1.get(i));
            String book = temp1.get(i);
            int index1 = book.indexOf("@@");
            int index2 = book.indexOf("@#");
            int index3 = book.indexOf("##");
            String mms_id = book.substring(0, index1);
            String name = book.substring(index1 + 2, index2-1);
            String author = book.substring(index2 + 2, index3 - 1);
            String cover = book.substring(index3 + 2);
            System.out.println("notyet iilist_"+i+" "+ii_list_book_name);
            ii_list_mms_id.add(mms_id);
            ii_list_book_name.add(name);
            ii_list_book_author.add(author);
            ii_list_cover.add(cover);
            System.out.println("already iilist_"+i+" "+ii_list_book_name);
        }

        System.out.println("阿阿book recommendation is iilist:"+ii_list_book_name);

        List<String> temp2 = new ArrayList<String>(Arrays.asList(resAssoRecommendation.split("#@")));
        System.out.println("start to translate assolist");
        for (int i = 0; i < temp2.size(); i++) {
            System.out.println("book asso: " + temp2.get(i));
            String book = temp2.get(i);
            int index1 = book.indexOf("@@");
            int index2 = book.indexOf("@#");
            int index3 = book.indexOf("##");
            String mms_id = book.substring(0, index1);
            String name = book.substring(index1 + 2, index2-1);
            String author = book.substring(index2 + 2, index3 - 1);
            String cover = book.substring(index3 + 2);

            asso_list_mms_id.add(mms_id);
            asso_list_book_name.add(name);
            asso_list_book_author.add(author);
            asso_list_cover.add(cover);
        }
        System.out.println("阿阿book recommendation is assolist:"+asso_list_book_name);

        System.out.println("start to change recommendation ii_list.");
        ImageView imageview1 = (ImageView) findViewById(R.id.imageview1);
        ImageView imageview2 = (ImageView) findViewById(R.id.imageview2);
        ImageView imageview3 = (ImageView) findViewById(R.id.imageview3);
        ImageView imageview4 = (ImageView) findViewById(R.id.imageview4);
        ImageView imageview5 = (ImageView) findViewById(R.id.imageview5);
        ImageView imageview6 = (ImageView) findViewById(R.id.imageview6);
        ImageView imageview7 = (ImageView) findViewById(R.id.imageview7);
        ImageView imageview8 = (ImageView) findViewById(R.id.imageview8);
        ImageView imageview9 = (ImageView) findViewById(R.id.imageview9);
        ImageView imageview10 = (ImageView) findViewById(R.id.imageview10);
        TextView tv1 = (TextView) findViewById(R.id.textview1);
        TextView tv2 = (TextView) findViewById(R.id.textview2);
        TextView tv3 = (TextView) findViewById(R.id.textview3);
        TextView tv4 = (TextView) findViewById(R.id.textview4);
        TextView tv5 = (TextView) findViewById(R.id.textview5);
        TextView tv6 = (TextView) findViewById(R.id.textview6);
        TextView tv7 = (TextView) findViewById(R.id.textview7);
        TextView tv8 = (TextView) findViewById(R.id.textview8);
        TextView tv9 = (TextView) findViewById(R.id.textview9);
        TextView tv10 = (TextView) findViewById(R.id.textview10);
        new Book.DownloadImageTask(imageview1)
                .execute(ii_list_cover.get(0));
        new Book.DownloadImageTask(imageview2)
                .execute(ii_list_cover.get(1));
        new Book.DownloadImageTask(imageview3)
                .execute(ii_list_cover.get(2));
        new Book.DownloadImageTask(imageview4)
                .execute(ii_list_cover.get(3));
        new Book.DownloadImageTask(imageview5)
                .execute(ii_list_cover.get(4));
        new Book.DownloadImageTask(imageview6)
                .execute(ii_list_cover.get(5));
        new Book.DownloadImageTask(imageview7)
                .execute(ii_list_cover.get(6));
        new Book.DownloadImageTask(imageview8)
                .execute(ii_list_cover.get(7));
        new Book.DownloadImageTask(imageview9)
                .execute(ii_list_cover.get(8));
        new Book.DownloadImageTask(imageview10)
                .execute(ii_list_cover.get(9));
        tv1.setText(ii_list_book_name.get(0));
        tv2.setText(ii_list_book_name.get(1));
        tv3.setText(ii_list_book_name.get(2));
        tv4.setText(ii_list_book_name.get(3));
        tv5.setText(ii_list_book_name.get(4));
        tv6.setText(ii_list_book_name.get(5));
        tv7.setText(ii_list_book_name.get(6));
        tv8.setText(ii_list_book_name.get(7));
        tv9.setText(ii_list_book_name.get(8));
        tv10.setText(ii_list_book_name.get(9));
        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(0));
            }
        });
        //imageView2 Onclick define
        imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(1));
            }
        });
        //imageView3 Onclick define
        imageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(2));
            }
        });
        //imageView4 Onclick define
        imageview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(3));
            }
        });
        //imageView5 Onclick define
        imageview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(4));
            }
        });
        //imageView6 Onclick define
        imageview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(5));
            }
        });
        //imageView7 Onclick define
        imageview7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(6));
            }
        });
        //imageView8 onclick define
        imageview8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(7));
            }
        });
        //imageView9 Onclick define
        imageview9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(8));
            }
        });
        //imageView10 Onclick define
        imageview10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook(ii_list_mms_id.get(9));
            }
        });


        TextView bookName = (TextView) findViewById(R.id.bookName);
        TextView authorName = (TextView) findViewById(R.id.authorName);
        TextView location = (TextView) findViewById(R.id.location);
        TextView available = (TextView) findViewById(R.id.available);
        TextView rating = (TextView) findViewById(R.id.score_tv);

        bookName.setText("書名: " + resBookName);
        authorName.setText("作者: " + resAuthor);
        location.setText("位置: " + loca_info);
        available.setText("狀態: " + avai_info);
        rating.setText("評分: "+resRating);
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


        //backButton 初始化
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visit_state!= null && visit_state.equals("login")){
                    System.out.println("stop viewing this book,stop timer!");
                    Calendar mCal = Calendar.getInstance();
                    exit_time = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());    // kk:24小時制, hh:12小時制
                    System.out.println("stop viewing this book time is: "+exit_time+", stop viewing mms_id is: "+requestMms_id);
                    upload_browse_log();
                    Intent backIT = new Intent();
                    backIT.putExtra("view_mms_id",requestMms_id);
                    backIT.putExtra("change","book");
                    backIT.putExtra("user_name",user_name);
                    backIT.putExtra("u_id",u_id);
                    backIT.putExtra("email",email);
                    backIT.putExtra("uu_list",uu_list);
                    backIT.setClass(Book.this, Personal.class);
                    startActivity(backIT);
                }else{
                Intent backIT = new Intent();
                backIT.setClass(Book.this, Guest.class);
                startActivity(backIT);
                }
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


        //iilistBtn初始化
        Button iilistBtn = findViewById(R.id.iiBtn);
        iilistBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("start to change recommendation ii_list.");
                ImageView imageview1 = (ImageView) findViewById(R.id.imageview1);
                ImageView imageview2 = (ImageView) findViewById(R.id.imageview2);
                ImageView imageview3 = (ImageView) findViewById(R.id.imageview3);
                ImageView imageview4 = (ImageView) findViewById(R.id.imageview4);
                ImageView imageview5 = (ImageView) findViewById(R.id.imageview5);
                ImageView imageview6 = (ImageView) findViewById(R.id.imageview6);
                ImageView imageview7 = (ImageView) findViewById(R.id.imageview7);
                ImageView imageview8 = (ImageView) findViewById(R.id.imageview8);
                ImageView imageview9 = (ImageView) findViewById(R.id.imageview9);
                ImageView imageview10 = (ImageView) findViewById(R.id.imageview10);
                TextView tv1 = (TextView) findViewById(R.id.textview1);
                TextView tv2 = (TextView) findViewById(R.id.textview2);
                TextView tv3 = (TextView) findViewById(R.id.textview3);
                TextView tv4 = (TextView) findViewById(R.id.textview4);
                TextView tv5 = (TextView) findViewById(R.id.textview5);
                TextView tv6 = (TextView) findViewById(R.id.textview6);
                TextView tv7 = (TextView) findViewById(R.id.textview7);
                TextView tv8 = (TextView) findViewById(R.id.textview8);
                TextView tv9 = (TextView) findViewById(R.id.textview9);
                TextView tv10 = (TextView) findViewById(R.id.textview10);
                new Book.DownloadImageTask(imageview1)
                        .execute(ii_list_cover.get(0));
                new Book.DownloadImageTask(imageview2)
                        .execute(ii_list_cover.get(1));
                new Book.DownloadImageTask(imageview3)
                        .execute(ii_list_cover.get(2));
                new Book.DownloadImageTask(imageview4)
                        .execute(ii_list_cover.get(3));
                new Book.DownloadImageTask(imageview5)
                        .execute(ii_list_cover.get(4));
                new Book.DownloadImageTask(imageview6)
                        .execute(ii_list_cover.get(5));
                new Book.DownloadImageTask(imageview7)
                        .execute(ii_list_cover.get(6));
                new Book.DownloadImageTask(imageview8)
                        .execute(ii_list_cover.get(7));
                new Book.DownloadImageTask(imageview9)
                        .execute(ii_list_cover.get(8));
                new Book.DownloadImageTask(imageview10)
                        .execute(ii_list_cover.get(9));
                tv1.setText(ii_list_book_name.get(0));
                tv2.setText(ii_list_book_name.get(1));
                tv3.setText(ii_list_book_name.get(2));
                tv4.setText(ii_list_book_name.get(3));
                tv5.setText(ii_list_book_name.get(4));
                tv6.setText(ii_list_book_name.get(5));
                tv7.setText(ii_list_book_name.get(6));
                tv8.setText(ii_list_book_name.get(7));
                tv9.setText(ii_list_book_name.get(8));
                tv10.setText(ii_list_book_name.get(9));
                //imageView1 Onclick define
                imageview1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(0));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView2 Onclick define
                imageview2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(1));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView3 Onclick define
                imageview3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(2));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView4 Onclick define
                imageview4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(3));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView5 Onclick define
                imageview5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(4));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView6 Onclick define
                imageview6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(5));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView7 Onclick define
                imageview7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(6));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView8 onclick define
                imageview8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(7));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView9 Onclick define
                imageview9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(8));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView10 Onclick define
                imageview10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(ii_list_mms_id.get(9));
                        System.out.println("recommend login"+visit_state);
                    }
                });
            }
        });


        //assolistBtn初始化
        Button assoBtn = findViewById(R.id.assoBtn);
        assoBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("start to change recommendation asso.");
                ImageView imageview1 = (ImageView) findViewById(R.id.imageview1);
                ImageView imageview2 = (ImageView) findViewById(R.id.imageview2);
                ImageView imageview3 = (ImageView) findViewById(R.id.imageview3);
                ImageView imageview4 = (ImageView) findViewById(R.id.imageview4);
                ImageView imageview5 = (ImageView) findViewById(R.id.imageview5);
                ImageView imageview6 = (ImageView) findViewById(R.id.imageview6);
                ImageView imageview7 = (ImageView) findViewById(R.id.imageview7);
                ImageView imageview8 = (ImageView) findViewById(R.id.imageview8);
                ImageView imageview9 = (ImageView) findViewById(R.id.imageview9);
                ImageView imageview10 = (ImageView) findViewById(R.id.imageview10);
                TextView tv1 = (TextView) findViewById(R.id.textview1);
                TextView tv2 = (TextView) findViewById(R.id.textview2);
                TextView tv3 = (TextView) findViewById(R.id.textview3);
                TextView tv4 = (TextView) findViewById(R.id.textview4);
                TextView tv5 = (TextView) findViewById(R.id.textview5);
                TextView tv6 = (TextView) findViewById(R.id.textview6);
                TextView tv7 = (TextView) findViewById(R.id.textview7);
                TextView tv8 = (TextView) findViewById(R.id.textview8);
                TextView tv9 = (TextView) findViewById(R.id.textview9);
                TextView tv10 = (TextView) findViewById(R.id.textview10);
                new Book.DownloadImageTask(imageview1)
                        .execute(asso_list_cover.get(0));
                new Book.DownloadImageTask(imageview2)
                        .execute(asso_list_cover.get(1));
                new Book.DownloadImageTask(imageview3)
                        .execute(asso_list_cover.get(2));
                new Book.DownloadImageTask(imageview4)
                        .execute(asso_list_cover.get(3));
                new Book.DownloadImageTask(imageview5)
                        .execute(asso_list_cover.get(4));
                new Book.DownloadImageTask(imageview6)
                        .execute(asso_list_cover.get(5));
                new Book.DownloadImageTask(imageview7)
                        .execute(asso_list_cover.get(6));
                new Book.DownloadImageTask(imageview8)
                        .execute(asso_list_cover.get(7));
                new Book.DownloadImageTask(imageview9)
                        .execute(asso_list_cover.get(8));
                new Book.DownloadImageTask(imageview10)
                        .execute(asso_list_cover.get(9));
                tv1.setText(asso_list_book_name.get(0));
                tv2.setText(asso_list_book_name.get(1));
                tv3.setText(asso_list_book_name.get(2));
                tv4.setText(asso_list_book_name.get(3));
                tv5.setText(asso_list_book_name.get(4));
                tv6.setText(asso_list_book_name.get(5));
                tv7.setText(asso_list_book_name.get(6));
                tv8.setText(asso_list_book_name.get(7));
                tv9.setText(asso_list_book_name.get(8));
                tv10.setText(asso_list_book_name.get(9));
                imageview1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(0));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView2 Onclick define
                imageview2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(1));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView3 Onclick define
                imageview3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(2));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView4 Onclick define
                imageview4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(3));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView5 Onclick define
                imageview5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(4));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView6 Onclick define
                imageview6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(5));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView7 Onclick define
                imageview7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(6));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView8 onclick define
                imageview8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(7));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView9 Onclick define
                imageview9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(8));
                        System.out.println("recommend login"+visit_state);
                    }
                });
                //imageView10 Onclick define
                imageview10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBook(asso_list_mms_id.get(9));
                        System.out.println("recommend login"+visit_state);
                    }
                });

            }
        });



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

    public void upload_browse_log(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                String uploadBrowseLogUrl = "http://140.119.19.18:5000/api/v1/browsing_upload/";
                System.out.println("start running");
                String uniqueID = UUID.randomUUID().toString();
                String rawData = "{\"mms_id\":\""+ requestMms_id +"\",\"user_id\":\""+u_id+"\",\"start_time\":\""+start_time+"\",\"end_time\":\""+exit_time+"\",\"session_id\":\""+uniqueID+"\"}";
                String charset = "UTF-8";
                System.out.println("browse log upload request: "+rawData);

                URLConnection connection = null;
                try {
                    connection = new URL(uploadBrowseLogUrl).openConnection();
                } catch (Exception e) {
                    Log.d(TAG,"browse log connection failed");
                    e.printStackTrace();
                }
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
                try (OutputStream output = connection.getOutputStream()) {
                    Log.d("browse logoutput format",output.toString());
                    output.write(rawData.getBytes(charset));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                InputStream response = null;
                try {
                    response = connection.getInputStream();
                    System.out.println("receiving browse log response : "+response.toString() + " " + response);
                } catch (Exception e) {
                    System.out.println("error in receiving browse log");
                    e.printStackTrace();
                }
                try {
                    System.out.println("start translate browse log: ");
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
                    System.out.println("response browse log : "+text);

                    JSONObject resJson;
                    resJson = new JSONObject(text);
                    System.out.println("browse log resJson: "+ resJson);
                    String res = resJson.getString("res");
                    System.out.println("browse log response state: "+res);
                } catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("error in translate browse log");
                }
            }
        }).start();
    }

    public void requestBook(final String resrecmms_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start running");
                String bookUrl = "http://140.119.19.18:5000/api/v1/book/";
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

                    asso_list_book_name = new ArrayList<>();
                    asso_list_book_author = new ArrayList<>();
                    asso_list_mms_id = new ArrayList<>();
                    asso_list_cover = new ArrayList<>();
                    ii_list_book_name = new ArrayList<>();
                    ii_list_book_author = new ArrayList<>();
                    ii_list_mms_id = new ArrayList<>();
                    ii_list_cover = new ArrayList<>();
                    resrecAuthor = resJson.getString("author");
                    resrecBookName = resJson.getString("book_name");
                    resrecLocandAvai = resJson.getString("location_and_available");
                    resrecRecommendation = resJson.getString("item_recommendation");
                    resrecAssoRecommendation = resJson.getString("asso_recommendation");
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
                        if(visit_state!=null &&visit_state.equals("login")){
                            bookIt.putExtra("user_name",user_name);
                            bookIt.putExtra("u_id",u_id);
                            bookIt.putExtra("email",email);
                            bookIt.putExtra("uu_list",uu_list);
                            bookIt.putExtra("state",visit_state);
                        }
                        bookIt.putExtra("mms_id",resrecmms_id);
                        bookIt.putExtra("resAuthor",resrecAuthor);
                        bookIt.putExtra("resBookName",resrecBookName);
                        bookIt.putExtra("resLocandAvai",resrecLocandAvai.toString());
                        bookIt.putExtra("resRecommendation",resrecRecommendation);
                        bookIt.putExtra("resAssoRecommendation",resrecAssoRecommendation);
                        bookIt.putExtra("resCover",resrecCover);
                        bookIt.putExtra("resHashtag",resrecHashtag);
                        bookIt.putExtra("resIntroduction",resrecIntroduction);
                        bookIt.putExtra("resRating",resrecRating);
                        bookIt.setClass(Book.this,Book.class);
                        startActivity(bookIt);
                        System.out.println("resrecrecommendation is"+resrecRecommendation+"\n"+resrecAssoRecommendation);


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
