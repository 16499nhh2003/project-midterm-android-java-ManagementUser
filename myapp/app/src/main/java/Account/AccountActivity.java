package Account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Auth.LoginActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {
    private final static String TAG = AccountActivity.class.getSimpleName();

    private void initUi() {
        recyclerView = findViewById(R.id.recycleView);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        statusAccount = findViewById(R.id.statusAccount);
        floatingActionButton = findViewById(R.id.addAccount);
    }

    RecyclerView recyclerView;
    AccountAdapter accountAdapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;
    TextView etName, etEmail, statusAccount;
    FloatingActionButton floatingActionButton;
    List<Account> accounts;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_account);

        initUi();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("List User");
        }

        db = FirebaseFirestore.getInstance();
        accounts = new ArrayList<Account>();

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showUser();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, AddAccountActivity.class));
                finish();
            }
        });
    }

    public void showUser() {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                accounts.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Account account = new Account();
                        account.setId(document.getId());
                        account.setName(document.getString("name"));
                        account.setStatus(Boolean.TRUE.equals(document.getBoolean("status")));
                        account.setPhoneNumber(document.getString("phoneNumber"));
                        account.setAge(Math.toIntExact(document.getLong("age")));
                        account.setRole(document.getString("role"));
                        accounts.add(account);
                    }
                    accountAdapter = new AccountAdapter(AccountActivity.this);
                    accountAdapter.setAccounts(accounts);
                    recyclerView.setAdapter(accountAdapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    public void deleteData(int index) {
        String id = accounts.get(index).getId();

        FirebaseAuth.getInstance()
                .getCurrentUser()
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });

        db.collection("users").document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AccountActivity.this, "Deleted ....", Toast.LENGTH_LONG).show();
                            showUser();
                        } else {
                            Toast.makeText(AccountActivity.this, "Delete Failure ....", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constaint.KEY_ADD_UPDATE_ACCOUNT && resultCode == Activity.RESULT_OK) {
            showUser();
        }
    }

    /*
     * Handle Menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnLogout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            return true;
        } else if (item.getItemId() == R.id.btnChangeProfilePicture) {
            Intent intent1 = getIntent();
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            intent.putExtra("role", "admin");
            intent.putExtra("document", intent1.getExtras().getString("document"));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
