package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class added_Studentlist extends AppCompatActivity {

    private String year,dept;
    private DatabaseReference attendance;
    private TextView cls_year;
    private ListView name;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added__studentlist);
        progressDialog=new ProgressDialog(added_Studentlist.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        Intent intent = getIntent();
        year = intent.getStringExtra("year");
        dept = intent.getStringExtra("dept");
        cls_year = findViewById(R.id.cls_year);
        name = findViewById(R.id.std_list);

        cls_year.setText(year+" "+dept);

        attendance= FirebaseDatabase.getInstance().getReference().child("Attendance");
        attendance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> key=new ArrayList<>();
                for(DataSnapshot childref: dataSnapshot.child(year).child(dept).getChildren()){
                    key.add(childref.getKey());
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(added_Studentlist.this, android.R.layout.simple_list_item_1, key);
                progressDialog.dismiss();
                name.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
