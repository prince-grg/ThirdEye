package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.Adapter.personAdapter;
import com.example.demo.Model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CaseListActivity extends AppCompatActivity {

    FirebaseFirestore fstore;
    FirebaseAuth auth;
    TextView txtcount;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list);
        fstore= FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        txtcount= findViewById(R.id.total_count);
        recyclerView= findViewById(R.id.person_recycler_view);
        ArrayList<Person> myListData = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.person_recycler_view);
        personAdapter adapter = new personAdapter(myListData,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data......");
        progressDialog.setTitle("Please wait ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        fstore.collection("Persons").whereEqualTo("uid",auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                  txtcount.setText(task.getResult().size()+" \n Cases ");
                    for(QueryDocumentSnapshot document:task.getResult())
                    {

                        try{
                            Person s= document.toObject(Person.class);
                            myListData.add(s);
                            adapter.notifyDataSetChanged();
                        }
                        catch (Exception e) {
                            Log.d("ss",e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(CaseListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CaseListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}