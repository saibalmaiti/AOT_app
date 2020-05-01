package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class addStudentName extends AppCompatActivity {

    private FirebaseDatabase attendance;
    private String year, dept;
    private String name, year_dept;
    private HashMap<String,Object> attendanceMap;
    private String[] subjects;
    private EditText stdName;
    private Context context;
    private Button addStd,statusBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_name);
        Intent intent = getIntent();
        year = intent.getStringExtra("year");
        dept = intent.getStringExtra("stream");
        context = addStudentName.this;
        addStd = findViewById(R.id.addStdBtn);
        statusBtn = findViewById(R.id.statusBtn);
        stdName = (EditText) findViewById(R.id.studentname1);
        attendanceMap = new HashMap<>();
        year_dept = dept.split("-")[0]+year.split(" ")[0];
        int sub_id = context.getResources().
                getIdentifier(year_dept,"array",context.getPackageName());
        subjects = context.getResources().getStringArray(sub_id);
        for(String s: subjects) {
            Log.i("::>>//", "Subject" + s);
            attendanceMap.put(s,0);
        }
        addStd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = (String)stdName.getText().toString();
                Log.i("//::>", "Subject" + attendanceMap);
                Log.i(">>//","Name"+name);
                attendance.getInstance().getReference("Attendance").child(year).child(dept)
                        .child(name).setValue(attendanceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(addStudentName.this,"Student Added",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(addStudentName.this,"Error Occurred",Toast.LENGTH_LONG).show();

                        }

                    }
                });

            }
        });
        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addStudentName.this,added_Studentlist.class);
                intent.putExtra("year",year);
                intent.putExtra("dept",dept);
                startActivity(intent);
            }
        });
    }
}
