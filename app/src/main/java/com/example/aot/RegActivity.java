package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.HashMap;
import java.util.Map;

public class RegActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText regName, regPwd, regMail,idUnique;
    private Button register;
    private FirebaseAuth firebaseAuth;

    private RadioButton radioStudent, radioTeacher, radioAdmin;
    private RadioGroup field;
    String nameid;
    final Map<String, Object> usermap = new HashMap<>();
    private Map<String, Object> Teachermap = new HashMap<>();
    private String dept, year;
    private Spinner spinYear;
    private Spinner spinDept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        getSupportActionBar().setTitle("Registration");
        setupUI();

        firebaseAuth=FirebaseAuth.getInstance();

        spinYear = findViewById(R.id.spinYear);
        spinDept =findViewById(R.id.spinDept);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinYear.setAdapter(adapter);
        spinYear.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.dept,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDept.setAdapter(adapter1);
        spinDept.setOnItemSelectedListener(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    String usermail = regMail.getText().toString().trim();
                    String userpwd = regPwd.getText().toString().trim();
                    String id = idUnique.getText().toString();
                    final String username = regName.getText().toString();



                    /*usermap.put("Email", usermail);
                    usermap.put("name", username);*/

                    int prompt = 0;
                    if (radioStudent.isChecked() && id.equals("STUDENT")) {
                        prompt = 1;
                        nameid = username + ",1";
                        usermap.put("Email", usermail);
                        usermap.put("name", username);
                    } else if (radioAdmin.isChecked() && id.equals("ADMIN")) {
                        prompt = 2;
                         nameid = username + ",2";
                        usermap.put("Email", usermail);
                        usermap.put("name", username);
                    } else if (radioTeacher.isChecked() && id.equals("TEACHER"))
                    {
                        prompt = 3;
                         nameid = username+",3";
                         Teachermap.put("Email",usermail);
                         Teachermap.put("Department",dept.split("-")[0]);
                    }
                    else
                    {
                        Toast.makeText(RegActivity.this,"Incorrect ID",Toast.LENGTH_LONG).show();
                    }

                    usermap.put("Type", prompt);
                    /*For Student and admin Registration*/
                    if(prompt==1 || prompt==2)
                    {


                        firebaseAuth.createUserWithEmailAndPassword(usermail,userpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nameid)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("log", "User profile updated.");
                                                    }
                                                }
                                            });
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegActivity.this, "Data Saved", Toast.LENGTH_LONG).show();
                                            } else {
                                                //display a failure message
                                            }
                                        }
                                    });
                                    emailVerification();

                                }
                                else
                                {
                                    Toast.makeText(RegActivity.this,"Registration Failed", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                    /* For Teacher Registration*/
                    else if(prompt==3){

                        firebaseAuth.createUserWithEmailAndPassword(usermail,userpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nameid)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("log", "User profile updated.");
                                                    }
                                                }
                                            });
                                    Teachermap.put("UID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    FirebaseDatabase.getInstance().getReference("Teacher")
                                            .child(username)
                                            .setValue(Teachermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegActivity.this, "Data Saved", Toast.LENGTH_LONG).show();
                                            } else {
                                                //display a failure message
                                            }
                                        }
                                    });
                                    emailVerification();

                                }
                                else
                                {
                                    Toast.makeText(RegActivity.this,"Registration Failed", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                }

            }
        });
    }

    private void setupUI(){
        regName=(EditText)findViewById(R.id.regName);
        regPwd=(EditText)findViewById(R.id.regPwd);
        regMail=(EditText)findViewById(R.id.regMail);
        register=(Button)findViewById(R.id.register);
        radioAdmin=(RadioButton)findViewById(R.id.radioAdmin);
        radioStudent=(RadioButton)findViewById(R.id.radioStudent);
        radioTeacher=(RadioButton)findViewById(R.id.radioTeacher);
        idUnique=(EditText)findViewById(R.id.idUnique);
        field=(RadioGroup)findViewById(R.id.field);

    }

    private boolean validate()
    {
        Boolean res =false;
        String name=regName.getText().toString();
        String pwd=regPwd.getText().toString();
        String mail=regMail.getText().toString();
        String id=idUnique.getText().toString();
        int radioCheck;
        if(field.getCheckedRadioButtonId()==-1)
        {
            radioCheck=0;
        }
        else
        {
            radioCheck=1;
        }
        if(name.isEmpty()||pwd.isEmpty()||mail.isEmpty()||id.isEmpty()||radioCheck==0)
        {
            Toast.makeText(this, "Empty field",Toast.LENGTH_LONG).show();
        }
        else
        {
            res=true;
        }
        return res;
    }

    private void emailVerification(){
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                        Toast.makeText(RegActivity.this,"Registration Successful,Verify Email",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(RegActivity.this,MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(RegActivity.this,"Verification Email Cannot Be Sent",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String txt= parent.getItemAtPosition(position).toString();
        //usermap.put("year",txt);
        dept = spinDept.getSelectedItem().toString();
        year = spinYear.getSelectedItem().toString();
        usermap.put("depertment",dept);
        usermap.put("year",year);
        //Toast.makeText(parent.getContext(),dept+year,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
