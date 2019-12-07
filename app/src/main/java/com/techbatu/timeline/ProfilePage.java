package com.techbatu.timeline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.techbatu.LoginPage;
import com.techbatu.R;
import com.techbatu.viewSyllabus;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePage extends AppCompatActivity {

    private CircleImageView iv_profile_photo_profile;
    private TextView tv_username,tv_name,tv_prn,tv_phone,tv_college,tv_dept;
    private String dept,sem,college,prn,fullname,phone,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        initVariables();

        initListners();

        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
        dept = sharedPreferences.getString("department","");
        sem = sharedPreferences.getString("semester","");
        college = sharedPreferences.getString("college","");
        prn = sharedPreferences.getString("prn","");
        fullname = sharedPreferences.getString("full_name","");


        SharedPreferences LoginStatus = getSharedPreferences("status",MODE_PRIVATE);
        String status = LoginStatus.getString("status","null");
        if(status.equals("true"))
        {
            Toast.makeText(this, "Wel-come!", Toast.LENGTH_SHORT).show();
            setElements();
        }else
        {
            startActivity(new Intent(this,LoginPage.class));
        }

    }

    private void setElements() {

        tv_dept.setText("Department : "+dept+"   Semester : "+sem);
        tv_college.setText("College : "+college);
        tv_prn.setText("PRN :"+prn);
        tv_name.setText("Name : " + fullname);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(prn).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists())
                {
                    Toast.makeText(ProfilePage.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }else{
                    phone = documentSnapshot.getString("phone");
                    username = documentSnapshot.getString("username");

                    tv_phone.setText("Mobile Number : +91"+ phone);
                    tv_username.setText(username);

                }
            }
        });

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference pathReference = storageRef.child("profile/profile_pic/"+prn+".jpg");
        Glide.with(ProfilePage.this)
                .load(pathReference)
                .into(iv_profile_photo_profile);


    }

    private void initVariables() {
        iv_profile_photo_profile = findViewById(R.id.iv_profile_photo_profile);
        tv_username = findViewById(R.id.tv_profile_username);
        tv_name = findViewById(R.id.tv_name_profile);
        tv_username = findViewById(R.id.tv_profile_username);
        tv_phone = findViewById(R.id.tv_profile_mobile_number);
        tv_prn = findViewById(R.id.tv_profile_prn);
        tv_college = findViewById(R.id.tv_profile_college);
        tv_dept = findViewById(R.id.tv_profile_dept);
    }

    private void initListners() {

    }


}
