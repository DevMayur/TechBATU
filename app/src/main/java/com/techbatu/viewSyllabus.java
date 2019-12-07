package com.techbatu;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class viewSyllabus extends AppCompatActivity {

    private ImageView iv_syllabus;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_syllabus);

        iv_syllabus = findViewById(R.id.iv_syllabus);

        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
        String dept = sharedPreferences.getString("department","");
        String sem = sharedPreferences.getString("semester","");
        String subname = sharedPreferences.getString("subname","");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("uploads/"+sem+subname+".jpg");
        Glide.with(viewSyllabus.this)
                .load(pathReference)
                .into(iv_syllabus);

    }
}
