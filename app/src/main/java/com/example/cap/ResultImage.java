package com.example.cap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

public class ResultImage extends androidx.appcompat.widget.AppCompatImageView {

    private String name;
    private String acc;
    private String[] ter;
    private Bitmap bitmap;

    public ResultImage(Context context, String name, String acc, String[] ter, Bitmap bitmap) {
        super(context);
        this.name = name;
        this.acc = acc;
        this.ter = ter;
        this.bitmap = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas = new Canvas(bitmap);

        Paint paint = new Paint(); //페인트 객체 생성
        paint.setColor(Color.RED); //페인트 색 지정
        paint.setStyle(Paint.Style.STROKE);

        /*왼쪽 위 꼭짓점이 (100, 100)이고 오른쪽 아래 꼭지점이 (200, 200)인 사각형
         * Paint 객체의 Style은 default 값이 Style.FILL이므로 내부가 채워지도록 그려진다*/
        canvas.drawRect(Integer.getInteger(ter[0]), Integer.getInteger(ter[2]), Integer.getInteger(ter[1]), Integer.getInteger(ter[3]), paint);

        /* 텍스트 그리기 */
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText("정확도 : " + acc, 100, 800, paint);
    }

}
