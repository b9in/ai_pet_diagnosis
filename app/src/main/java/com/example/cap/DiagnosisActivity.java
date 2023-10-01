package com.example.cap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiagnosisActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView iv_photo;
    Button sendBtn, closeBtn;

    int PICK_IMAGE_REQUEST = 100;

    int REQUEST_CAMERA_PERMISSION = 10;
    private String imageString;
    private ProgressDialog progress;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        iv_photo = findViewById(R.id.iv_photo);
        sendBtn = findViewById(R.id.sendBtn);

        closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btn_photo = findViewById(R.id.btn_photo);
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(DiagnosisActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    requestCameraPermission();
                }
            }

            private void launchCamera() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    activityResultPicture.launch(intent);
                } else {
                    Toast.makeText(DiagnosisActivity.this, "카메라 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            private void requestCameraPermission() {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // 권한 요청에 대한 설명을 사용자에게 보여줄 수도 있습니다.
                }

                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        });

        Button btn_image = findViewById(R.id.btn_image);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromAlbum();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage(bitmap);
            }
        });

        queue = Volley.newRequestQueue(this);
    }

    // 사진찍기
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();

                        bitmap = (Bitmap) extras.get("data");

                        iv_photo.setImageBitmap(bitmap);
                        //sendImage(bitmap);
                        //Intent intent1 = new Intent(DiagnosisActivity.this, ResultActivity.class);
                        //intent1.putExtra("key", value);
                        //startActivity(intent1);
                    }
                }
            });

    // 이미지 불러오기
    public void pickImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // 이미지 불러오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                // 불러온 사진을 사용하는 코드 작성
                iv_photo.setImageBitmap(bitmap);
                //sendImage(bitmap);
                //Intent intent1 = new Intent(DiagnosisActivity.this, ResultActivity.class);
                //intent1.putExtra("key", value);
                //startActivity(intent1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 이미지 플라스크로 전송
    private void sendImage(Bitmap bitmap) {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        String flask_url = "";
        StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress = new ProgressDialog(DiagnosisActivity.this);
                        progress.dismiss();
                        if (response.equals("true")) {
                            Toast.makeText(DiagnosisActivity.this, "Uploaded Successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DiagnosisActivity.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress = new ProgressDialog(DiagnosisActivity.this);
                        progress.dismiss();
                        Toast.makeText(DiagnosisActivity.this, "Some error occurred -> " + error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("imgBase64", imageString);

                return params;
            }
        };

        queue.add(request);



        Intent intent1 = new Intent(DiagnosisActivity.this, ResultActivity.class);
        //intent1.putExtra("key", value);
        startActivity(intent1);
        finish();
    }
}
