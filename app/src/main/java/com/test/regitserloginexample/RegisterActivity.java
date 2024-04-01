package com.test.regitserloginexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.io.InputStream;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private FirebaseUser currentUser; // 로그인 된 유저 정보
    private Button mBtnRegister; // 회원가입 버튼
    private TextInputLayout textinput;
    private TextInputLayout textinput2;
    private AppCompatEditText mEtEmail, mEtPwd; // 회원가입 입력필드
    private TextInputLayout pwdchecklayout;
    private AppCompatEditText pwdcheck;
    private Button pwdcheckbutton; //비밀번호 확인 버튼
    private Button btnBack; //돌아가기 버튼
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 확인
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RegisterLoginExample"); //Firebase의 프로젝트와 연동

        pwdchecklayout = (TextInputLayout) findViewById(R.id.pwdchecklayout);
        pwdcheck =  (AppCompatEditText) findViewById(R.id.pwdcheck);
        mBtnRegister = findViewById(R.id.btn_register);
        textinput = (TextInputLayout) findViewById(R.id.textinput);
        textinput2 = (TextInputLayout) findViewById(R.id.textinput2);
        mEtEmail = (AppCompatEditText) findViewById(R.id.et_email);
        mEtPwd = (AppCompatEditText) findViewById(R.id.et_pwd);
        btnBack = (Button) findViewById(R.id.btn_back);
        pwdcheckbutton = (Button) findViewById(R.id.pwdcheckbutton);

        SharedPreferences settings = getSharedPreferences("PREFS",0);
        password = settings.getString("password","");

        mEtEmail.addTextChangedListener(idTextWatcher); // 텍스트 이메일
        mEtPwd.addTextChangedListener(pwTextWatcher); // 텍스트 비밀번호

        textinput.setCounterEnabled(true);
        textinput2.setCounterEnabled(true);

        textinput.setCounterMaxLength(20); //최대길이 20
        textinput2.setCounterMaxLength(15); //최대길이 15

        textinput.setErrorEnabled(true); //에러가 있을 경우 표기
        textinput2.setErrorEnabled(true); //에러가 있을 경우 표기



        btnBack.setOnClickListener(new View.OnClickListener() { //돌아가기 버튼
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); //회원가입 화면에서 로그인 화면으로 이동
                startActivity(intent);
                finish(); //다른 액티비티로 넘어 갈 경우 현재 액티비티 파괴
            }
        });
        pwdcheckbutton.setOnClickListener(new View.OnClickListener() { //비밀번호 확인 버튼
            @Override
            public void onClick(View view) {
                String userPass1 = mEtPwd.getText().toString();
                String userPass2 = pwdcheck.getText().toString();

                if(userPass1.equals("") || userPass2.equals("")){ //비밀번호가 동일한지 확인 하는 의문문
                    Toast.makeText(RegisterActivity.this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                } else {
                    if(userPass1.equals(userPass2)){
                        SharedPreferences settings = getSharedPreferences("PREFS",0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password",userPass1);
                        editor.apply();
                        Toast.makeText(RegisterActivity.this,"비밀번호가 동일합니다.",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegisterActivity.this, "비밀번호가 틀렸습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        mBtnRegister.setOnClickListener(new View.OnClickListener() { //가입완료 버튼
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                if (strEmail.length() == 0 ){
                    Toast.makeText(RegisterActivity.this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    mEtEmail.requestFocus();
                    return;
                }
                if (strPwd.length() == 0 ){
                    Toast.makeText(RegisterActivity.this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    mEtPwd.requestFocus();
                    return;
                }

                // Firebase Auth 진행 (인증 진행)
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                UserAccount account = new UserAccount();
                                account.setIdToken(firebaseUser.getUid()); //사용자의 고유 값
                                account.setEmailId(firebaseUser.getEmail()); //사용자의 이메일
                                account.setPassword(strPwd); //사용자의 패스워드
                                // setValue : database에 insert (삽입) 행위
                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this,"회원가입에 실패하셨습니다. @를 포함한 다른 이메일이나 비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }
        });
    }



    TextWatcher idTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
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


