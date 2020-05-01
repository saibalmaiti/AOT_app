package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.net.PasswordAuthentication;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText pwdEmail;
    private Button pwdReset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        pwdEmail=(EditText)findViewById(R.id.pwdEmail);
        pwdReset=(Button)findViewById(R.id.pwdResetButton);

        pwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth=FirebaseAuth.getInstance();
                String usermail=pwdEmail.getText().toString().trim();
                if(usermail.equals(""))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Enter Registered Email ID",Toast.LENGTH_LONG).show();

                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(usermail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "Reset Mail Sent Successfully", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ForgotPasswordActivity.this,"Request Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
