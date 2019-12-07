package com.techbatu.timeline;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.techbatu.R;

public class ViewFullAnswer extends AppCompatActivity {

    private String answer,imageUir,username;
    private TextView tv_answer,tv_username;
    private PhotoView iv_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_answer);

        tv_answer = findViewById(R.id.tv_answer_full_view);
        tv_username = findViewById(R.id.tv_username_answer_full_view);
        iv_imageView = findViewById(R.id.iv_image_answer_full_view);

        SharedPreferences sharedPreferences = getSharedPreferences("Answer_Adapter",Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username","");
        imageUir = sharedPreferences.getString("image_Uir","");
        answer = sharedPreferences.getString("answer","");

        tv_username.setText(username);
        tv_answer.setText(answer);
        Glide.with(this).load(imageUir).into(iv_imageView);

    }
}
