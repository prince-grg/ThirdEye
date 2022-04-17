package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    String name, email, pass, phone;
    FirebaseAuth auth;

    FirebaseFirestore fstore;
    TextView btnSignUp, btnSignIn, txtName, txtEmail, txtPassword, txtPhoneNumber;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        btnSignUp = findViewById(R.id.btn_signup23);
        btnSignIn = findViewById(R.id.btn_signIn45);
        txtName = findViewById(R.id.txt_name);
        txtEmail = findViewById(R.id.txt_Email);
        txtPassword = findViewById(R.id.txt_password);
        txtPhoneNumber = findViewById(R.id.txt_phoneNumber);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Creating new account ..");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = txtName.getText().toString();
                email = txtEmail.getText().toString();
                pass = txtPassword.getText().toString();
                phone = txtPhoneNumber.getText().toString();
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fields Can't be Empty", Toast.LENGTH_SHORT).show();
                } else {
      progressDialog.show();

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {


                                fstore.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();

                                            startActivity(new Intent(SignUpActivity.this, DashActivity.class));
                                            finish();
                                            User user = new User(name, email, pass, phone);
                                            fstore.collection("User").document(email).set(user);


                                        }


                                    }
                                });


                            } else {
                                Toast.makeText(SignUpActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });


                }
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });


    }
}