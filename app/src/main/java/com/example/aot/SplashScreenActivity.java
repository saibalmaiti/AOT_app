package com.example.aot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        LogoLauncher logoLauncher=new LogoLauncher();
        logoLauncher.start();


    }

    private class LogoLauncher extends Thread{

        public void run()
        {
            try{
                sleep(3000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            firebaseAuth=FirebaseAuth.getInstance();
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if(user!=null)
            {

                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String userType = currentuser.split(",")[1];
                        if(userType.equals("1")){
                            Intent intentResident = new Intent(SplashScreenActivity.this, StudentHomeActivity.class);
                            startActivity(intentResident);
                            FirebaseMessaging.getInstance().subscribeToTopic("Student");
                            SplashScreenActivity.this.finish();
                        }
                        else if(userType.equals("2")){
                            startActivity(new Intent(SplashScreenActivity.this,AdminHomeActivity.class));
                            SplashScreenActivity.this.finish();
                        }
                        else if(userType.equals("3")) {
                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                            FirebaseMessaging.getInstance().subscribeToTopic("Teacher");
                            SplashScreenActivity.this.finish();
                        }

            }
            else {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                SplashScreenActivity.this.finish();
            }

        }
    }
}

