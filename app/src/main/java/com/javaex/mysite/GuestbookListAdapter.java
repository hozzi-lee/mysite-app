package com.javaex.mysite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.javaex.vo.GuestbookVo;

import java.util.List;

public class GuestbookListAdapter extends ArrayAdapter<GuestbookVo> {

    private TextView txtNo;
    private TextView txtName;
    private TextView txtDate;
    private TextView txtContent;

    // CONSTRUCTORS
    public GuestbookListAdapter(Context context, int textViewResourceId, List<GuestbookVo> items) {
        super(context, textViewResourceId, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("getView", "position= " + position);

        if (convertView == null) { // 만들어진 view가 없다 --> 만들어야 한다
            LayoutInflater layoutInflater = (LayoutInflater)LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.activity_list_item, null);

            Log.d("getView", "틀을 새로 만듬");
        }


        // 1개의 방명록 데이터 처리 --> xml 데이터 매칭
        txtNo = convertView.findViewById(R.id.txtNo);
        txtName = convertView.findViewById(R.id.txtName);
        txtDate = convertView.findViewById(R.id.txtDate);
        txtContent = convertView.findViewById(R.id.txtContent);

        // 1개의 데이터 가져오기 --> 부모쪽에 전체 리스트가 있다 position
        GuestbookVo guestbookVo = super.getItem(position);
        txtNo.setText("" + guestbookVo.getNo());
        txtName.setText(guestbookVo.getName());
        txtDate.setText(guestbookVo.getDate());
        txtContent.setText(guestbookVo.getContent());



        return convertView;
    }

    /*
    public GuestbookListAdapter(Context context, int resource, List<GuestbookVo> objects) {
        super(context, resource, objects);
    }
    */
}
