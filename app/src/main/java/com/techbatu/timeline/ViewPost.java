package com.techbatu.timeline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.techbatu.R;

import java.io.File;

public class ViewPost extends AppCompatActivity {
    String imgUri;
    PhotoView iv_post_fullscreen;
    TextView tv_likes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Intent intent = getIntent();
        String docID = intent.getStringExtra("POST_IMAGE_PATH");
        int likes = intent.getIntExtra("likes",0);
        tv_likes = findViewById(R.id.tv_likes_full);


        tv_likes.setText(likes + "Likes");

        iv_post_fullscreen = findViewById(R.id.iv_post_full);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("posts").document(docID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    imgUri = documentSnapshot.getString("image_Uir");

                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.action_like_inactive);



                    Glide.with(ViewPost.this).load(imgUri).apply(options).into(iv_post_fullscreen);

                }else{
                    Toast.makeText(ViewPost.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
