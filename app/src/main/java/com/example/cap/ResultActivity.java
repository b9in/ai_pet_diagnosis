package com.example.cap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
        String nameValue = intent.getStringExtra("name");
        String accValue = intent.getStringExtra("acc");
        String ter[] = intent.getStringArrayExtra("ter");
        //String image = intent.getStringExtra("image");

        byte[] byteArray = getIntent().getByteArrayExtra("image");

        // 바이트 배열을 비트맵으로 변환
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);

        canvas.drawBitmap(bitmap, 0, 0, null);

        /* 왼쪽 위 꼭짓점이 (100, 100)이고 오른쪽 아래 꼭지점이 (200, 200)인 사각형
         * Paint 객체의 Style은 default 값이 Style.FILL이므로 내부가 채워지도록 그려진다 */
        try {

            accValue = accValue.replace("%", "");

            Log.d("acc", accValue);

            Paint paint = new Paint(); // 페인트 객체 생성
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3); // 선의 두께 조절

            if(nameValue.equals("무증상")){
                paint.setColor(Color.GREEN);
            } else if(Integer.parseInt(accValue) > 79) {
                paint.setColor(Color.RED); // 페인트 색 지정
            } else if(Integer.parseInt(accValue) > 59) {
                paint.setColor(Color.MAGENTA);
            } else {
                paint.setColor(Color.YELLOW);
            }


            float xminValue = Float.parseFloat(ter[0]);
            float yminValue = Float.parseFloat(ter[1]);
            float widthValue = Float.parseFloat(ter[2]);
            float heightValue = Float.parseFloat(ter[3]);

            canvas.drawRect(xminValue, yminValue, widthValue, heightValue, paint);

            /* 텍스트 그리기 */
            Paint paintText = new Paint();
            paintText.setTextSize(20);
            paintText.setColor(Color.RED);

            canvas.drawText(nameValue, xminValue+10, yminValue+30, paintText);
        } catch (NumberFormatException e) {
            // Handle the exception, for example, log an error or show a message
            e.printStackTrace(); // This will print the stack trace to help you identify the issue
        }

        if (canvas != null) {
            //imgR.setImageBitmap(customViewBitmap);
            imgR.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
            txtTitle.setText(nameValue);
            txtContent.setText("무증상의 경우 영역 표시가 녹색으로 표시됩니다. \n" +
                    "영역 왼쪽 상단에 질병명이 표시되며 정확도가 높을 수록 빨간색으로 표시됩니다. \n" +
                    "\n- 정확도 표시\n" +
                    "RED > MAGENTA > YELLOW \n" +
                    "무증상 : GREEN");
        } else {
            // Handle the case where customView or its dimensions are not valid.
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
