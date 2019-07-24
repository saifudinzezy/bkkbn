package com.example.asus.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.admin.R;
import com.example.asus.admin.menu.ChatUser;
import com.example.asus.admin.model.group.ChatItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.asus.admin.helper.Constan.KEY_DATA;

public class AdapterGroup extends RecyclerView.Adapter<AdapterGroup.ViewHolder> {
    private Context context;
    private List<ChatItem> list;
    private LaporanAdapterListener listener;

    public AdapterGroup(Context context, List<ChatItem> list) {
        this.context = context;
        this.list = list;
    }

    public AdapterGroup(Context context, List<ChatItem> list, LaporanAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public List<ChatItem> getList() {
        return list;
    }

    public void setList(List<ChatItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.txtNama.setText(list.get(position).getNama());
        viewHolder.notifCountText.setText(list.get(position).getTotal());
        viewHolder.txtPesan.setText(list.get(position).getPesan());

        if (!getList().get(position).getJenkel().equalsIgnoreCase("Laki - Laki")) {
            viewHolder.icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.girl, 0, 0, 0);
        }

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatItem data = list.get(position);
                Intent intent = new Intent(context, ChatUser.class);
                intent.putExtra(KEY_DATA, data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        TextView icon;
        @BindView(R.id.txt_nama)
        TextView txtNama;
        @BindView(R.id.txt_pesan)
        TextView txtPesan;
        @BindView(R.id.notifCountText)
        TextView notifCountText;
        @BindView(R.id.cv)
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface LaporanAdapterListener {
        void onSelect(int index, ChatItem data, View view);
    }
}