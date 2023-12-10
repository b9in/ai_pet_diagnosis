package com.example.cap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class DiagnosisActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView iv_photo;
    Button sendBtn, closeBtn;
    private ProgressDialog progressDialog;

    int PICK_IMAGE_REQUEST = 100;

    int REQUEST_CAMERA_PERMISSION = 10;
    private String imageString;
    private ProgressDialog progress;
    private RequestQueue queue;
    String responseStr;

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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                iv_photo.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // 이미지 플라스크로 전송
    private void sendImage(Bitmap bitmap) {

        File imageFile = bitmapToFile(bitmap);

        progressDialog = new ProgressDialog(DiagnosisActivity.this);
        progressDialog.setMessage("Loading..."); // ProgressDialog에 표시될 메시지 설정
        progressDialog.setCancelable(false); // ProgressDialog를 취소할 수 없도록 설정
        progressDialog.show(); // ProgressDialog 표시

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        String flask_url = BuildConfig.FLASK_URL;

        File img = imageFile;
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, flask_url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progress = new ProgressDialog(DiagnosisActivity.this);
                        progress.dismiss();
                        progressDialog.dismiss();
                        try {
                            //Toast.makeText(DiagnosisActivity.this, "Uploaded Successful", Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            // JSON 데이터를 파싱하고 특정 키에 따라 변수에 저장
                            String name = jsonObject.getString("class_info"); // 질병이름(숫자로나옴)
                            //String img = jsonObject.getString("img"); //이미지
                            //String splitText = img.replace("b'","");
                            //String splitText = img.substring(2, img.length()-2);

                            String xmin = jsonObject.getString("xmin");
                            String ymin = jsonObject.getString("ymin");
                            String width = jsonObject.getString("width");
                            String height = jsonObject.getString("height");

                            String ter[] = {xmin, ymin, width, height};

                            Log.i("t",xmin+ymin+width+height);

                            String acc = jsonObject.getString("acc"); //정확도

                            Intent intent1 = new Intent(DiagnosisActivity.this, ResultActivity.class);
                            intent1.putExtra("name", name);
                            //intent1.putExtra("img", splitText);
                            intent1.putExtra("acc", acc);
                            intent1.putExtra("ter", ter);
                            intent1.putExtra("image", imageBytes);
                            startActivity(intent1);

                            finish();
                        } catch (Exception e){
                            Toast.makeText(DiagnosisActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress = new ProgressDialog(DiagnosisActivity.this);
                        progress.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(DiagnosisActivity.this, "Some error occurred -> " + error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("img", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        //Volley.newRequestQueue(this).add(request);

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                20000 ,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

    }

    private File bitmapToFile(Bitmap bitmap) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, "image.jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageFile;
    }


}
