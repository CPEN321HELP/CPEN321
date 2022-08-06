package com.example.help_m5.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.help_m5.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatItem> chatItems;

    public ChatAdapter(List<ChatItem> chatItems) {
        this.chatItems = chatItems;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    /*
        Show data onto recyclerview
        Called after OnCreateViewHolder to bind the views
     */
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatItem chatItem = chatItems.get(position);

        holder.myMessage.setText(chatItem.getMyMessage());
        holder.myDateTime.setText(chatItem.getMyDateTime());
        if (holder.myDateTime.getText().toString().isEmpty() && holder.myMessage.getText().toString().isEmpty()) {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
        }

        holder.oppoMessage.setText(chatItem.getOppoMessage());
        holder.oppoDateTime.setText(chatItem.getOppoDateTime());
        if (holder.oppoDateTime.getText().toString().isEmpty() && holder.oppoMessage.getText().toString().isEmpty()) {
            holder.oppoLayout.setVisibility(View.GONE);
            holder.myLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout myLayout;
        public TextView myMessage;
        public TextView myDateTime;
        public LinearLayout oppoLayout;
        public TextView oppoMessage;
        public TextView oppoDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myMessage = (TextView) itemView.findViewById(R.id.myMessage);
            myDateTime = (TextView) itemView.findViewById(R.id.myMsgTime);
            myLayout = (LinearLayout) itemView.findViewById(R.id.myLayout);

            oppoMessage = (TextView) itemView.findViewById(R.id.oppoMessage);
            oppoDateTime = (TextView) itemView.findViewById(R.id.oppoMsgTime);
            oppoLayout = (LinearLayout) itemView.findViewById(R.id.oppoLayout);

        }
    }

}
