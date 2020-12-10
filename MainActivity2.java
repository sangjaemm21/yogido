package com.example.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class MainActivity2 extends AppCompatActivity {

    Double latitude = 35.85036929033647;
    Double longitude = 128.61592230854183;
    private String jsonString;

    LinearLayout main2_layout;
    LayoutInflater inflater;

    Button main2_btns[];

    int main2_selected = 0;

    ArrayList<LinearLayout> main2_shop_array[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        main2_layout = (LinearLayout) findViewById(R.id.buttonLayout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        MyApplication myApp = (MyApplication)getApplicationContext();
        String hostStr =myApp.getState();

        main2_btns = new Button[9];
        main2_btns[0] = (Button)findViewById(R.id.main2_btn1);
        main2_btns[1] = (Button)findViewById(R.id.main2_btn2);
        main2_btns[2] = (Button)findViewById(R.id.main2_btn3);
        main2_btns[3] = (Button)findViewById(R.id.main2_btn4);
        main2_btns[4] = (Button)findViewById(R.id.main2_btn5);
        main2_btns[5] = (Button)findViewById(R.id.main2_btn6);
        main2_btns[6] = (Button)findViewById(R.id.main2_btn7);
        main2_btns[7] = (Button)findViewById(R.id.main2_btn8);
        main2_btns[8] = (Button)findViewById(R.id.main2_btn9);

        Intent intent = getIntent();
        main2_selected = intent.getExtras().getInt("foodKind");
        main2_btns[0].setBackgroundColor(Color.parseColor("#FFFFFF"));
        main2_btns[0].setTextColor(Color.parseColor("#000000"));
        main2_btns[main2_selected].setBackgroundColor(Color.parseColor("#F13F4B"));
        main2_btns[main2_selected].setTextColor(Color.parseColor("#FFFFFF"));

        main2_shop_array = new ArrayList[9];
        for(int i=0;i<9;i++)
            main2_shop_array[i] = new ArrayList<>();

        final JsonParse jsonParse = new JsonParse();
        jsonParse.execute(hostStr + "/mysqlTst.php");
    }

    public void menuOnlick(View view) {
        for(int i=0;i<9;i++)
        {
            if(main2_btns[i].getId() == view.getId()){
                main2_selected = i;
                main2_btns[i].setBackgroundColor(Color.parseColor("#F13F4B"));
                main2_btns[i].setTextColor(Color.parseColor("#FFFFFF"));
            }
            else{
                main2_btns[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
                main2_btns[i].setTextColor(Color.parseColor("#000000"));
            }
        }

        set_visible_shops();
    }

    public void set_visible_shops(){
        for(int i=0;i<main2_shop_array[0].size();i++)
            main2_shop_array[0].get(i).setVisibility(View.GONE);

        for(int i=0;i<main2_shop_array[main2_selected].size();i++)
            main2_shop_array[main2_selected].get(i).setVisibility(View.VISIBLE);
    }

    public class JsonParse extends AsyncTask<String, Void, String> {

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

                String postParameters = "latitude=" + latitude.toString() + "&longitude=" + longitude.toString();
                Log.d("tets",postParameters);

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
                    TextView main2_txt = (TextView) findViewById(R.id.main2_txt_place);
                    //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                    JSONArray jsonArray = new JSONArray(s);

                    for(Integer i=0;i<jsonArray.length();i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        final Integer shopID = obj.getInt("shopID");
                        String name = obj.getString("shopName");
                        Double dis = obj.getDouble("distance");
                        String dis_str = String.format("%.2fkm",dis);

                        Double star = obj.getDouble("star");
                        Integer reviews = obj.getInt("reviews");
                        String reviews_str = String.format("★%.1f | 리뷰 %d",star, reviews);
                        String img_main_str = obj.getString("img_src");

                        String shopCode = obj.getString("shopCode");
                        int shopCode_int = 0;

                        switch (shopCode){
                            case "Q01": shopCode_int = 1; break;
                            case "Q02": shopCode_int = 2; break;
                            case "Q03": shopCode_int = 3; break;
                            case "Q04": shopCode_int = 4; break;
                            case "Q05": shopCode_int = 5; break;
                            case "Q06": shopCode_int = 6; break;
                            case "Q07": shopCode_int = 7; break;
                            case "Q010": shopCode_int = 8; break;
                        }

                        LinearLayout ll = (LinearLayout) View.inflate(getApplicationContext(), R.layout.shop_button, null);
                        main2_layout.addView(ll);

                        main2_shop_array[0].add(ll);
                        main2_shop_array[shopCode_int].add(ll);

                        if (main2_selected!=0&&main2_selected!=shopCode_int){
                            ll.setVisibility(View.GONE);
                        }

                        TextView textView;
                        textView = (TextView) ll.findViewById(R.id.shop_button_text1);
                        textView.setText(name);

                        textView = (TextView) ll.findViewById(R.id.shop_button_text2);
                        textView.setText(reviews_str);

                        textView = (TextView) ll.findViewById(R.id.shop_button_text3);
                        textView.setText(dis_str);

                        if(img_main_str!="null")
                        {
                            Toast.makeText(getApplicationContext(),img_main_str,Toast.LENGTH_SHORT).show();
                            ImageView img_main = (ImageView) ll.findViewById(R.id.shop_button_image);
                            new DownloadFilesTask(img_main).execute(img_main_str+".png");
                        }

                        ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                                intent.putExtra("shopID",shopID);
                                startActivity(intent);
                            }
                        });

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