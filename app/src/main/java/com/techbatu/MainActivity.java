package com.techbatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.techbatu.timeline.NewPost;
import com.techbatu.timeline.NewsFeed;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button bt_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv_background = findViewById(R.id.iv_background);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("Design/Techbatubackground.png");
        Glide.with(MainActivity.this)
                .load(pathReference)
                .into(iv_background);

        bt_student = findViewById(R.id.bt_student);
        bt_student.setOnClickListener(this);

        SharedPreferences LoginStatus = getSharedPreferences("status",MODE_PRIVATE);
        String status = LoginStatus.getString("status","null");
        if(status.equals("true"))
        {
            startActivity(new Intent(this,Dashboard.class));
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
     switch (id)
     {
         case R.id.bt_student:
             startActivity(new Intent(this,StudentOptions.class));
             break;
     }
    }
}