package com.example.numad25sp_ranzhou;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private Context context;

    public ContactsAdapter(List<Contact> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhoneNumber());

        holder.itemView.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
            context.startActivity(callIntent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            showEditDialog(holder.getAdapterPosition());
            return true;
        });

        holder.deleteButton.setOnClickListener(v -> {
            int removedPosition = holder.getAdapterPosition();
            Contact removedContact = contactList.get(removedPosition);
            contactList.remove(removedPosition);
            notifyItemRemoved(removedPosition);

            Snackbar snackbar = Snackbar.make(v, "Deleted " + removedContact.getName(), Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", undoView -> {
                contactList.add(removedPosition, removedContact);
                notifyItemInserted(removedPosition);
            });
            snackbar.show();
        });
    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Contact");

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_edit_contact, null);
        EditText inputName = viewInflated.findViewById(R.id.edit_text_edit_name);
        EditText inputPhone = viewInflated.findViewById(R.id.edit_text_edit_phone);

        Contact contact = contactList.get(position);
        inputName.setText(contact.getName());
        inputPhone.setText(contact.getPhoneNumber());

        builder.setView(viewInflated);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = inputName.getText().toString().trim();
            String newPhone = inputPhone.getText().toString().trim();

            if (!newName.isEmpty() && !newPhone.isEmpty()) {
                contact.setName(newName);
                contact.setPhoneNumber(newPhone);
                notifyItemChanged(position);

                Snackbar.make(viewInflated, "Contact updated", Snackbar.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView;
        ImageButton deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contact_name);
            phoneTextView = itemView.findViewById(R.id.contact_phone);
            deleteButton = itemView.findViewById(R.id.button_delete_contact);
        }
    }
}
