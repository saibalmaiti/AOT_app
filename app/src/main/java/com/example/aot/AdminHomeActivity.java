package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class AdminHomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logOut;
    private Button website;
    private Button meeting,addStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        FirebaseMessaging.getInstance().subscribeToTopic("Admin");
        firebaseAuth=FirebaseAuth.getInstance();
        String userid =(String)firebaseAuth.getCurrentUser().getDisplayName();
        String[] parts=userid.split(",");
        String name = parts[0];
        Toast.makeText(AdminHomeActivity.this, "WELCOME "+name, Toast.LENGTH_LONG).show();
        getSupportActionBar().setTitle("Admin");
        website=(Button)findViewById(R.id.btnWeb);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this,WebPageActivity.class));
            }
        });
        meeting = (Button)findViewById(R.id.buttonMeeting);
        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this,Notification.class));
            }
        });
        addStudent = (Button)findViewById(R.id.buttonAdd);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this,addStudent.class));
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
        startActivity(new Intent(AdminHomeActivity.this,MainActivity.class));
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
