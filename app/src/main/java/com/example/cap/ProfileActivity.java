package com.example.cap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Button closeBtn, profileImageBtn, updateBtn;
    EditText name, species;
    CircleImageView circleIv;
    int PICK_IMAGE_REQUEST = 100;
    private Bitmap receivedBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        species = findViewById(R.id.species);
        circleIv = findViewById(R.id.circle_iv);

        closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileImageBtn = findViewById(R.id.profileImageBtn);

        profileImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromAlbum();
            }
        });

        updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] byteArray = bitmapToByteArray(receivedBitmap);

                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name.getText().toString());
                editor.putString("species", species.getText().toString());
                editor.putString("image", Base64.encodeToString(byteArray, Base64.DEFAULT));
                editor.apply();

                setResult(RESULT_OK);
                Intent restartIntent = new Intent(MainActivity.activity, MainActivity.class);
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.activity.startActivity(restartIntent);
                MainActivity.activity.finish();

            }
        });

// 다른 코드

// ProfileActivity 종료 후 결과 처리
        ActivityResultLauncher<Intent> profileActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {

                        }
                    }
                });


}

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // 이미지를 PNG 형식으로 압축
        return stream.toByteArray();
    }


    public void pickImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                receivedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);


                Bitmap resizedBitmap = Bitmap.createScaledBitmap(receivedBitmap,receivedBitmap.getWidth(), receivedBitmap.getHeight(), true);

                circleIv.setImageBitmap(resizedBitmap);
                circleIv.setVisibility(View.VISIBLE);
                profileImageBtn.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

