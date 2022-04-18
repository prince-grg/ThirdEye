package com.example.demo.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.DashActivity;
import com.example.demo.Model.Person;
import com.example.demo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class personAdapter extends RecyclerView.Adapter<personAdapter.ViewHolder>{
    ArrayList<Person> persons;
    Context context;

    public personAdapter(ArrayList<Person> persons, Context context) {
        this.persons = persons;
        this.context = context;
    }

    @NonNull
    @Override
    public personAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_person_view,parent,false);
        return  new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull personAdapter.ViewHolder holder, int position) {
        Person person=persons.get(position);
        holder.name.setText(person.getName());

        holder.age.setText(person.getAge());
        holder.gender.setText(person.getGender());
        String ss =person.getImageLocation();
        //holder.innerLayout.setBackgroundColor(R.color.yellow);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(ss);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("data","Success");
                Picasso.get().load(uri).placeholder(R.drawable.profile).into(holder.pp);
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("data", "Failure");
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DashActivity.class);
                //   Student stu=student;
//                intent.putExtra("name", person.getName());
//                intent.putExtra("class", person.getClass());
//                intent.putExtra("father", person.getFather_name());
//                intent.putExtra("center", person.getCenter());
//                intent.putExtra("mobile", person.getPhone());
//                intent.putExtra("age", person.getAge());
//                intent.putExtra("teacher", person.getTeacher());
//                intent.putExtra("profile", person.getProfilepic());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,age,gender;
        ImageView pp;
        LinearLayout layout,innerLayout;
        View vv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.layout);
            name=itemView.findViewById(R.id.name_id);
            pp=itemView.findViewById(R.id.profie_img_id);
            age=itemView.findViewById(R.id.class_name_id);
            gender=itemView.findViewById(R.id.center_name_id);
            innerLayout=itemView.findViewById(R.id.innerLayout);
            vv=itemView;

        }
    }
}