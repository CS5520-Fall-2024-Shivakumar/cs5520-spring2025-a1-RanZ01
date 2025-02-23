package com.example.numad25sp_ranzhou;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ContactsCollectorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private ContactsViewModel contactsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_collector);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        contactsAdapter = new ContactsAdapter(contactsViewModel.getContactList(), this);
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_contact);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ContactsCollectorActivity.this, AddContactActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("new_contact_name");
            String phone = data.getStringExtra("new_contact_phone");

            if (name != null && phone != null) {
                Contact newContact = new Contact(name, phone);
                contactsViewModel.addContact(newContact);
                contactsAdapter.notifyItemInserted(contactsViewModel.getContactList().size() - 1);

                Snackbar.make(recyclerView, "Contact Added: " + name, Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            contactsViewModel.getContactList().remove(contactsViewModel.getContactList().size() - 1);
                            contactsAdapter.notifyItemRemoved(contactsViewModel.getContactList().size());
                        }).show();
            }
        }
    }
}
