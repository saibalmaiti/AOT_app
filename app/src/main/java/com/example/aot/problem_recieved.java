package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class problem_recieved extends AppCompatActivity {
    private String ID,messeage,date;
    private DatabaseReference student,log;
    private FirebaseAuth firebaseAuth;
    private TextView textView1,textView2,textView3,textView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_recieved);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        messeage = intent.getStringExtra("messeage");
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        date = df.format(Calendar.getInstance().getTime());
        textView1 =findViewById(R.id.textView);
        textView2 =findViewById(R.id.textView2);
        textView3 =findViewById(R.id.textView3);
        textView4 =findViewById(R.id.textView4);
        student = FirebaseDatabase.getInstance().getReference("Users").child(ID);
        log = FirebaseDatabase.getInstance().getReference("Log").child(firebaseAuth.getCurrentUser().getDisplayName().split(",")[0]);
        student.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView1.setText(dataSnapshot.child("name").getValue().toString());
                textView2.setText(dataSnapshot.child("year").getValue().toString());
                textView3.setText(dataSnapshot.child("depertment").getValue().toString());
                textView4.setText(messeage);
                Map<String,Object> rdate = new HashMap<>();
                rdate.put("Receiving Time",date);
                log.child(dataSnapshot.child("name").getValue().toString()).child(messeage).setValue(rdate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
