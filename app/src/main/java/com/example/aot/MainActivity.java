package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText pwd;

    private Button loginButton;

    private Button signin;
    private FirebaseAuth firebaseAuth;
    DatabaseReference db;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().subscribeToTopic("Default");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Log In");


        userName=(EditText)findViewById(R.id.userName);
        pwd=(EditText)findViewById(R.id.pwd);

        loginButton=(Button)findViewById(R.id.loginButton);
        signin=(Button)findViewById(R.id.singIn);
        forgotPassword=(TextView)findViewById(R.id.forgotPwd);

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        FirebaseUser user=firebaseAuth.getCurrentUser();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(userName.getText().toString(),pwd.getText().toString());

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });

    }

    private void validate(String userName, String password)
    {
        if(userName.isEmpty()||password.isEmpty())
        {
            Toast.makeText(MainActivity.this,"Empty Field",Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.setMessage("Loading");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        progressDialog.dismiss();
                        Boolean key = checkEmailVerification();

                        if(key)
                        {

                            String currentuser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                            String userType = currentuser.split(",")[1];
                            if(userType.equals("1")){
                                Intent intentResident = new Intent(MainActivity.this, StudentHomeActivity.class);
                                startActivity(intentResident);
                                FirebaseMessaging.getInstance().subscribeToTopic("Student");
                                MainActivity.this.finish();
                            }
                            else if(userType.equals("2")){
                                startActivity(new Intent(MainActivity.this,AdminHomeActivity.class));
                                MainActivity.this.finish();
                            }
                            else if(userType.equals("3")) {
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                FirebaseMessaging.getInstance().subscribeToTopic("Teacher");
                                MainActivity.this.finish();
                            }


                        }
                        }


                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Failed",Toast.LENGTH_LONG).show();

                    }
                }
            });

        }

    }

    private Boolean checkEmailVerification(){
        Boolean check = false;
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean flag=firebaseUser.isEmailVerified();
        if(flag)
        {
            check = true;


        }
        else
        {
            Toast.makeText(this,"Verify Email",Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
        return check;
    }
}
