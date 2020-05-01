package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class StudentHomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logOut;
    private Button website;
    private Button portal;
    private Button btnAttendance,btnComplaint;
    private ArrayList<String> Teacher = new ArrayList<>();
    private DatabaseReference teachers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        getSupportActionBar().setTitle("Student");
        teachers= FirebaseDatabase.getInstance().getReference().child("Teacher");
        teachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren() ){
                    Teacher.add(child.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        String userid =(String)firebaseAuth.getCurrentUser().getDisplayName();
        String[] parts=userid.split(",");
        final String name = parts[0];
        Toast.makeText(StudentHomeActivity.this, "WELCOME "+name, Toast.LENGTH_LONG).show();
        FirebaseMessaging.getInstance().subscribeToTopic("Student");

        website=(Button)findViewById(R.id.studentWebtBtn);
        portal =(Button)findViewById(R.id.buttonEducation);
        btnAttendance=(Button)findViewById(R.id.studentBtnAttendance);
        btnComplaint=(Button)findViewById(R.id.studentComplaintBtn);
        portal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentHomeActivity.this,aotportal.class));
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentHomeActivity.this,WebPageActivity.class));
            }
        });
        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentHomeActivity.this,StudentAttendanceActivity.class));
            }
        });
        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomeActivity.this,studentComplaintActivity.class);
                intent.putStringArrayListExtra("Teacher",Teacher);
                intent.putExtra("Name",name);
                intent.putExtra("ID",firebaseAuth.getCurrentUser().getUid().toString());
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu m) {

        getMenuInflater().inflate(R.menu.menu,m);
        return super.onCreateOptionsMenu(m);
    }

    private void logOut()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(StudentHomeActivity.this,MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu: {
                logOut();

            }
        }
        return super.onOptionsItemSelected(item);
    }
}
