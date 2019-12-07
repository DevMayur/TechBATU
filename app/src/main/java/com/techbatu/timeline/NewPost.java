package com.techbatu.timeline;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.InternetDomainName;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.techbatu.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;



public class NewPost extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private Button bt_choose_image_post,bt_upload_image_post;
    private ImageView iv_preview_image_post;
    private EditText et_post_description;
    private Uri imgUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        initvariables();

        initlistners();

    }

    private void initlistners() {
        bt_choose_image_post.setOnClickListener(this);
        bt_upload_image_post.setOnClickListener(this);
    }

    private void initvariables() {

        bt_choose_image_post = findViewById(R.id.bt_choose_image_post);
        bt_upload_image_post = findViewById(R.id.bt_upload_image_post);
        et_post_description = findViewById(R.id.et_post_description);
        iv_preview_image_post = findViewById(R.id.iv_preview_image_post);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch(id)
        {
            case R.id.bt_choose_image_post:
                getImage();
                break;

            case R.id.bt_upload_image_post:

                 if(imgUri != null  )
                    {
                        uploadPost();
                    }
                    else{
                    Toast.makeText(NewPost.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    private void uploadPost() {

        final StorageReference ref = storageReference.child("posts/"+Calendar.getInstance().getTime().toString());
        final UploadTask uploadTask = ref.putFile(imgUri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        Uri getdownloadUri = task.getResult();


                        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info", MODE_PRIVATE);
                        String user = sharedPreferences.getString("username", "");
                        String prn = sharedPreferences.getString("prn", "");

                        if (user.equals("") && prn.equals("")) {

                            Toast.makeText(NewPost.this, "Username Empty", Toast.LENGTH_SHORT).show();

                        } else {


                            Date currentTime = Calendar.getInstance().getTime();
                            String timestr = currentTime.toString();


                            Map<String, Object> params = new HashMap<>();
                            params.put("image_Uir", getdownloadUri.toString());
                            params.put("description", et_post_description.getText().toString());
                            params.put("username", user);
                            params.put("prn", prn);
                            params.put("timestamp",timestr);

                            firebaseFirestore.collection("posts").add(params);
                            Toast.makeText(NewPost.this, "Post Created Succefully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewPost.this,NewsFeed.class));
                        }


                }

                 else {
                    // Handle failures
                    // ...
                    Toast.makeText(NewPost.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                imgUri = result.getUri();
                Picasso.with(this).load(imgUri).into(iv_preview_image_post);
            }

            else if(requestCode == CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception e = result.getError();
                Toast.makeText(NewPost.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void getImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(NewPost.this);


    }
}
