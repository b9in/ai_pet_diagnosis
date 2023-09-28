package com.example.cap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    public static Activity activity;


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
                        switchFragment(new HomeFragment());
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

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containers, fragment)
                .commit();
    }


}