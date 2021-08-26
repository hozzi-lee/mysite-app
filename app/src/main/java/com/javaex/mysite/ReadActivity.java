package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javaex.vo.GuestbookVo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private TextView txtNo;
    private TextView txtName;
    private TextView txtDate;
    private TextView txtContent;
    private Button btnGoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        // layout id값 java 코드로 정의
        txtNo = (TextView)findViewById(R.id.txtNo);
        txtName = (TextView)findViewById(R.id.txtName);
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtContent = (TextView)findViewById(R.id.txtContent);

        btnGoList = (Button)findViewById(R.id.btnGoList);
        btnGoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 리스트로 돌아가기
//                Intent intent = new Intent(ReadActivity.this, ListActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        
        // intent로 넘어온 data값 꺼내기
        Intent intent = getIntent();
        int no = intent.getExtras().getInt("no");
        String name = intent.getExtras().getString("name");

        Log.d("ReadActivity_onCreate", "no= " + no);
        Log.d("ReadActivity_onCreate", "name= " + name);

        /* TEST no 여러개
        int no2 = 123;
        int no3 = 456;
        */

        // -----------------------> AsyncTask 사용 innerClass
        ReadAsyncTask readAsyncTask = new ReadAsyncTask();
        readAsyncTask.execute(no);
        // TEST no 여러개
//        readAsyncTask.execute(no, no2, no3);

    }


    // METHOD - 2) innerClass
    public class ReadAsyncTask extends AsyncTask<Integer, Integer, GuestbookVo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GuestbookVo doInBackground(Integer... noArray) {
            Log.d("doInBackground", "start check");

            // Gson Instance --> Gradle에 GSON 설치
            Gson gson = new Gson();

            // 보내는 guestbookVo
            GuestbookVo requestVo = null;

            // 받는 guestbookVo
            GuestbookVo responseVo = null;

            /* Array TEST
            int no2 = Integers[1];
            int no3 = Integers[2];
            Log.d("doInBackground", "no= " + no2);
            Log.d("doInBackground", "no= " + no3);
            */

            int no = noArray[0];
            Log.d("doInBackground", "no= " + no);
            requestVo = new GuestbookVo();
            requestVo.setNo(no);

            // vo --> json
            String requestJson = gson.toJson(requestVo);
            Log.d("requestJson", requestJson);

            // no 데이터에 대한 정보 요청
            // REQUEST --> body에 requestJson을 넣는다
            try {
                URL url = new URL("http://192.168.35.135:8088/mysite5/api/guestbook/read");  // url 생성

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  // url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); // 요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); // 응답시 데이터 형식 json
                conn.setDoOutput(true); // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); // InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                // RequestBody에 data보내기. json(GuestbookVo에 no를 담아서 보낸다)
                // 보내는(RequestBody) Stream 통신 --> 데이터 형식은 json
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter bfw = new BufferedWriter(osw);

                bfw.write(requestJson);
                bfw.flush();
                
                // 받는(ResponseBody) Stream 통신 --> 데이터 형식은 json
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader bfr = new BufferedReader(isr);

                ////////////////////////////////////////////////////////////////////

                int resCode = conn.getResponseCode(); // 응답(Response)코드 200이 정상
                Log.d("REQUEST", "resCode= " + resCode);

                if(resCode == HttpURLConnection.HTTP_OK){ // 정상이면(HTTP_OK == 200)
                    // 응답 ResponseBody
                    String responseJson = "";
                    while(true) {
                        String line = bfr.readLine();
                        if (line == null) {
                            break;
                        }
                        responseJson += line;
                    }
                    Log.d("responseJson", responseJson);

                    // json(문자열) --변환--> java
                    responseVo = gson.fromJson(responseJson, GuestbookVo.class);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 정보 출력
            return responseVo;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(GuestbookVo guestbookVo) {
            super.onPostExecute(guestbookVo);
            Log.d("onPostExecute", "onPostExecute");
            Log.d("onPostExecute", "guestbookVo= " + guestbookVo);
            
            // 화면에 출력
            txtNo.setText("" + guestbookVo.getNo());
            txtName.setText(guestbookVo.getName());
            txtDate.setText(guestbookVo.getDate());
            txtContent.setText(guestbookVo.getContent());

        }
    }
}