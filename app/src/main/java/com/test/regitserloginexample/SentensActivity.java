package com.test.regitserloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SentensActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentens);

        Button btn_sentensout = (Button)findViewById(R.id.btn_sentensout);

        btn_sentensout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SentensActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); //현재 액티비티 파괴
            }
        });
    }
}