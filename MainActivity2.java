package com.example.googlemapandroidapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.tabs.TabLayout;

public class MainActivity2 extends AppCompatActivity {
    Button btn;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                Toast myToast = Toast.makeText(this.getApplicationContext(),data.getStringExtra("INPUT_TEXT"), Toast.LENGTH_SHORT);
                myToast.show();

            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs) ;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                int pos = tab.getPosition() ;
                changeView(pos) ;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        }) ;
        btn = findViewById(R.id.reviewWriteBtn);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, ReviewActivity.class);
                startActivityForResult(intent,0);
            }
        }) ;

    }

    public void reviewWrite(){

    }
    private void changeView(int index) {
        RelativeLayout rev = (RelativeLayout) findViewById(R.id.review) ;
        NestedScrollView men = (NestedScrollView) findViewById(R.id.menu) ;


        switch (index) {
            case 0 :
                rev.setVisibility(View.INVISIBLE) ;
                men.setVisibility(View.VISIBLE) ;

                break ;
            case 1 :
                rev.setVisibility(View.VISIBLE) ;
                men.setVisibility(View.INVISIBLE) ;

                break ;

        }
    }
}