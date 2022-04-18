package com.example.demo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DashActivity extends AppCompatActivity {

   FirebaseAuth mAuth;
   CardView old_cases,new_cases1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
      old_cases= findViewById(R.id.card_cases);
      new_cases1=findViewById(R.id.new_cases);
      old_cases.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View view) {
              startActivity(new Intent(DashActivity.this,CaseListActivity.class));
          }
      });
      new_cases1.setOnClickListener(new View.OnClickListener(){

          @Override
          public void onClick(View view) {
              startActivity(new Intent(DashActivity.this,MainActivity.class));
          }

      });


    }
}