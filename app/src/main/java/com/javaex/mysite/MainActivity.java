package com.javaex.mysite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.javaex.vo.GuestbookVo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

                // 독립해서 실행
                WriteAsyncTask writeAsyncTask = new WriteAsyncTask();
                writeAsyncTask.execute(guestVo);

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



    // METHOD - 3) 이너클래스 inner Class
    public class WriteAsyncTask extends AsyncTask<GuestbookVo, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(GuestbookVo... guestbookVo) {
            Log.d("doInBackground", "실행");
            Log.d("doInBackground", "guestVo= " + guestbookVo[0].toString());

            // vo --> json
            Gson gson = new Gson();
            String json = gson.toJson(guestbookVo[0]);
            Log.d("doInBackground", "gson --> json= " + json);
            
            // 데이터 전송(outputStream == json --> body)
            try {
                // 접속정보
                URL url = new URL("http://192.168.35.135:8088/mysite5/api/guestbook/add2");  //url 생성

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  //url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                // 전송
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                bw.write(json);
                bw.flush();

                int resCode = conn.getResponseCode(); // 응답코드 200이 정상
                Log.d("doInBackground-conn", "resCode= " + resCode);


                if(resCode == HttpURLConnection.HTTP_OK){ // 정상이면(HTTP_OK == 200)
                    // 서버에 전송
                    Log.d("onClick", "서버전송................");

                    // 리스트 액티비티로 전환 - 종료
//                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
//                    startActivity(intent);
                    finish();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}