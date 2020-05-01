package com.example.aot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Meeting_notification extends AppCompatActivity {
private String Time;
private String message;
private TextView msgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_notification);
        msgView = findViewById(R.id.msgView);
        Intent intent = getIntent();
        Time = intent.getStringExtra("messeage");
        message = "Meeting at "+Time+" "+"today.";
        msgView.setText(message);
    }
}
