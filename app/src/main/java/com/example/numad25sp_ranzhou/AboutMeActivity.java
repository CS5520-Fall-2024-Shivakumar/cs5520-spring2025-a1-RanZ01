package com.example.numad25sp_ranzhou;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutMeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView nameTextView = findViewById(R.id.text_name);
        TextView emailTextView = findViewById(R.id.text_email);

        nameTextView.setText("Name: Ran Zhou");
        emailTextView.setText("Email: zhou.ran1@northeastern.edu");
    }
}
