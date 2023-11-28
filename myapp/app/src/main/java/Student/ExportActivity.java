package Student;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utils.ExcelGenerator;


public class ExportActivity extends AppCompatActivity implements ExcelGenerator {
    Button btnExportStudent;
    private final String[] permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100;
    private final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        btnExportStudent = findViewById(R.id.btnExportStudent);

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        btnExportStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExportStudent();
            }
        });
    }


    private void handleExportStudent() {
        List<Student> students = new ArrayList<Student>();

        students.add(new Student("1", "James", "X", "2", "Male"));
        students.add(new Student("1", "James", "X", "2", "Male"));
        students.add(new Student("1", "James", "X", "2", "Male"));
        students.add(new Student("1", "James", "X", "2", "Male"));

        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(students).getAsJsonArray();

        String[] titles = new String[]{"ID", "Name", "Class", "Bench", "Age", "Gender", "Grade"};
        String[] indexName = new String[]{"studentId", "studentName", "studentClass", "studentBench", "studentAge", "studentGender", "studentGrade"};

        HashMap<String, String> otherValue = new HashMap<>();
        otherValue.put("Record", "Student Record");
        otherValue.put("Place", "Campus City");
        otherValue.put("City", "Toranto");

        File file = generateXlsFile(this, titles, indexName, jsonArray, otherValue, "Student Record", "students", 1);

        Log.i(ExportActivity.class.getSimpleName() , file.toString());
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ExportActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ExportActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(ExportActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ExportActivity.this, "REQUEST_CODE_WRITE_EXTERNAL_STORAGE Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ExportActivity.this, "REQUEST_CODE_WRITE_EXTERNAL_STORAGE Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ExportActivity.this, "REQUEST_CODE_READ_EXTERNAL_STORAGE Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ExportActivity.this, "REQUEST_CODE_READ_EXTERNAL_STORAGE Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
