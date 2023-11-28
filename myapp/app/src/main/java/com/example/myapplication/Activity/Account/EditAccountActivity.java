package com.example.myapplication.Activity.Account;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditAccountActivity extends AppCompatActivity {
    private final static String TAG = EditAccountActivity.class.getSimpleName();
    Button btnEditAccount;
    private Dialog progressDialog;
    EditText etIdAccount, etNameAccount, etAgeAccount, etPhoneAccount;
    ToggleButton toggleButtonAccount;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        unitUI();

        db = FirebaseFirestore.getInstance();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Update Account");
        }

        //Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            etIdAccount.setText(bundle.getString("id", "invalid"));
            etNameAccount.setText(bundle.getString("name", "invalid"));
            etAgeAccount.setText(String.valueOf(bundle.getInt("age", 0)));
            etPhoneAccount.setText(bundle.getString("phone", ""));
            boolean status = bundle.getBoolean("status", false);
            toggleButtonAccount.setChecked(status);
        }
        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null) {
                    String id = etIdAccount.getText().toString().trim();
                    String name = etNameAccount.getText().toString().trim();
                    int age = Integer.parseInt(etAgeAccount.getText().toString().trim());
                    String phone = etPhoneAccount.getText().toString().trim();

                    boolean status = toggleButtonAccount.getText().toString().equalsIgnoreCase("Normal");

                    Account account = new Account();
                    account.setId(id);
                    account.setName(name);
                    account.setAge(age);
                    account.setPhoneNumber(phone);
                    account.setStatus(status);
                    updateData(account);

                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void updateData(Account account) {
        this.showProgressDialog("Updating data ....");
        db.collection("users").document(account.getId())
                .update("name", account.getName(),
                        "age", account.getAge(),
                        "phoneNumber", account.getPhoneNumber(),
                        "status", account.isStatus())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dismissProgressDialog();
                            Log.i(TAG, "Update Successfully");
                            Toast.makeText(EditAccountActivity.this, "Update Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            dismissProgressDialog();
                            Log.i(TAG, "Error:" + task.getException().getMessage());
                            Toast.makeText(EditAccountActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void unitUI() {
        btnEditAccount = findViewById(R.id.btnEditAccount);
        etIdAccount = findViewById(R.id.etIdAccount);
        etNameAccount = findViewById(R.id.etNameAccount);
        etAgeAccount = findViewById(R.id.etAgeAccount);
        etPhoneAccount = findViewById(R.id.etPhoneAccount);
        toggleButtonAccount = findViewById(R.id.toggleButtonAccount);
    }

    private void showProgressDialog(String msg) {
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setTitle(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
