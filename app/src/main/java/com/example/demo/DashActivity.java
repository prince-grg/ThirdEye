package com.example.demo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class DashActivity extends AppCompatActivity {

   FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        mAuth=FirebaseAuth.getInstance();
        String phoneNum = "+919116462331";
        String testVerificationCode = "123456";

// Whenever verification is triggered with the whitelisted number,
// provided it is not set for auto-retrieval, onCodeSent will be triggered.
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Save the verification id somewhere
                        // ...

                        // The corresponding whitelisted code above should be used to complete sign-in.
                        Toast.makeText(DashActivity.this, "code send", Toast.LENGTH_SHORT).show();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, testVerificationCode);

                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(DashActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithCredential:success");
                                            Toast.makeText(DashActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = task.getResult().getUser();
                                            // Update UI
                                        } else {
                                            // Sign in failed, display a message and update the UI
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            Toast.makeText(DashActivity.this, "failed", Toast.LENGTH_SHORT).show();

                                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                // The verification code entered was invalid
                                            }
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        // ...\\
                        Toast.makeText(DashActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                        Toast.makeText(DashActivity.this, "verification failed", Toast.LENGTH_SHORT).show();
                        Log.d("Veri",e.getMessage());
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }
}