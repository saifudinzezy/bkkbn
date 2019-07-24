package com.example.asus.admin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.admin.R;
import com.example.asus.admin.model.chat.ChatItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by USER on 10/08/2018.
 */

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyViewHolder> {
    private static String USER = "";
    private List<ChatItem> itemList;
    private Context context;

    public AdapterChat(List<ChatItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public List<ChatItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ChatItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesan, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setContent(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemSender)
        CardView sender;

        @BindView(R.id.itemReceiver)
        CardView receiver;

        @BindView(R.id.tvSenderUsername)
        TextView tvSenderUsername;

        @BindView(R.id.tvSenderMessage)
        TextView tvSenderMessage;

        @BindView(R.id.tvReceiverUsername)
        TextView tvReceiverUsername;

        @BindView(R.id.tvReceiverMessage)
        TextView tvReceiverMessage;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setContent(ChatItem item){
//
            //jika dia user
            if (item.getKode().equalsIgnoreCase("0")){
                receiver.setVisibility(View.GONE);
                tvReceiverUsername.setVisibility(View.GONE);

                tvSenderMessage.setText(item.getPesan());
                tvSenderUsername.setText(item.getNamaUser());
            } else {
                //jika diadmin
                sender.setVisibility(View.GONE);
                tvSenderUsername.setVisibility(View.GONE);

                tvReceiverMessage.setText(item.getPesan());
                tvReceiverUsername.setText("Saya");
            }
        }
    }
}