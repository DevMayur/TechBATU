package com.techbatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.techbatu.timeline.GetDetails;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    private Button bt_send_phone_number,bt_verify_otp,bt_resend_phone_number;
    private EditText et_phone_number,et_otp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verification_code;
    PhoneAuthProvider.ForceResendingToken mtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initvariables();

        initlistners();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                try {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("EXCEPTIONINONCOMPLETE","EXCEPTION IS"+e.getMessage());
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(SignUp.this, "Verification failed"+e, Toast.LENGTH_SHORT).show();
                bt_verify_otp.setVisibility(View.INVISIBLE);
                et_otp.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                try{
                verification_code = s;
                bt_verify_otp.setVisibility(View.VISIBLE);
                et_otp.setVisibility(View.VISIBLE);}
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("ExceptionINSENDCODE","Exception is"+e.getMessage());
                }

            }
        };

    }

    private void initlistners() {
        bt_send_phone_number.setOnClickListener(this);
        bt_verify_otp.setOnClickListener(this);
    }

    private void initvariables() {
        bt_verify_otp = findViewById(R.id.bt_verify_otp);
        bt_send_phone_number = findViewById(R.id.bt_send_phone_number_otp);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_otp = findViewById(R.id.et_otp);
        auth = FirebaseAuth.getInstance();
        bt_verify_otp.setVisibility(View.INVISIBLE);
        et_otp.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id)
        {
            case R.id.bt_send_phone_number_otp:
                String number = "+91"+et_phone_number.getText().toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        this,               // Activity (for callback binding)
                        mCallback);

                break;

            case R.id.bt_verify_otp:

                verify();

                break;

        }

    }




    private void verify() {
        String otp = et_otp.getText().toString();
            VerifyPhoneNumber(verification_code, otp);

    }

    private void VerifyPhoneNumber(String verifycode, String otp) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifycode,otp);
        signInWithPhoneAuthCredential(credential);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           startActivity(new Intent(SignUp.this,GetDetails.class));
                            SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("phone",et_phone_number.getText().toString());
                            editor.commit();
                            Toast.makeText(SignUp.this, "Successfully Signed up", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(SignUp.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }



}
