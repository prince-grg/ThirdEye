package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.Model.Person;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_PICK_IMAGE = 1000;
    private static final int CAMERA_REQUEST = 1001;
    Button  btnUpload;
    ImageView image_view;
    TextView tname, tfathername, tnumber, tage, tgender, taddress;
    String name = null, age = null, gender = null , phone = null, address = null, father_name = null;
    Uri filePath;
    ProgressDialog progressDialog;
    private static final String[] GENDER = new String[]{
            "Male", "Female", "Others"
    };
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tname = findViewById(R.id.txt_name);
        tfathername = findViewById(R.id.txt_father);
        tnumber = findViewById(R.id.txt_phone);
        tage = findViewById(R.id.txt_age);
        tgender = findViewById(R.id.txt_gender);
        taddress = findViewById(R.id.txt_address);


        fstore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, GENDER);

        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.txt_gender);
        textView.setAdapter(adapter);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Uplaoding ..");
        progressDialog.setMessage("Registering new user");

        btnUpload = findViewById(R.id.btnUpload);
        image_view = findViewById(R.id.imageView);

        image_view.setOnClickListener(v -> {
            chooseImage1();
        });

        btnUpload.setOnClickListener(v -> {
            uploadImage();
        });

    }

    private void chooseImage1() {
        final String[] fonts = {
                "Camera", "Files"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose image from ");
        builder.setItems(fonts, new DialogInterface.OnClickListener() {
            @
                    Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Camera".equals(fonts[which])) {
                    openCamera();
                } else if ("Files".equals(fonts[which])) {
                    chooseImage();
                }
            }
        });
        builder.show();
    }

    private void uploadImage() {
        if (filePath != null) {
            progressDialog.show();
            String rUID= UUID.randomUUID().toString();
            StorageReference reference = storageReference.child("images/" +rUID);
            name = tname.getText().toString();
            age = tage.getText().toString();
            phone = tnumber.getText().toString();
            address = taddress.getText().toString();
            father_name = tfathername.getText().toString();
            gender = ((AutoCompleteTextView) tgender).getText().toString();

            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please Fill Mandatory Fields", Toast.LENGTH_SHORT).show();
            } else {
                Person person = new Person(name, age, gender, address, phone, father_name, reference.getPath());
                fstore.collection("Persons").document(rUID).set(person).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                         }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                    }
                });

                reference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, DashActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        } else {
            Toast.makeText(this, "Please Select a Image", Toast.LENGTH_SHORT).show();

        }
    }

    private void openCamera() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                    filePath = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void chooseImage() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PERMISSION_PICK_IMAGE);
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_PICK_IMAGE) {
                if (data != null) {
                    if (data.getData() != null) {
                        filePath = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                            image_view.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (requestCode == CAMERA_REQUEST) {

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    image_view.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

