package com.nozimy.app65_home1.ui.listing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nozimy.app65_home1.data.entities.Contact;
import com.nozimy.app65_home1.R;

import java.util.List;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder>{
    private final List<Contact> items;
    private final OnListFragmentInteractionListener listener;

    public ContactsListAdapter(List<Contact> items, OnListFragmentInteractionListener listener){
        this.items = items;
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
        holder.item = items.get(position);
        holder.contentTextView.setText(items.get(position).name);
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
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View containerView;
        public final TextView contentTextView;
        public Contact item;

        public ViewHolder(View view) {
            super(view);
            containerView = view;
            contentTextView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentTextView.getText() + "'";
        }
    }
}
