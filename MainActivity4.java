package com.example.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.net.URL;
import java.util.ArrayList;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity4 extends AppCompatActivity {

    Integer shopID;
    String jsonString;
    ArrayList<LinearLayout> food_layout_list;
    ArrayList<LinearLayout> review_layout_list;
    Button button_food;
    Button button_review;

    private IntentIntegrator qrScan;

    Double latitude = 35.85036929033647;
    Double longitude = 128.61592230854183;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        TextView txt_shopNo = (TextView) findViewById(R.id.main4_txt_shopNo);

        Intent intent = getIntent();

        shopID = intent.getExtras().getInt("shopID");
        txt_shopNo.setText("shopNo : " + shopID.toString());

        food_layout_list = new ArrayList<>();
        review_layout_list = new ArrayList<>();
        button_food = (Button)findViewById(R.id.main4_btn1);
        button_review = (Button)findViewById(R.id.main4_btn2);

        MyApplication myApp = (MyApplication)getApplicationContext();
        String hostStr =myApp.getState();

        JsonParse jsonParse = new JsonParse("main4_showInfo");
        jsonParse.execute(hostStr + "/getShopInfo.php");

        jsonParse = new JsonParse("main4_foodMenu");
        jsonParse.execute(hostStr + "/getFoodMenu.php");

        jsonParse = new JsonParse("main4_review");
        jsonParse.execute(hostStr + "/getReview.php");
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main4_btn_review:
                qrScan = new IntentIntegrator(this);
                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
                break;

            case R.id.main4_btn1:
                button_food.setBackgroundColor(Color.parseColor("#F13F4B"));
                button_food.setTextColor(Color.parseColor("#FFFFFF"));

                button_review.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_review.setTextColor(Color.parseColor("#000000"));
                for(int i=0;i<food_layout_list.size();i++)
                {
                    LinearLayout ll = food_layout_list.get(i);
                    ll.setVisibility(View.VISIBLE);
                }
                for(int i=0;i<review_layout_list.size();i++)
                {
                    LinearLayout ll = review_layout_list.get(i);
                    ll.setVisibility(View.GONE);
                }
                break;

            case R.id.main4_btn2:
                button_review.setBackgroundColor(Color.parseColor("#F13F4B"));
                button_review.setTextColor(Color.parseColor("#FFFFFF"));

                button_food.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_food.setTextColor(Color.parseColor("#000000"));
                for(int i=0;i<review_layout_list.size();i++)
                {
                    LinearLayout ll = review_layout_list.get(i);
                    ll.setVisibility(View.VISIBLE);
                }
                for(int i=0;i<food_layout_list.size();i++)
                {
                    LinearLayout ll = food_layout_list.get(i);
                    ll.setVisibility(View.GONE);
                }
                break;


            case R.id.main4_btn3:
                Intent intent = new Intent(getApplicationContext(), googleMapTst.class);
                TextView txt_shopName = (TextView)findViewById(R.id.main4_txt_shopName);
                TextView txt_shopKind = (TextView)findViewById(R.id.main4_txt_shopKind);
                intent.putExtra("lat",latitude);
                intent.putExtra("long",longitude);
                intent.putExtra("shopName",txt_shopName.getText().toString());
                intent.putExtra("shopKind",txt_shopKind.getText().toString());
                startActivity(intent);
                break;
        }
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

                String postParameters = "shopID=" + shopID.toString();

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
                    if(kind_str == "main4_showInfo"){
                        TextView main2_txt = (TextView) findViewById(R.id.main2_txt_place);
                        JSONArray jsonArray = new JSONArray(s);

                        JSONObject obj = jsonArray.getJSONObject(0);

                        if (obj.getString("shopName")!=null){
                            String shopName = obj.getString("shopName");
                            Double review_star = obj.getDouble("star");
                            String subName = obj.getString("subName");
                            String shopKind = obj.getString("shopKind");
                            String address = obj.getString("address");
                            String img_main_str = obj.getString("img_src");
                            latitude = obj.getDouble("Latitude");
                            longitude = obj.getDouble("Longitude");

                            TextView txt_shopName = (TextView)findViewById(R.id.main4_txt_shopName);
                            TextView txt_shopKind = (TextView)findViewById(R.id.main4_txt_shopKind);
                            TextView txt_shopAddr = (TextView)findViewById(R.id.main4_txt_shopAddress);
                            TextView txt_shopStar = (TextView)findViewById(R.id.main4_txt_star);

                            txt_shopName.setText(shopName +" "+ subName);
                            txt_shopKind.setText(shopKind);
                            txt_shopAddr.setText(address);

                            String review_star_str = "";
                            Integer review_star_int = (int) Math.round(review_star);

                            for(int i=0;i<review_star_int;i++)
                                review_star_str += "★";
                            for(int i=review_star_int;i<5;i++)
                                review_star_str += "☆";

                            review_star_str += " ("+review_star.toString() + " / 5.0)";
                            txt_shopStar.setText(review_star_str);

                            if(img_main_str!="null"){
                                ImageView img_main = (ImageView) findViewById(R.id.main4_img_main);
                                new DownloadFilesTask(img_main).execute(img_main_str+".png");
                            }

                        }
                    }
                    if(kind_str == "main4_foodMenu"){
                        LinearLayout main4_content = (LinearLayout)findViewById(R.id.main4_content);
                        //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = new JSONArray(s);

                        for(Integer i=0;i<jsonArray.length();i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Integer price = obj.getInt("price");
                            String food_name = obj.getString("food_name");
                            String img_str = obj.getString("img_src");
                            String food_price_str = price.toString() + "원";

                            LinearLayout ll = (LinearLayout) View.inflate(getApplicationContext(), R.layout.food_menu, null);
                            main4_content.addView(ll);
                            food_layout_list.add(ll);

                            TextView txt_food_name = (TextView) ll.findViewById(R.id.food_name);
                            TextView txt_food_price = (TextView)ll.findViewById(R.id.food_price);
                            ImageView img_food_src = (ImageView)ll.findViewById(R.id.food_img);

                            txt_food_name.setText(food_name);
                            txt_food_price.setText(food_price_str);

                            if(img_str!="null"){
                                new DownloadFilesTask(img_food_src).execute(img_str);
                            }
                        }
                    }


                    if(kind_str == "main4_review"){
                        LinearLayout main4_content = (LinearLayout)findViewById(R.id.main4_content);
                        //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                        JSONArray jsonArray = new JSONArray(s);

                        for(Integer i=0;i<jsonArray.length();i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Integer star = obj.getInt("star");
                            String review_str = obj.getString("review");
                            String reviewer = obj.getString("reviewerID");
                            String star_str = "";

                            for(int j=0;j<star;j++)
                                star_str += "★";
                            for(int j=star;j<5;j++)
                                star_str += "☆";

                            star_str = star_str + " ("+star+" / 5)";

                            LinearLayout ll = (LinearLayout) View.inflate(getApplicationContext(), R.layout.review, null);
                            main4_content.addView(ll);
                            review_layout_list.add(ll);

                            TextView txt_review_name = (TextView) ll.findViewById(R.id.review_name);
                            TextView txt_review_star = (TextView)ll.findViewById(R.id.review_star);
                            TextView txt_review_review = (TextView)ll.findViewById(R.id.review_review);

                            txt_review_name.setText(reviewer);
                            txt_review_star.setText(star_str);
                            txt_review_review.setText(review_str);

                            ll.setVisibility(View.GONE);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                String s = result.getContents();
                //Toast.makeText(this, s, Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject obj = jsonArray.getJSONObject(0);
                    Integer shopID = obj.getInt("shopID");

                    Intent intent = new Intent(getApplicationContext(), MainActivity5.class);
                    intent.putExtra("shopID",shopID);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}