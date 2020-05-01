package com.example.aot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class studentComplaintActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference teachers,log;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private ArrayList<String> teacherList= new ArrayList<>();
    private Spinner teacherSpin;
    private ProgressDialog progressDialogue;
    private Button complain;
    private EditText cmpText;
    private String title,body,FCMID,teacherName,stdName,stdID,date;
    private RequestQueue mRequestQue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complaint);
        Intent intent = getIntent();
        teacherList = intent.getStringArrayListExtra("Teacher");
        stdName = intent.getStringExtra("Name");
        stdID = intent.getStringExtra("ID");

        progressDialogue=new ProgressDialog(studentComplaintActivity.this);
        progressDialogue.setMessage("Sending");

        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        date = df.format(Calendar.getInstance().getTime());
        Log.d("Date",date);

        mRequestQue = Volley.newRequestQueue(this);

        teachers = FirebaseDatabase.getInstance().getReference("Teacher");
        log = FirebaseDatabase.getInstance().getReference("Log");
        complain = findViewById(R.id.complaintSubmitBtn);
        cmpText = findViewById(R.id.complaintText);

        teacherSpin=findViewById(R.id.facultySpin);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(studentComplaintActivity.this,android.R.layout.simple_spinner_dropdown_item,teacherList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSpin.setAdapter(adapter);
        teacherSpin.setOnItemSelectedListener(studentComplaintActivity.this);

        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogue.show();
                body = cmpText.getText().toString();

                teachers.child(teacherName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Complains").hasChild(stdName)){
                            FirebaseDatabase.getInstance().getReference("HOD").child("Saibal").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        FCMID = dataSnapshot.child("FCMID").getValue().toString();

                                    title = "Complain Against "+teacherName;
                                    sendNotification();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        title = stdName;
                        FCMID = dataSnapshot.child("FCMID").getValue().toString();
                        sendNotification();
                        Map<String,Object> cmp_map =new HashMap<>();
                        cmp_map.put("problem",body);
                        cmp_map.put("ID",stdID);
                        teachers.child(teacherName).child("Complains").child(stdName).updateChildren(cmp_map);
                        Map<String,Object> log_map =new HashMap<>();
                        log_map.put("Sending Time",date);
                        log.child(teacherName).child(stdName).child(body).updateChildren(log_map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }
    private void sendNotification()  {

        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to",FCMID);
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();
            notification.put("title",title);
            notification.put("body",body);
            data.put("UserId",stdID);
            /*Added extra*/
            notification.put("click_action","com.example.aot.problem_received");
            mainObj.put("notification",notification);
            mainObj.put("data",data);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialogue.dismiss();
                            Toast.makeText(studentComplaintActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // on failure run
                    progressDialogue.dismiss();
                    Toast.makeText(studentComplaintActivity.this,"Notification Failed",Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyAhw3MYKFx0lQPai1UiWh5ZU2WWsHXHLX4");
                    return header;
                }
            };

            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        teacherName=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
