package Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import Account.AccountActivity;

public class LoginActivity extends AppCompatActivity {
    public final static String TAG = LoginActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public EditText etEmail, etPass;
    public Button btnLogin;

    private void initUi() {
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString().trim();
        if (!email.contains("@")) {
            // Admin
            String username = email;
            firebaseFirestore
                    .collection("admin")
                    .whereEqualTo("username", username)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "intentAdmin");
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    String passwordQuery = document.getString("password");
                                    if (passwordQuery.equals(password)) {

                                        Intent intentAdmin = new Intent(LoginActivity.this, AccountActivity.class);
                                        intentAdmin.putExtra("document", document.getId());
                                        startActivity(intentAdmin);
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else {
            // User
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                AuthResult authResult = task.getResult();
                                String userId = authResult.getUser().getUid();
                                logLoginHistory(userId);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Toast.makeText(getBaseContext(), "Authentication Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Toast.makeText(getBaseContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void logLoginHistory(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);
        CollectionReference loginHistoryRef = userRef.collection("loginHistory");

        Map<String, Object> loginData = new HashMap<>();
        loginData.put("timestamp", FieldValue.serverTimestamp());

        loginHistoryRef.add(loginData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Login history added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding login history", e);
                    }
                });
    }
}
