package com.example.myapplication.Activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddUserActivity extends AppCompatActivity {
    private EditText etPhone, etName, etAge;
    private Switch aSwitch;
    private Button btnAdd;

    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        aSwitch = findViewById(R.id.switchStatus);
        btnAdd =  findViewById(R.id.btnAdd);


        firebaseFirestore = FirebaseFirestore.getInstance();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = etName.getText().toString().trim();
                    int age = Integer.parseInt(etAge.getText().toString().trim());
                    String phone = etPhone.getText().toString().trim();
                    boolean status = aSwitch.isSelected();

                    if (TextUtils.isEmpty(name)) {
                        etName.setError("Please enter User Name");
                    } else if (TextUtils.isDigitsOnly(etAge.getText().toString().trim())) {
                        etPhone.setError("Please enter number");
                    } else if (TextUtils.isEmpty(phone) && TextUtils.isDigitsOnly(phone)) {
                        etPhone.setError("Please enter User Phone");
                    } else {
                        // calling method to add data to Firebase Firestore.
                        addDataToFirestore(name , age ,phone , status);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private  void  addDataToFirestore(String name , int age ,String  phone ,boolean status){
        User user = new User(name , age ,phone , status);
        firebaseFirestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText( getBaseContext() , "Add User Successfully:" + documentReference.getId()  ,  Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( getBaseContext() , "Error adding document" + e ,  Toast.LENGTH_LONG).show();
            }
        });
    }
}
