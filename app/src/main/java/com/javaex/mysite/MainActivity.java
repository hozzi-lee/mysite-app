package com.javaex.mysite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javaex.vo.GuestbookVo;

public class MainActivity extends AppCompatActivity {

    // FIELD
    Toolbar toolbar;
    private Button btnWrite;
    private EditText edtName;
    private EditText edtPassword;
    private EditText edtContent;

    // CONSTRUCTORS

    // GETTER:SETTER

    // METHOD - 1) onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected", "home(back) button click");
        Log.d("onOptionsItemSelected", "item.getItemId()= " + item.getItemId());
        Log.d("onOptionsItemSelected", "R.id.home= " + R.id.home);
        Log.d("onOptionsItemSelected", "android.R.id.home= " + android.R.id.home);
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("onOptions - switch", "android.R.id.home");
                finish();
                return true;
            default:
                Log.d("onOptions - switch", "default");
                return super.onOptionsItemSelected(item);
        }

    }


    // METHOD - 2) onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TOOLBAR
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("방명록쓰기"); --> 액티비티.xml에 작성


        // FORM - EditText 값을 java 코드로 정의
        edtName = (EditText)findViewById(R.id.edtName);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtContent = (EditText)findViewById(R.id.edtContent);

        // FORM - btnWrite를 java 코드로 정의
        btnWrite = (Button)findViewById(R.id.btnWrite);
        // FORM - btnWrite를 클릭 했을 때
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick", "btnWrite button click");

                // 방명록 data를 객체로 묶기
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String content = edtContent.getText().toString();

                GuestbookVo guestVo = new GuestbookVo(name, password, content);
                Log.d("GuestbookVo", guestVo.toString());

//                Log.d("INFORMATION", "[name= " + name + ", password= " + password + ", content= " + content + "]");

                Toast.makeText(getApplicationContext(),
                        "INFORMATION [name= " + name + ", password= " + password + ", content= " + content + "]",
                        Toast.LENGTH_SHORT).show();

                // 서버에 전송
                Log.d("onClick", "서버전송................");


                // 리스트 액티비티로 전환
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });





        /*
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String content = edtContent.getText().toString();

                Log.d("clickButtonGroup", "" + view.getId());

                if (view.getId() == R.id.btnSave) {
                    Toast.makeText(getApplicationContext(),
                            "INFORMATION [name= " + name + ", password= " + password + ", content= " + content + "]",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(listener)
        */
    }
}