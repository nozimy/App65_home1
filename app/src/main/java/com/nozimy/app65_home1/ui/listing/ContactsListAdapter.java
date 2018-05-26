package com.nozimy.app65_home1.ui.listing;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.model.Contact;

import java.util.List;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder>{
    List<? extends Contact> contactList;
    private final OnListFragmentInteractionListener listener;

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

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return contactList.get(oldItemPosition).getId() ==
                            contacts.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Contact newProduct = contacts.get(newItemPosition);
                    Contact oldProduct = contactList.get(oldItemPosition);
                    return newProduct.getId() == oldProduct.getId();
                }
            });
            contactList = contacts;
            result.dispatchUpdatesTo(this);
        }
    }

    public ContactsListAdapter(OnListFragmentInteractionListener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.contentTextView.setText(contactList.get(position).getDisplayName());
        holder.containerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onListFragmentInteraction(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View containerView;
        public final TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            containerView = itemView;
            contentTextView = (TextView) itemView.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentTextView.getText() + "'";
        }
    }
}
