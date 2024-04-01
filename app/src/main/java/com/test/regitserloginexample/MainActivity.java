package com.test.regitserloginexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser; // 로그인 된 유저 정보
    private FragmentPagerAdapter fragmentPagerAdapter;
    private TextView Text_Information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("RegisterLoginExample", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "No Email"); // 기본값으로 "No Email" 설정

        // 가져온 이메일을 TextView에 표시
        TextView textInformation = findViewById(R.id.text_Information);
        textInformation.setText(userEmail);

        SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        Button btn_word = (Button)findViewById(R.id.btn_word); //단어 버튼
        Button btn_sentens = (Button)findViewById(R.id.btn_sentens); //문장 버튼

        btn_word.setOnClickListener(new View.OnClickListener() { //단어 버튼 클릭시 이벤트 실행
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WordActivity.class);
                startActivity(intent);
                finish(); //현재 액티비티 파괴
            }
        });

        btn_sentens.setOnClickListener(new View.OnClickListener() {//문장 버튼 클릭시 이벤트 실행
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SentensActivity.class);
                startActivity(intent);
                finish(); //현재 액티비티 파괴
            }
        });

        Button btn_open = (Button)findViewById(R.id.btn_open); //메뉴 버튼
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button btn_close = (Button)findViewById(R.id.btn_close); //메뉴 닫기 버튼
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();

        Button btn_logout = findViewById(R.id.btn_logout);
          btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 하기
                mFirebaseAuth.signOut();
                Intent intent  = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //탈퇴 처리
        //mFirebaseAuth.getCurrentUser().delete();

    }


    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {}

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {}

        @Override
        public void onDrawerStateChanged(int newState) {}
    };

}