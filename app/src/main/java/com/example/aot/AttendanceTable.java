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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AttendanceTable extends AppCompatActivity {
    private ProgressDialog progressDialogue,progressDialogue2;
    private String year,stream,subject;
    private Button update;
    private DatabaseReference attendance;
    private ArrayList<String> studentName = new ArrayList<>();
    private ListView checkList;
    private ArrayList<String> present;
    private Double d;
    private Date c;
    private SimpleDateFormat df;
    private Map<String, Object> attMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_table);
        progressDialogue=new ProgressDialog(AttendanceTable.this);
        progressDialogue.setMessage("Loading");
        progressDialogue.show();
        progressDialogue2=new ProgressDialog(AttendanceTable.this);
        progressDialogue2.setMessage("Updating");

        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(c);
        Log.i("//::>>", "Date "+formattedDate);

        attMap = new HashMap<>();
        present = new ArrayList<String>();
        Intent intent=getIntent();
        year= intent.getStringExtra("year");
        stream= intent.getStringExtra("stream");
        subject= intent.getStringExtra("subject");

        update = (Button)findViewById(R.id.update_btn1);
        checkList=(ListView)findViewById(R.id.checkable_list);
        checkList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        attendance = FirebaseDatabase.getInstance().getReference().child("Attendance");
        attendance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> key=new ArrayList<>();
                for(DataSnapshot child: dataSnapshot.child(year).child(stream).getChildren())
                {
                    studentName.add(child.getKey().toString());
                }


                /* initialize database*/
                for (final String n: studentName) {
                    attendance.child(year).child(stream).child(n)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.hasChild(formattedDate)){
                                        attMap.put(subject,0);
                                        Log.i("N::","Names "+n);
                                        attendance.child(year).child(stream).child(n).child(formattedDate).updateChildren(attMap);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {


                                }
                            });
                }

               /* for (String n: studentName){
                    Log.i("N::","Names "+n);
                    attendance.child(year).child(stream).child(n).child(formattedDate).updateChildren(attMap);
                }*/
               /* view setup and data collection*/
                studentName.remove((Object)"Total");
                studentName.add("Total");
                ArrayAdapter<String>arrayAdapter = new ArrayAdapter(AttendanceTable.this, R.layout.row_layout, studentName);
                progressDialogue.dismiss();
                checkList.setAdapter(arrayAdapter);
                checkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        String selected_item = ((TextView)view).getText().toString();
                        if(present.contains(selected_item))
                            present.remove(selected_item);
                        else
                            present.add(selected_item);
                        Log.i("::<<//","Selected "+selected_item+" List "+present);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /* database update*/
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (present.contains((Object)"Total")){
                    progressDialogue2.show();
                    for (final String name : present) {
                        Log.i(">>??", name);
                        attendance.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                d = Double.valueOf(dataSnapshot.child(year).child(stream).child(name).child(subject).getValue().toString());
                                Log.i(";;>?", "Past Value " + name + " " + d);
                                d = d + 1;
                                attMap.put(subject,1);
                                attendance.child(year).child(stream).child(name).child(formattedDate)
                                        .updateChildren(attMap);

                                attendance.child(year).child(stream).child(name).child(subject).setValue((Object) d)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialogue2.dismiss();
                                                    Intent intent = new Intent(AttendanceTable.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    AttendanceTable.this.finish();
                                                }
                                                else {
                                                    Toast.makeText(AttendanceTable.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            /*Database Error*/
                            }
                        });
                    }
                }
                else {
                    /*if total is not selected*/
                    Toast.makeText(AttendanceTable.this,"Select Total",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
