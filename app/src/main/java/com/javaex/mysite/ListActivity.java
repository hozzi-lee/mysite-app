package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javaex.vo.GuestbookVo;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView lvGuestbookList;
    private Button btnGoWriteForm;

    // METHOD - 1) onCreate 실행 될 때
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // ListView를 객체화
        lvGuestbookList = (ListView)findViewById(R.id.lvGuestbookList);
        
        //////////////////////////////////////////////////////////////
        // 서버에서 data 받아오기(가상) -- 임시로 만든 가상메서드(List) 사용
        /*
        List<GuestbookVo> gusetbookList = getListFromServer();
        Log.d("ListACT", "서버수신............");
        Log.d("serverDataGuestbookList", gusetbookList.toString());
        */

        // 이너클래스(ListAsyncTask)의 onPostExecute()메서드로 이동
        /*
        // Adapter를 생성
        GuestbookListAdapter guestbookListAdapter = new GuestbookListAdapter(getApplicationContext(), R.id.lvGuestbookList, gusetbookList);

        // ListView에 Adapter를 set
        lvGuestbookList.setAdapter(guestbookListAdapter);
        */

        //////////////////////////////////////////////////////////////
        // 글쓰기 버튼 event
        btnGoWriteForm = (Button)findViewById(R.id.btnGoWriteForm);
        btnGoWriteForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("btnGoWriteForm", "button click");

                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // METHOD - 2) onResume 숨겨졌다가 다시 보일 때 리스트 갱신을 위한
    @Override
    protected void onResume() {
        super.onResume();

        ///////////////////////////////////////////////////////////
        // 리스트 관련 작업

        ///////////////////////////////////////////////////////////
        // 독립해서 코드 진행 --> getData --> 화면출력(adapter/set)
        ListAsyncTask listAsyncTask = new ListAsyncTask();
        listAsyncTask.execute();

        // click event
        lvGuestbookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 현재 클릭한 리스트 view의 index 값
                Log.d("onItemClick", "index= " + i);

                // 클릭한 view의 값을 가져온다
                TextView txtContent = (TextView)view.findViewById(R.id.txtContent);
                Log.d("viewContent", "txtContent= " + txtContent.getText().toString());

                // view에 보여지지 않은 데이터를 가져올때 --> List의 값을 사용할 때
                GuestbookVo gVo = (GuestbookVo)adapterView.getItemAtPosition(i);
                Log.d("ListData", "vo= " + gVo);
                Log.d("ListData", "vo.date= " + gVo.getDate());

                // 클릭한 아이템의 pk값 읽어오기
                int no = gVo.getNo();
                Log.d("ListData", "vo.no= " + no);

                // 액티비티 전환(Intent ReadActivity) - 데이터 보내기(putExtra 키, 값) 글 번호(no)
                Intent intent = new Intent(ListActivity.this, ReadActivity.class);
                intent.putExtra("no", no);
                intent.putExtra("name", gVo.getName());

                startActivity(intent);
            }
        });


    }

    // METHOD - 3) innerClass
    public class ListAsyncTask extends AsyncTask<Void, Integer, List<GuestbookVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<GuestbookVo> doInBackground(Void... voids) {
            List<GuestbookVo> guestbookList = null;

            // SERVER CONNECTION

            // REQUEST
            try {
                URL url = new URL("http://192.168.35.135:8088/mysite5/api/guestbook/list");  // url 생성

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  // url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); // 요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); // 응답시 데이터 형식 json
                conn.setDoOutput(true); // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); // InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                int resCode = conn.getResponseCode(); // 응답코드 200이 정상
                Log.d("REQUEST", "" + resCode);

                if(resCode == 200){ //정상이면

                    //Stream 을 통해 통신한다
                    //데이타 형식은 json으로 한다.
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader bfr = new BufferedReader(isr);

                    String jsonData = "";
                    while (true) {
                        String line = bfr.readLine();
                        if (line == null) {
                            break;
                        }

                        jsonData += line;
                    }

                    // 응답 RESPONSE json(문자열) --변환--> java(List)
                    // Gradle에 GSON 설치
                    Log.d("BufferedReader", "jsonData= " + jsonData);

                    Gson gson = new Gson();
                    guestbookList = gson.fromJson(jsonData, new TypeToken<List<GuestbookVo>>(){}.getType());
                    Log.d("RESPONSE json - java", "size= " + guestbookList.size());
                    Log.d("RESPONSE json - java", "index(0).name= " + guestbookList.get(0).getName());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return guestbookList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<GuestbookVo> guestbookList) {
            super.onPostExecute(guestbookList);
            Log.d("onPostExecute", "onPostExecute()");

            Log.d("RESPONSE json - java", "size= " + guestbookList.size());
            Log.d("RESPONSE json - java", "index(0).name= " + guestbookList.get(0).getName());

            // Adapter를 생성
            GuestbookListAdapter guestbookListAdapter = new GuestbookListAdapter(getApplicationContext(), R.id.lvGuestbookList, guestbookList);

            // ListView에 Adapter를 set
            lvGuestbookList.setAdapter(guestbookListAdapter);
        }
    }


    // 방명록 임시 정보 만들기 (가상)
    /*
    public List<GuestbookVo> getListFromServer() {
        List<GuestbookVo> gusetbookList = new ArrayList<GuestbookVo>();

        for(int i = 0; i < 50; i++) {
            GuestbookVo guestbookVo = new GuestbookVo();
            guestbookVo.setNo(i);
            guestbookVo.setName("정우성" + i);
            guestbookVo.setDate(i + " - 2021. 8. 19.");
            guestbookVo.setContent(i + "번째 본문입니다.");

            gusetbookList.add(guestbookVo);
        }
        return gusetbookList;
    }
    */

}