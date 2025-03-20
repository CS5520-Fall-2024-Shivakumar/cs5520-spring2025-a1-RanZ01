package com.example.numad25sp_ranzhou;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txt =findViewById(R.id.textView);

        Button aboutMeButton = findViewById(R.id.button);
        aboutMeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
            startActivity(intent);
        });

        Button quickCalcButton = findViewById(R.id.button_quick_calc);
        quickCalcButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });

        Button contactsCollectorButton = findViewById(R.id.button_contacts_collector);
        contactsCollectorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactsCollectorActivity.class);
            startActivity(intent);
        });

        Button openPrimeActivity = findViewById(R.id.btn_open_prime);
        openPrimeActivity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PrimeActivity.class);
            startActivity(intent);
        });

    }
}
