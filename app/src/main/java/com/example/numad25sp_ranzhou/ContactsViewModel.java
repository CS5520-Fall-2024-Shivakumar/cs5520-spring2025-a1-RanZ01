package com.example.numad25sp_ranzhou;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends ViewModel {
    private final List<Contact> contactList = new ArrayList<>();

    public List<Contact> getContactList() {
        return contactList;
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
    }
}
