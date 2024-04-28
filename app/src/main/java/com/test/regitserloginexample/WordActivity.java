package com.test.regitserloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class WordActivity extends AppCompatActivity {

    private TextView tv_sub;

    private TextView tv_splitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);





        Button btn_wordout = (Button)findViewById(R.id.btn_wordout);
        btn_wordout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); //현재 액티비티 파괴
            }
        });
    }
}