package com.example.myapplication.Activity.Account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddAccountActivity extends AppCompatActivity {
    private static final String TAG = AddAccountActivity.class.getSimpleName();

    private void initUi() {
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        btnAdd = findViewById(R.id.btnAddAccount);
        loadingPB = findViewById(R.id.progressBar);
        //toggleButton = findViewById(R.id.toggleButtonAddAccount);
        etEmail = findViewById(R.id.etEmail);
        spinner = findViewById(R.id.role);
    }

    private EditText etPhone, etName, etAge, etEmail;
    private Button btnAdd;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar loadingPB;
    ToggleButton toggleButton;
    Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        initUi();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Account");

        String[] roles = {"manager", "employee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Instance Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        final int[] role = {-1};
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = etName.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();

                    if (TextUtils.isEmpty(name)) {
                        etName.setError("Please enter Account Name");
                    } else if (!TextUtils.isDigitsOnly(etAge.getText().toString().trim())
                            || etAge.getText().toString().trim().equals("")) {
                        etAge.setError("Please enter age");
                    } else {
                        int age = Integer.parseInt(etAge.getText().toString().trim());
                        if (age <= 0 && age >= 150) {
                            etAge.setError("Please enter a valid age (greater than 0)  and <=150");
                        } else if (TextUtils.isEmpty(phone)) {
                            etPhone.setError("Please enter Account Phone");
                        } else if (role[0] == -1) {
                            Toast.makeText(getBaseContext(), "Please choose a role for the user!", Toast.LENGTH_LONG).show();
                        } else {
                            loadingPB.setVisibility(View.VISIBLE);
                            Account account = new Account();
                            account.setAge(age);
                            account.setName(name);
                            account.setPhoneNumber(phone);
                            account.setEmail(email);
                            account.setRole(role[0] == 1 ? "Manager" : "Employee");
                            addData(account);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Exception :" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nameRole = (String) parent.getItemAtPosition(position);
                if (nameRole.equals("employee")) {
                    role[0] = 2;
                } else {
                    role[0] = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "nau");
            }
        });
    }

    private void addData(Account account) {
        String email = account.getEmail();
        String phone = account.getPhoneNumber();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, phone)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserDataToFirestore(account);
                        } else {
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(getBaseContext(), "Authentication failed: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void saveUserDataToFirestore(Account account) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        account.setId(uid);
        firebaseFirestore.collection("users").document(uid).set(account)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(getBaseContext(), "Add Account Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddAccountActivity.this, AccountActivity.class));
                        } else {
                            Toast.makeText(getBaseContext(), "Error adding document: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
