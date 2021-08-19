package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.javaex.vo.GuestbookVo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView lvGuestbookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // 서버에서 data 받아오기 -- 임시로 가상메서드 사용
        List<GuestbookVo> gusetbookList = getListFromServer();
        Log.d("ListACT", "서버수신............");
        Log.d("serverDataGuestbookList", gusetbookList.toString());

        // ListView를 객체화
        lvGuestbookList = (ListView)findViewById(R.id.lvGuestbookList);

        // Adapter를 생성
        GuestbookListAdapter guestbookListAdapter = new GuestbookListAdapter(getApplicationContext(), R.id.lvGuestbookList, gusetbookList);

        // ListView에 Adapter를 set
        lvGuestbookList.setAdapter(guestbookListAdapter);

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
            }
        });
    }


    // 방명록 정보 만들기 (가상)
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
}