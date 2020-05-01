package com.example.aot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Notification extends AppCompatActivity {
    private Button mnotify;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private String ntext;
    private String time;
    private ProgressDialog progressDialog;
    private TimePicker timePicker1;
    private  int hour,min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle("MEETING");
        progressDialog=new ProgressDialog(Notification.this);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = timePicker1.getHour();
                min = timePicker1.getMinute();
                Log.d("Time","time: "+hour+":"+min);
            }
        });


        mnotify = (Button) findViewById(R.id.submit);


        mRequestQue = Volley.newRequestQueue(this);

        mnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Sending");
                progressDialog.show();
                setupUI();
                sendNotification();

            }
        });

    }

    private void setupUI() {
        time = (new StringBuilder().append(hour).append(":").append(min)).toString();
        ntext = "Meeting at " + time;
        Log.d("Time2","time: "+ntext);
        Toast.makeText(Notification.this,ntext,Toast.LENGTH_SHORT);
    }

    private void sendNotification()  {

        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/Teacher");
            JSONObject notification = new JSONObject();
            notification.put("title","NEW MEETING");
            notification.put("body",ntext);
            /*Added extra*/
            notification.put("click_action","com.example.aot.Meeting_notification");
            mainObj.put("notification",notification);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(Notification.this, "Sent", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // on failure run
                    progressDialog.dismiss();
                    Toast.makeText(Notification.this,"Notification Failed",Toast.LENGTH_SHORT).show();
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
}