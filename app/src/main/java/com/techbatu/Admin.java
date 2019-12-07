package com.techbatu;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Admin extends AppCompatActivity implements View.OnClickListener {


    public static final int PICK_IMAGE_REQUEST=1;
    private Button bt_choose_file,bt_upload;
    private ImageView iv_preview_upload;
    private EditText et_enter_name;
    private Uri iv_uri;
    private StorageTask uploadTask;
    private String setdownloadUrl;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        initvariables();

        initlistners();
    }

    private void initlistners() {

        bt_choose_file.setOnClickListener(this);
        bt_upload.setOnClickListener(this);

    }

    private void initvariables() {
        bt_choose_file = findViewById(R.id.bt_choose_file);
        bt_upload = findViewById(R.id.bt_upload);
        iv_preview_upload = findViewById(R.id.iv_preview_upload);
        et_enter_name = findViewById(R.id.et_enter_name);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id)
        {
            case R.id.bt_choose_file:
                openFilechooser();
                break;

            case R.id.bt_upload:

                if(uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(Admin.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }else{
                    uploadFile();
                }

                break;
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if(iv_uri !=null){
            StorageReference fileReference = storageReference.child(et_enter_name.getText()+"."+getFileExtension(iv_uri));
            final String extension = getFileExtension(iv_uri);

            uploadTask = fileReference.putFile(iv_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            Toast.makeText(Admin.this, "Data Added to Database Successfully !", Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(et_enter_name.getText().toString().trim(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            setdownloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            SharedPreferences sharedPreferences = getSharedPreferences("Upload_Url",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(et_enter_name.getText().toString()+"."+extension,setdownloadUrl);
                            editor.commit();
                            databaseReference.child(et_enter_name.getText().toString()).setValue(upload);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Admin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFilechooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData() !=null){
            iv_uri = data.getData();

            Picasso.with(this).load(iv_uri).into(iv_preview_upload);
        }

    }
}
