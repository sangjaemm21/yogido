package com.example.food;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class starPopup extends Activity {
    Integer selectedStar;
    Button buttons[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_star_popup);

        selectedStar = 5;

        buttons = new Button[5];
        buttons[0] = (Button)findViewById(R.id.starPop_btn1);
        buttons[1] = (Button)findViewById(R.id.starPop_btn2);
        buttons[2] = (Button)findViewById(R.id.starPop_btn3);
        buttons[3] = (Button)findViewById(R.id.starPop_btn4);
        buttons[4] = (Button)findViewById(R.id.starPop_btn5);
    }


    public void starClick(View view) {
        for(int i=0;i<5;i++)
        {
            if(view.getId() == buttons[i].getId()){
                selectedStar = i+1;

                for(int j=0;j<=i;j++)
                    buttons[j].setTextColor(Color.parseColor("#F13F4B"));

                for(int j=i+1;j<5;j++)
                    buttons[j].setTextColor(Color.parseColor("#c8c8c8"));
            }
        }

    }

    public void mOnClose(View view) {
        Intent intent = new Intent();
        intent.putExtra("result", selectedStar);
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }
}