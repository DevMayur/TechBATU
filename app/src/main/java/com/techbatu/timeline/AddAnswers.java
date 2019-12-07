package com.techbatu.timeline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techbatu.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;

public class AddAnswers extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_que_username,tv_question;
    private EditText et_answer;
    private ImageView iv_question_image;
    private ImageView iv_answer_image;
    private Uri answeruri;
    private Button bt_save_answer;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private String postId;
    private RecyclerView recyclerView;
    private List<AnswerModel> alist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answers);

        tv_que_username = findViewById(R.id.tv_username_question_add_answer);
        tv_question = findViewById(R.id.tv_question_add_answer);
        et_answer = findViewById(R.id.et_answertext_add_answer);
        iv_answer_image = findViewById(R.id.iv_answerimage_add_answer);
        iv_question_image = findViewById(R.id.iv_questionimage_add_answer);
        bt_save_answer = findViewById(R.id.bt_save_answer);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        recyclerView = findViewById(R.id.all_answers_recycler_view);
        alist = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final AnswerAdapter answerAdapter = new AnswerAdapter(alist,this);
        recyclerView.setAdapter(answerAdapter);


        iv_answer_image.setOnClickListener(this);
        bt_save_answer.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("add_answer_to",Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username","");
        String question = sharedPreferences.getString("question","");
        String imgUri = sharedPreferences.getString("imgUri","");
        postId = sharedPreferences.getString("postId","");


        tv_que_username.setText(username);
        tv_question.setText(question);
        Glide.with(this).load(imgUri).into(iv_question_image);


        firebaseFirestore.collection("answers/"+postId+"/answer").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty())
                {

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String blogPostId = doc.getDocument().getId();
                            AnswerModel answerModel = doc.getDocument().toObject(AnswerModel.class).withId(blogPostId);
                            alist.add(answerModel);
                            answerAdapter.notifyDataSetChanged();
                        }
                    }

                }else{

                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_answerimage_add_answer:

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddAnswers.this);

                break;


            case R.id.bt_save_answer:

                if(answeruri!=null)
                {
                    uploadImage();
                }else
                {
                    Toast.makeText(this, "Please Select an Image!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if(requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                answeruri =  result.getUri();
                Glide.with(this).load(answeruri).into(iv_answer_image);
            }
        }
        if(requestCode == CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
        {
            Exception e = result.getError();
            Toast.makeText(AddAnswers.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void uploadImage() {

        final StorageReference ref = storageReference.child("Answers/"+postId+"/"+Calendar.getInstance().getTime().toString());
        final UploadTask uploadTask = ref.putFile(answeruri);

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

                        Toast.makeText(AddAnswers.this, "Username Empty", Toast.LENGTH_SHORT).show();

                    } else {


                        Date currentTime = Calendar.getInstance().getTime();
                        String timestr = currentTime.toString();


                        Map<String, Object> params = new HashMap<>();
                        params.put("image_Uir", getdownloadUri.toString());
                        params.put("answer", et_answer.getText().toString());
                        params.put("username", user);
                        params.put("prn", prn);
                        params.put("timestamp",timestr);

                        firebaseFirestore.collection("answers/"+postId+"/answer").add(params);
                        Toast.makeText(AddAnswers.this, "Answer Saved Added Successful !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddAnswers.this,NewsFeed.class));
                    }


                }

                else {
                    // Handle failures
                    // ...
                    Toast.makeText(AddAnswers.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
