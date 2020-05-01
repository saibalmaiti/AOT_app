package com.example.aot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StudentAttendanceActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private String uid,count;
    private DatabaseReference db,attendance;
    private String dept;
    private String year,name;
    private ListView attendanceListView;
    private ArrayList<Object> arrayList=new ArrayList<>();
    private ArrayList<Double> total = new ArrayList<>();
    private ArrayList<String> percentage = new ArrayList<>();
    private static DecimalFormat df = new DecimalFormat("#.##");
    private ArrayList<Double> val = new ArrayList<>();
    private int i=0,j=0,c;
    private String active ;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(StudentAttendanceActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        setContentView(R.layout.activity_student_attendance);
        getSupportActionBar().setTitle("Attendance");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        attendanceListView=(ListView)findViewById(R.id.listView);


        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        /*changed valueEvent listener*/
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dept = dataSnapshot.child("depertment").getValue().toString();
                year = dataSnapshot.child("year").getValue().toString();
                name = dataSnapshot.child("name").getValue().toString();
                attendance = FirebaseDatabase.getInstance().getReference().child("Attendance");
                /*changed valueEvent listener*/
                attendance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> key = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.child(year).child(dept).child(name).getChildren()) {
                            Log.i("!_@@_key::>", child.getKey());
                            Log.i("!_@@_key::>", "Value" + child.getValue());
                            key.add(child.getKey());
                            val.add(Double.valueOf(child.getValue().toString()));

                        }
                        for (DataSnapshot childref : dataSnapshot.child(year).child(dept).child("Total").getChildren()) {
                            total.add((val.get(i) / Double.valueOf(childref.getValue().toString())) * 100);
                            percentage.add(key.get(i) + "   " + df.format(total.get(i)) + "%");
                            i++;
                        }
                        Double t = 0.0;
                        Double c = 0.0;
                        for (Double per : total) {
                            t = t + per;
                            c = c + 1;
                        }
                        t = t / c;
                        percentage.add("Total   " + df.format(t) + "%");

                        arrayAdapter = new ArrayAdapter(StudentAttendanceActivity.this, android.R.layout.simple_list_item_1, percentage);
                        progressDialog.dismiss();
                        attendanceListView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



}

}
