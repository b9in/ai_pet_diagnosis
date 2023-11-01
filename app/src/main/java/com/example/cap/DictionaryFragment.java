package com.example.cap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class DictionaryFragment extends Fragment {


    ListView listView;

    Context ctx;

    public DictionaryFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("질병 사전");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dictionary, container, false);

        listView = v.findViewById(R.id.listView);

        List<String> list = new ArrayList<>();
        list.add("안검염");
        //list.add("안검종양");
        list.add("안검 내반증");
        list.add("유루증");
        //list.add("색소침착성각막염");
        //list.add("핵경화");
        list.add("결막염");
        //list.add("비궤양성 각막질환");
        list.add("백내장");
        //list.add("유리체변성");

        ctx=getContext();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.list_textview, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = (String) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), DictionaryActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        return v;

    }


}