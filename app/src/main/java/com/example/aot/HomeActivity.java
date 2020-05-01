package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference tdatabase;
    private Button logOut;
    private Button website;
    private String token,name;
    private Button attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth=FirebaseAuth.getInstance();
        String userid =(String)firebaseAuth.getCurrentUser().getDisplayName();
        String[] parts=userid.split(",");
        name = parts[0];
        Toast.makeText(HomeActivity.this, "WELCOME "+name, Toast.LENGTH_LONG).show();
        getSupportActionBar().setTitle("Teacher");
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("Teacher");
        tdatabase = FirebaseDatabase.getInstance().getReference("Teacher");
        /*FCM Id*/
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("??..", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = ("FCMID: "+ token);
                        tdatabase.child(name).child("FCMID").setValue(token);
                        Log.d("??..<", msg);

                    }
                });

        website=(Button)findViewById(R.id.teacherWebButton);
        attendance=(Button)findViewById(R.id.teacherBtnAttendance);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,AttendanceActivity.class));
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,WebPageActivity.class));
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
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Teacher");
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity.this,MainActivity.class));
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
