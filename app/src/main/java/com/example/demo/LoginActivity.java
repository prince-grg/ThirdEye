package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {


    FirebaseFirestore fstore;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    TextView btnlogin,txtEmail,txtPassword,btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Hooks
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Sign in");
        progressDialog.setMessage("Signing into Account");
        btnlogin=  findViewById(R.id.btn_Login);
        txtEmail=findViewById(R.id.txt_Email);
        txtPassword=findViewById(R.id.txt_password);
        btnSignUp=findViewById(R.id.btn_signup12);
        //Login Button
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();
                if (email.equals("") || pass.equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Enter Valid Info", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {

                                Toast.makeText(LoginActivity.this, "Sign in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, DashActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                if(null != intent)
                startActivity(intent);
                else
                    Toast.makeText(LoginActivity.this, "Null intent", Toast.LENGTH_SHORT).show();
               finish();
            }
        });



    }


}