package com.example.cap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    Button closeBtn, btnMap, btnDictionary;
    TextView txtTitle, txtContent;
    ImageView imgR;

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
        imgR = findViewById(R.id.imgR);

        Intent intent = getIntent();
        String imgValue = intent.getStringExtra("img");
        String nameValue = intent.getStringExtra("name");
        String accValue = intent.getStringExtra("acc");
        Bitmap bitmap = null;

        try {
            byte[] decodedString = Base64.decode(imgValue, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            imgR.setImageBitmap(bitmap);
            txtTitle.setText(nameValue);
            txtContent.setText("정확도 : " + accValue);

        } else {
            // 변환에 실패한 경우 처리할 코드
            txtTitle.setText("Error 발생");
            txtContent.setText("다시 시도해주세요.");
        }


        closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //imgR.setImageResource(R.drawable.results_ex);
        //txtTitle.setText("결막염");
        /*
        txtContent.setText("\"강아지결막염은 눈꺼풀의 안쪽과 흰 눈의 표면(공막)으로 이루어진 결막에 염증이 생긴 질병을 의미합니다. \n\n" +
                        " 강아지에게 발생하는 안과 질환 중 가장 잦게 나타나는 질병으로 알려져있습니다. \n\n" +
                                        "결막염에 걸리면 나타나는 증상은 눈곱이 많이 끼고, 눈이 충혈됩니다. \n\n" +
                        " 만약 강아지가 눈을 자주 비비거나, 바닥에 얼굴을 문지르는 행동 반복하면 결막염을 의심해볼 수 있습니다. \n\n" +
                        "또 눈을 제대로 뜨지 못하고, 자주 깜빡거리는 모습을 보이기도 합니다.\n\n" +
                        " 결막이 붓고 부풀어 오르는 결막부종의 증상도 나타납니다. \n\n"
                                        );
        */
        btnMap = findViewById(R.id.btnMap);
        btnDictionary = findViewById(R.id.btnDictionary);
        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("결과 페이지");
        }


        btnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.putExtra("selectedItemId", R.id.map);
                startActivity(intent);
                MainActivity MA = (MainActivity) MainActivity.activity;
                MA.finish();
                finish();
            }
        });

        btnDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ResultActivity.this, MainActivity.class);
                intent1.putExtra("selectedItemId", R.id.dictionary);
                startActivity(intent1);
                MainActivity MA = (MainActivity) MainActivity.activity;
                MA.finish();
                finish();
            }
        });

    }
}
