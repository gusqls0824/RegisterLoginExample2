package com.test.regitserloginexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private FirebaseUser currentUser; // 로그인 된 유저 정보
    private TextInputLayout textinput;
    private TextInputLayout textinput2;
    private AppCompatEditText mEtEmail, mEtPwd; // 로그인 입력필드


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 확인
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RegisterLoginExample"); //Firebase의 프로젝트와 연동

        textinput = (TextInputLayout) findViewById(R.id.textinput);
        textinput2 = (TextInputLayout) findViewById(R.id.textinput2);
        mEtEmail = (AppCompatEditText) findViewById(R.id.et_email);
        mEtPwd = (AppCompatEditText) findViewById(R.id.et_pwd);

        mEtEmail.addTextChangedListener(idTextWatcher);
        mEtPwd.addTextChangedListener(pwTextWatcher);

        textinput.setCounterEnabled(true);
        textinput2.setCounterEnabled(true);

        textinput.setCounterMaxLength(20);
        textinput2.setCounterMaxLength(15);

        textinput.setErrorEnabled(true);
        textinput2.setErrorEnabled(true);


        Button btn_login = findViewById(R.id.btn_login); //로그인 버튼

        btn_login.setOnClickListener(new View.OnClickListener() { //로그인 버튼 클릭 시 이벤트 실행
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                if (strEmail.length() == 0 ){ //아이디에 빈칸일 경우
                    Toast.makeText(LoginActivity.this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    mEtEmail.requestFocus();
                    return;
                }
                if (strPwd.length() == 0 ){ //비밀번호에 빈칸일 경우
                    Toast.makeText(LoginActivity.this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    mEtPwd.requestFocus();
                    return;
                }
                //Firebase 내의 데이터베이스와 연동하여 확인하기 위한 코드
                mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("RegisterLoginExample",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("IsLoggedIn", true); // 로그인 상태 저장
                            editor.putString("userEmail", strEmail); // 사용자 이메일 저장
                            editor.apply();
                            //로그인 성공 !!!
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userEmail", strEmail); // 'strEmail'라는 키로 사용자 이메일 데이터를 전달
                            startActivity(intent);
                            finish(); //현재 액티비티 파괴
                        } else {
                            Toast.makeText(LoginActivity.this,"이메일 혹은 비밀번호를 확인하세요.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button btn_register = findViewById(R.id.btn_register); //회원가입 버튼
        btn_register.setOnClickListener(new View.OnClickListener() { //회원가입 버튼 클릭시 이벤트 실행
            @Override
            public void onClick(View view) {
                //회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    TextWatcher idTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()>20){
                textinput.setError("20자 이하로 작성해주세요.");
            } else {
                textinput.setError(null);
            }
        }
    };

    TextWatcher pwTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()>15){
                textinput2.setError("15자 이하로 작성해주세요.");
            } else {
                textinput2.setError(null);
            }
        }
    };
}