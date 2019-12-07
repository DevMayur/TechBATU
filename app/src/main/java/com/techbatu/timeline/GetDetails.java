package com.techbatu.timeline;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.techbatu.LoginPage;
import com.techbatu.R;
import com.techbatu.Upload;

import java.util.HashMap;
import java.util.Map;

public class GetDetails extends AppCompatActivity implements View.OnClickListener {

    public static int PICK_IMAGE_REQUEST_SIGNUP=1;

    private StorageTask uploadTask;
    private Uri iv_uri;
    private ImageView iv_preview_image;
    private EditText et_email,et_first_name_signup,et_last_name_signup,et_username_signup,et_password_signup,et_prn_signup;
    private Button bt_choose_profile_signup,bt_sign_up_signup;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask muploadTask;
    private  FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);

        initvariables();
        initlistners();


        storageReference = FirebaseStorage.getInstance().getReference("profile/profile_pic");
        databaseReference  = FirebaseDatabase.getInstance().getReference("profile/profile_pic");




    }



    private void initlistners() {

        bt_sign_up_signup.setOnClickListener(this);
        bt_choose_profile_signup.setOnClickListener(this);

    }

    private void initvariables() {

        et_email = findViewById(R.id.et_email);
        iv_preview_image = findViewById(R.id.iv_preview_image);
        et_first_name_signup = findViewById(R.id.et_first_name_signup);
        et_last_name_signup = findViewById(R.id.et_last_name_signup);
        et_username_signup = findViewById(R.id.et_username_signup);
        et_password_signup = findViewById(R.id.wt_password_signup);
        et_prn_signup = findViewById(R.id.et_prn_signup);
        bt_choose_profile_signup = findViewById(R.id.bt_choose_profile_signup);
        bt_sign_up_signup = findViewById(R.id.bt_sign_up_signup);
    }

    private void openfilechooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST_SIGNUP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST_SIGNUP && resultCode == RESULT_OK &&
                data != null &&data.getData()!=null)
        {
            iv_uri = data.getData();
            Picasso.with(this).load(iv_uri).into(iv_preview_image);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.bt_choose_profile_signup:
                openfilechooser();
                break;

            case R.id.bt_sign_up_signup:
                if(muploadTask != null && muploadTask.isInProgress())
                {
                    Toast.makeText(this, "Upload is in Progress", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(et_prn_signup.getText().toString().length() == 20)
                    {
                        uploadprofile();
                        try{
                            uploaddetaiils();}
                        catch (IllegalArgumentException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Wrong PRN", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
                default:
                    Toast.makeText(this, "Something went Wrong!", Toast.LENGTH_SHORT).show();


        }
    }


    private String getFileExtention(Uri localuri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(localuri));
    }

    private void uploadprofile() {
        if(iv_uri !=null){
            StorageReference fileReference = storageReference.child(et_prn_signup.getText().toString()+"."+getFileExtention(iv_uri));
            muploadTask = fileReference.putFile(iv_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Upload upload = new Upload(et_prn_signup.getText().toString().trim()
                            ,taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    String uploadID = databaseReference.push().getKey();
                    databaseReference.child(et_prn_signup.getText().toString().trim()).setValue(upload);
                    Toast.makeText(GetDetails.this, "Profile Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                    Toast.makeText(GetDetails.this, "Error - "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void uploaddetaiils() {
        final String first_name = et_first_name_signup.getText().toString();
        final String last_name = et_last_name_signup.getText().toString();
        final String username = et_username_signup.getText().toString();
        final String password = et_password_signup.getText().toString();
        final String prn = et_prn_signup.getText().toString();
        final String full_name = et_first_name_signup.getText().toString() + " " + et_last_name_signup.getText().toString();
        Intent intent = new Intent(this,BlogRecyclerAdapter.class);
        intent.putExtra("prn",prn);

        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
        final String phone = sharedPreferences.getString("phone","");

        Map<String,Object> params = new HashMap<>();
        params.put("phone",phone);
        params.put("first_name",first_name);
        params.put("last_name",last_name);
        params.put("full_name",full_name);
        params.put("username",username);
        params.put("password",password);
        params.put("prn",prn);

        firebaseFirestore.collection("Users").document(prn).set(params)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GetDetails.this, "Account Cretaed Succesfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GetDetails.this,LoginPage.class));
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                e.printStackTrace();;
                Toast.makeText(GetDetails.this, "Something went wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        }

}
