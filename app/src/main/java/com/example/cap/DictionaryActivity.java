package com.example.cap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.w3c.dom.Text;

public class DictionaryActivity extends AppCompatActivity {

    Button closeBtn;
    LinearLayout parent;

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

    public void CreateImageView(String param, int paramParent)  {
        if(paramParent == 1) {
            parent = findViewById(R.id.parent_cause);
        } else if( paramParent == 2) {
            parent = findViewById(R.id.parent);
        }

        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView iv = new ImageView(this);
        TextView tv = new TextView(this);
        iv.setImageResource(R.drawable.dog_ic);
        tv.setText(param);

        Typeface customFont = ResourcesCompat.getFont(this, R.font.gmarket_sans_ttf_light);
        tv.setTypeface(customFont);

        int textSizeInDp_30 = 30;
        int textSizeInDp_20 = 20;
        int textSizeInPixels_30 = (int) (textSizeInDp_30 * getResources().getDisplayMetrics().density);
        int textSizeInPixels_20 = (int) (textSizeInDp_20 * getResources().getDisplayMetrics().density);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPixels_20);

        int marginInPixels = (int) (50 * getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams ivLayoutParams = new LinearLayout.LayoutParams(
                textSizeInPixels_30, textSizeInPixels_30
        );
        LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
        ivLayoutParams.setMargins(marginInPixels, 0, 0, 0);
        tvLayoutParams.setMargins(0, 0, marginInPixels, marginInPixels);

        int paddingInPixels_10 = (int) (10 * getResources().getDisplayMetrics().density);
        int paddingInPixels_3 = (int) (3 * getResources().getDisplayMetrics().density);
        tv.setPadding(paddingInPixels_10, paddingInPixels_3, 0, 0);

        horizontalLayout.addView(iv, ivLayoutParams);
        horizontalLayout.addView(tv, tvLayoutParams);

        parent.addView(horizontalLayout);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String text = intent.getStringExtra("data");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }

        TextView title_tv = findViewById(R.id.title);
        TextView subtitle = findViewById(R.id.subtitle);
        switch (text){
            case "안검염":
                CreateImageView("피부병으로 인해 발생할 수 있어요",1);
                CreateImageView("자가면역질환",1);
                CreateImageView("결막염, 각막염",1);
                CreateImageView("과민 반응",1);
                CreateImageView("눈 주위가 붉게 부어올라요",2);
                CreateImageView("눈 주위 털이 빠져요",2);
                CreateImageView("가려움증으로 인해 눈을 계속 비벼요",2);
                CreateImageView("심한 경우 고름이 생겨요",2);
                title_tv.setText("안검염");
                subtitle.setText("Blepharitis");
                break;
                /*
            case "안검종양":
                CreateImageView("안검종양의 설명",1);
                title_tv.setText("안검종양");
                break;
                */
            case "안검 내반증":
                CreateImageView("유전에 의한 경우가 대부분이에요",1);
                CreateImageView("만성 각막염이나 결막염의 심한 통증으로 눈꺼풀이 안쪽으로 휘게 되어 안검 내반이 발생될 수 있어요",1);
                CreateImageView("노견의 경우 안륜근이 약해져서 발생할 수 있어요",1);
                CreateImageView("눈물량이 늘어나요",2);
                CreateImageView("눈꺼풀 경련이 일어나요",2);
                CreateImageView("눈을 자꾸 비벼요",2);
                title_tv.setText("안검 내반증");
                subtitle.setText("Entropion");
                break;
            case "유루증":
                CreateImageView("눈물을 코로 배출시켜주는 코 눈물관(비루관)이 기능을 제대로 수행하지 못했을 경우에 발생해요",1);
                CreateImageView("각막염이나 결막염을 앓았을 경우 발생할 수 있어요",1);
                CreateImageView("눈꺼풀과 속눈썹이 눈을 찌르는 경우 발생할 수 있어요",1);
                CreateImageView("눈 주변의 털이 지속적으로 축축해지고 붉은색으로 변해요",2);
                title_tv.setText("유루증");
                subtitle.setText("Epiphora");
                break;
                /*
            case "색소침착성각막염":
                CreateImageView("색소침착성각막염의 설명",1);
                title_tv.setText("색소침착성각막염");
                break;
            case "핵경화":
                CreateImageView("핵경화의 설명",1);
                title_tv.setText("핵경화");
                break;
                 */
            case "결막염":
                CreateImageView("눈물분비샘 세포가 선천적으로 이상이 있을 경우 생겨요",1);
                CreateImageView("항생제 장기간 노출 시 생겨요",1);
                CreateImageView("홍역에 의한 호흡기 증후군 감염 시 발생할 수 있어요",1);
                CreateImageView("한쪽 눈 혹은 두 눈이 빨개요",2);
                CreateImageView("눈을 앞다리로 긁거나 바닥에 얼굴을 비벼요",2);
                CreateImageView("눈꺼풀이 충혈돼요",2);
                CreateImageView("눈물을 자주 흘려요",2);
                title_tv.setText("결막염");
                subtitle.setText("Conjunctivitis");
                break;
                /*
            case "비궤양성 각막질환":
                CreateImageView("비궤양성 각막질환의 설명",1);
                title_tv.setText("비궤양성 각막질환");
                break;

                 */
            case "백내장":
                CreateImageView("노화 시 발생할 수 있어요",1);
                CreateImageView("당뇨병에 걸린 강아지는 높은 혈당 수치로 인해 백내장에 걸릴 위험이 있어요",1);
                CreateImageView("영양결핍 시 발생할 수 있어요",1);
                CreateImageView("눈이 혼탁하고 불투명해요",2);
                CreateImageView("눈을 가늘게 뜨거나 문질러요",2);
                title_tv.setText("백내장");
                subtitle.setText("Cataract");;
                break;
                /*
            case "유리체변성":
                CreateImageView("유리체변성의 설명",1);
                title_tv.setText("유리체변성");
                break;

                 */
        }

    }
}
