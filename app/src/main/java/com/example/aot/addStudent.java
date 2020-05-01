package com.example.aot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class addStudent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner yearspin,deptspin;
    private Button nxt;
    private String year,dept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        yearspin = findViewById(R.id.yearSpinner1);
        deptspin = findViewById(R.id.departmentSpinner);
        nxt = findViewById(R.id.addBtnNext);
        ArrayAdapter<CharSequence> adapterYear=ArrayAdapter.createFromResource(this,R.array.year,R.layout.my_spinner);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearspin.setAdapter(adapterYear);
        yearspin.setOnItemSelectedListener(addStudent.this);
        ArrayAdapter<CharSequence> adapterdept=ArrayAdapter.createFromResource(this,R.array.dept,R.layout.my_spinner);
        adapterdept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptspin.setAdapter(adapterdept);
        deptspin.setOnItemSelectedListener(addStudent.this);
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(addStudent.this,addStudentName.class);
                intent.putExtra("year",year);
                intent.putExtra("stream",dept);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        year = yearspin.getSelectedItem().toString();
        dept = deptspin.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(addStudent.this,"SELECT CLASS AND STREAM",Toast.LENGTH_LONG).show();

    }
}
