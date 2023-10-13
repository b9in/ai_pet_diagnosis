package com.example.cap;

import static android.app.Activity.RESULT_OK;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Cap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    Button directBtn, profileBtn;
    private CircleIndicator indicator;

    public static HomeFragment newInstance(byte[] byteArray, String name, String species) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putByteArray("image", byteArray);
        args.putString("name", name);
        args.putString("species", species);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("반려동물 안구 자가진단");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        CircleImageView circleIv = v.findViewById(R.id.circle_iv);
        TextView mainName = v.findViewById(R.id.mainName);
        TextView mainSpecies = v.findViewById(R.id.mainSpecies);

        Bundle args = getArguments();
        if(args != null) {
            byte[] byteArray = args.getByteArray("image");
            Bitmap bitmap = byteArrayToBitmap(byteArray);
            String name = args.getString("name");
            String species = args.getString("species");

            if (bitmap != null && name != null && species != null) {
                circleIv.setImageBitmap(bitmap);
                mainName.setText(name);
                mainSpecies.setText(species);

            } else {
                circleIv.setImageResource(R.drawable.test_img);
                mainName.setText("누렁이");
                mainSpecies.setText("KOKONI");
            }
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        directBtn = v.findViewById(R.id.directBtn);

        directBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), DiagnosisActivity.class);
                startActivity(intent1);
            }
        });

        profileBtn = v.findViewById(R.id.profileBtn);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent1);
            }
        });

        ViewPager pager = v.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3); //3개까지 caching

        MainPagerAdapter adapter = new MainPagerAdapter(activity.getSupportFragmentManager(), 1);

        Quation1 quation1 = new Quation1();
        adapter.addItem(quation1);

        Quation2 quation2 = new Quation2();
        adapter.addItem(quation2);

        Quation3 quation3 = new Quation3();
        adapter.addItem(quation3);

        pager.setAdapter(adapter);

        indicator = v.findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        return v;
    }


    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}