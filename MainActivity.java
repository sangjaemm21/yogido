package com.example.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;


public class MainActivity extends AppCompatActivity {
    private TextView nowPlaceView;

    private String jsonString;
    //ArrayList<Tree> treeArrayList;
    private BottomNavigationView mBottomNV;

    Double latitude = 35.85036929033647;
    Double longitude = 128.61592230854183;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNV = (BottomNavigationView)findViewById(R.id.bottom);
        mBottomNV.bringToFront();

        nowPlaceView = (TextView)findViewById(R.id.text1);
        nowPlaceView.setText("현재 위치 : 위도 "+latitude.toString()+", 경도 "+longitude.toString());
        Log.d("ch0","test");



        Log.d("ch9","test");
    }

    public void onClick(View view) {
        int foodKind = 0;

        switch (view.getId()){
            case R.id.main1_btn0: foodKind = 0; break;
            case R.id.main1_btn1: foodKind = 1; break;
            case R.id.main1_btn2: foodKind = 2; break;
            case R.id.main1_btn3: foodKind = 3; break;
            case R.id.main1_btn4: foodKind = 4; break;
            case R.id.main1_btn5: foodKind = 5; break;
            case R.id.main1_btn6: foodKind = 6; break;
            case R.id.main1_btn7: foodKind = 7; break;
            case R.id.main1_btn8: foodKind = 8; break;
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        intent.putExtra("foodKind",foodKind);
        startActivity(intent);
    }


}

