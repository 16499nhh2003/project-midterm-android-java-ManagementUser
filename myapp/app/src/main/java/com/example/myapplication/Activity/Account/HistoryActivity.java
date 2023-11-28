package com.example.myapplication.Activity.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryActivity.class.getSimpleName();
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        Intent intent = getIntent();
        if (intent != null) {
            String uid = intent.getExtras().getString("uid");
            showHistoryLogin(uid);
        }


    }

    private void showHistoryLogin(String userId) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        CollectionReference loginHistoryRef = userRef.collection("loginHistory");

        loginHistoryRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> historyList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Timestamp timestamp = (Timestamp) document.getData().get("timestamp");
                            String formattedTime = formatDateAndTime(timestamp.toDate());
                            historyList.add(formattedTime);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(HistoryActivity.this, android.R.layout.simple_list_item_1, historyList);
                        listView = findViewById(R.id.listViewHistory);
                        listView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting login history", e);
                    }
                });
    }

    private String formatDateAndTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi", "VN"));
        return sdf.format(date);
    }
}
