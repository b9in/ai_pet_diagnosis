package com.example.cap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    public static Activity activity;
    byte[] byteArray = null;
    String name = null;
    String species = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);

        int selectedItemId = getIntent().getIntExtra("selectedItemId", R.id.home);
        navigationBarView.post(() -> navigationBarView.setSelectedItemId(selectedItemId));

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        if(byteArray == null || name == null || species == null) {
                            switchFragment(new HomeFragment());
                        } else {
                            HomeFragment homeFragment = HomeFragment.newInstance(byteArray, name, species);
                            switchFragment(homeFragment);
                        }
                        return true;
                    case R.id.map:
                        switchFragment(new MapFragment());
                        return true;
                    case R.id.dictionary:
                        switchFragment(new DictionaryFragment());
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // SharedPreferences에서 데이터 검색
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name", null);
        species = sharedPreferences.getString("species", null);
        String imageString = sharedPreferences.getString("image", null);

        if (imageString != null) {
            byteArray = Base64.decode(imageString, Base64.DEFAULT);
        }
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containers, fragment)
                .commit();
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }


}