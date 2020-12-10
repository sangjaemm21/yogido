package com.example.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity5 extends AppCompatActivity {

    TextView txt_shopID;
    TextView txt_shopName;
    TextView txt_date;
    EditText etxt_review;

    Integer selected_star;

    Integer shopID;
    String jsonString;
    String currentDate_str;

    public static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Intent intent = getIntent();

        shopID = intent.getExtras().getInt("shopID");

        txt_shopID = (TextView) findViewById(R.id.main5_txt_shopID);
        txt_shopName = (TextView) findViewById(R.id.main5_txt_shopName);
        txt_date = (TextView) findViewById(R.id.main5_txt_date);
        etxt_review = (EditText)findViewById(R.id.main5_etxt_review);

        txt_shopID.setText("가게 ID : "+shopID);

        Date currentTime = Calendar.getInstance().getTime();
        currentDate_str = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentTime);
        txt_date.setText(currentDate_str);

        MyApplication myApp = (MyApplication)getApplicationContext();
        String hostStr =myApp.getState();

        JsonParse jsonParse = new JsonParse("main5_showInfo");
        jsonParse.execute(hostStr + "/getShopInfo.php");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                Integer result = data.getIntExtra("result", 1);
                Button starBtn = (Button)findViewById(R.id.main5_btn_star);

                String star_str = "";
                for(int i=0;i<result;i++)
                    star_str += "★";
                for(int i=result;i<5;i++)
                    star_str += "☆";

                star_str += " ("+result+" / 5)";

                starBtn.setText(star_str);
                selected_star = result;
            }
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, starPopup.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void uploadReview(View view) throws IOException {
        MyApplication myApp = (MyApplication)getApplicationContext();
        String hostStr =myApp.getState();

        JsonParse jsonParse = new JsonParse("main5_sndReview");
        jsonParse.execute(hostStr + "/uploadReview.php");

        Toast.makeText(getApplicationContext(),"리뷰등록완료",Toast.LENGTH_SHORT).show();
        finish();
    }


    public class JsonParse extends AsyncTask<String, Void, String> {

        public String kind_str;

        JsonParse(String str){
            kind_str = str;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];

            try{
                URL serverURL = new URL(url);
                HttpURLConnection http = (HttpURLConnection) serverURL.openConnection();
                http.setReadTimeout(5000);
                http.setConnectTimeout(5000);
                http.setRequestMethod("POST");
                http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                String postParameters="";
                if(kind_str !="main5_sndReview")
                    postParameters = "shopID=" + shopID.toString();
                else
                {
                    postParameters = "shopID=" + shopID.toString()+"&" +
                            "review="+etxt_review.getText().toString()+"&"+
                            "star="+selected_star.toString()+"&"+
                            "reviewer=이도희&"+
                            "review_date="+currentDate_str;
                }

                OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(outStream);
                writer.write(postParameters);
                writer.flush(); ;

                int responseStatusCode = http.getResponseCode();
                Log.d("tests", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = http.getInputStream();
                }
                else{
                    inputStream = http.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine())!=null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            }catch(Exception e){
                Log.d("err : ",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s == null){

            }
            else{
                jsonString = s;

                try {
                    if(kind_str == "main5_showInfo"){
                        JSONArray jsonArray = new JSONArray(s);

                        JSONObject obj = jsonArray.getJSONObject(0);

                        if (obj.getString("shopName")!=null){
                            String shopName = obj.getString("shopName");
                            String subName = obj.getString("subName");
                            String img_main_str = obj.getString("img_src");

                            TextView txt_shopName = (TextView)findViewById(R.id.main5_txt_shopName);

                            txt_shopName.setText(shopName +" "+ subName);

                            if(img_main_str!="null"){
                                ImageView img_main = (ImageView) findViewById(R.id.main5_img_main);
                                new DownloadFilesTask(img_main).execute(img_main_str+".png");
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        public ImageView imgView;

        public DownloadFilesTask(ImageView img){
            imgView = img;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                MyApplication myApp = (MyApplication)getApplicationContext();
                String hostStr =myApp.getState();
                String img_url = hostStr+"/images/" + strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            imgView.setImageBitmap(result);
        }
    }
}