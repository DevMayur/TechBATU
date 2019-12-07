package com.techbatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.techbatu.timeline.NewsFeed;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextView et_prn,et_password;
    private Button bt_sign_in,bt_sign_up;
    private String first_name,last_name,username,password,prn_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ImageView iv_logo_login = findViewById(R.id.iv_logo_login);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("Design/skymines2.png");
        Glide.with(LoginPage.this)
                .load(pathReference)
                .into(iv_logo_login);

        initvariables();
        initListners();

        et_prn.setText("");
        et_password.setText("");
        SharedPreferences LoginStatus = getSharedPreferences("status",MODE_PRIVATE);
        String status = LoginStatus.getString("status","null");
        if(status.equals("true"))
        {
            Toast.makeText(this, "already Logged In!", Toast.LENGTH_SHORT).show();
        }
    }


    private void initvariables() {
        et_prn = findViewById(R.id.et_prn);
        et_password = findViewById(R.id.et_password);
        bt_sign_in = findViewById(R.id.bt_sign_in);
        bt_sign_up = findViewById(R.id.bt_sign_up);
    }

    private void initListners() {
        bt_sign_in.setOnClickListener(this);
        bt_sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        switch (id)
        {
            case R.id.bt_sign_in:
                if(!TextUtils.isEmpty(et_prn.getText()) && !TextUtils.isEmpty(et_password.getText()) )
                {
                    signin();
                }else
                {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }



                break;
            case R.id.bt_sign_up:
                startActivity(new Intent(LoginPage.this,SignUp.class));
                break;
        }

    }



    private void signin() {
        final String prn = et_prn.getText().toString();
        if(prn!=null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference docRef = firebaseFirestore.collection("Users").document(prn);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        first_name = documentSnapshot.getString("first_name");
                        last_name = documentSnapshot.getString("last_name");
                        username = documentSnapshot.getString("username");
                        password = documentSnapshot.getString("password");
                        prn_no = documentSnapshot.getString("prn");
                        String full_name = first_name+""+last_name;

                           if(et_password.getText().toString().equals(password))
                           {
                        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("first_name",first_name);
                        editor.putString("last_name",last_name);
                        editor.putString("full_name",full_name);
                        editor.putString("username",username);
                        editor.putString("password",password);
                        editor.putString("prn",prn_no);
                        editor.commit();

                            Toast.makeText(LoginPage.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                            SharedPreferences LoginStatus = getSharedPreferences("status",MODE_PRIVATE);
                           SharedPreferences.Editor statouseditor = LoginStatus.edit();
                           statouseditor.putString("status","true");
                           statouseditor.commit();

                            startActivity(new Intent(LoginPage.this, Dashboard.class));

                        }else
                           {
                               Toast.makeText(LoginPage.this, "INVALID", Toast.LENGTH_SHORT).show();
                           }


                    }
                    else
                    {
                        Toast.makeText(LoginPage.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }
}
