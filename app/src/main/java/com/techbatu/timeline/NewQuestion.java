package com.techbatu.timeline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techbatu.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewQuestion extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private Button bt_choose_image_question,bt_upload_image_question;
    private ImageView iv_preview_image_question;
    private EditText et_question_text_question;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        initvariables();

        initlistners();

    }

    private void initlistners() {
        bt_choose_image_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        bt_upload_image_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgUri != null  )
                {
                    uploadImage();
                }
                else{
                    Toast.makeText(NewQuestion.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImage() {

        final StorageReference ref = storageReference.child("Questions/"+Calendar.getInstance().getTime().toString());
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

                        Toast.makeText(NewQuestion.this, "Username Empty", Toast.LENGTH_SHORT).show();

                    } else {


                        Date currentTime = Calendar.getInstance().getTime();
                        String timestr = currentTime.toString();


                        Map<String, Object> params = new HashMap<>();
                        params.put("image_Uir", getdownloadUri.toString());
                        params.put("question", et_question_text_question.getText().toString());
                        params.put("username", user);
                        params.put("prn", prn);
                        params.put("timestamp",timestr);

                        firebaseFirestore.collection("questions").add(params);
                        Toast.makeText(NewQuestion.this, "Question posted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewQuestion.this,NewsFeed.class));
                    }


                }

                else {
                    // Handle failures
                    // ...
                    Toast.makeText(NewQuestion.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void chooseImage() {

        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            imgUri = result.getUri();

            Glide.with(this).load(imgUri).into(iv_preview_image_question);

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }

    private void initvariables() {
        bt_choose_image_question = findViewById(R.id.bt_choose_image_question);
        bt_upload_image_question  =findViewById(R.id.bt_upload_image_question);
        iv_preview_image_question = findViewById(R.id.iv_preview_image_question);
        et_question_text_question = findViewById(R.id.et_text_question);
    }
}
