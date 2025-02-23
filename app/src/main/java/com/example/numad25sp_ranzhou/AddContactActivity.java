package com.example.numad25sp_ranzhou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class AddContactActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEditText = findViewById(R.id.edit_text_name);
        phoneEditText = findViewById(R.id.edit_text_phone);
        saveButton = findViewById(R.id.button_save_contact);

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(AddContactActivity.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_contact_name", name);
            resultIntent.putExtra("new_contact_phone", phone);
            setResult(RESULT_OK, resultIntent);

            Snackbar.make(v, "Contact Added", Snackbar.LENGTH_LONG)
                    .setAction("Undo", undoView -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    }).show();

            finish();
        });
    }
}
