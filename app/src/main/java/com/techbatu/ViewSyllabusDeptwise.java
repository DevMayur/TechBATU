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

public class ViewSyllabusDeptwise extends AppCompatActivity {

    private ImageView iv_syllabus_deptwise;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_syllabus_deptwise);

        MobileAds.initialize(this, "ca-app-pub-8579970963670342~2634189437");

        mAdView = findViewById(R.id.adView_deptwiise);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        iv_syllabus_deptwise = findViewById(R.id.iv_syllabus_deptwise);

        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
        String dept = sharedPreferences.getString("department","");
        String sem = sharedPreferences.getString("semester","");
        String subname = sharedPreferences.getString("subname","");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("uploads/"+dept+"/"+sem+subname+".jpg");

        Glide.with(ViewSyllabusDeptwise.this)
                .load(pathReference)
                .into(iv_syllabus_deptwise);

    }
}
