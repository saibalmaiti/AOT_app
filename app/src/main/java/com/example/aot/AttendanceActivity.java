package com.example.aot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AttendanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner yearSpin, streamSpin;
    private Button  btnNext;
    public String year, stream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        yearSpin=findViewById(R.id.yearSpinner);
        streamSpin=findViewById(R.id.streamSpinner);
        btnNext=findViewById(R.id.attendanceBtnNext);
        ArrayAdapter<CharSequence> adapterYear=ArrayAdapter.createFromResource(this,R.array.year,R.layout.my_spinner);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(adapterYear);
        yearSpin.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterStream=ArrayAdapter.createFromResource(this,R.array.dept,R.layout.my_spinner);
        adapterStream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        streamSpin.setAdapter(adapterStream);
        streamSpin.setOnItemSelectedListener(this);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AttendanceActivity.this,AttendanceSubjectActivity.class);
                intent.putExtra("year",year);
                intent.putExtra("stream",stream);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        year=yearSpin.getSelectedItem().toString();
        stream=streamSpin.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
