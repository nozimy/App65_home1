package com.nozimy.app65_home1.ui.listing;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.model.Contact;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder>{
    private List<? extends Contact> contactList;
    private final ContactClickCallback listener;

    public void setContactList(final List<? extends Contact> contacts){
        if (contactList == null) {
            contactList = contacts;
            notifyItemRangeInserted(0, contacts.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return contactList.size();
                }

                @Override
                public int getNewListSize() {
                    return contacts.size();
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return Objects.equals(contactList.get(oldItemPosition).getId(), contacts.get(newItemPosition).getId());
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Contact newContact = contacts.get(newItemPosition);
                    Contact oldContact = contactList.get(oldItemPosition);
                    return Objects.equals(newContact.getId(), oldContact.getId())
                            && Objects.equals(oldContact.getFamilyName(), newContact.getFamilyName());
                }
            });
            contactList = contacts;
            result.dispatchUpdatesTo(this);
        }

    }

    public ContactsListAdapter(ContactClickCallback listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(contactList.get(holder.getAdapterPosition()).getId());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bind(contactList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content) TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
//            contentTextView = (TextView) itemView.findViewById(R.id.content);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Contact contact){
            contentTextView.setText(contact.getDisplayName());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentTextView.getText() + "'";
        }
    }
}
