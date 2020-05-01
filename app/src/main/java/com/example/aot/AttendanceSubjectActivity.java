package com.example.aot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AttendanceSubjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    private String year,stream,dept,sub;
    private Context context;
    private Spinner SubSpin;
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_subject);
        context = AttendanceSubjectActivity.this;
        /* code to receive the stream and dept data from previous intent*/
        Intent intent=getIntent();
        year= intent.getStringExtra("year");
        stream= intent.getStringExtra("stream");
        /*end of code*/
        next =findViewById(R.id.nextAttendance);
        SubSpin=findViewById(R.id.subjct);

        dept = stream.split("-")[0]+year.split(" ")[0];
        int str_id = context.getResources().getIdentifier(dept, "array",
                context.getPackageName());
        ArrayAdapter<CharSequence> adapterSub=ArrayAdapter.
                createFromResource(AttendanceSubjectActivity.this,str_id,R.layout.my_spinner);
        adapterSub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SubSpin.setAdapter(adapterSub);
        SubSpin.setOnItemSelectedListener(AttendanceSubjectActivity.this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceSubjectActivity.this,AttendanceTable.class);
                intent.putExtra("year",year);
                intent.putExtra("stream",stream);
                intent.putExtra("subject",sub);
                startActivity(intent);
            }
        });




    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sub=SubSpin.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
