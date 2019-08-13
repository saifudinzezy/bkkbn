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
import com.example.asus.admin.menu.MainActivity;
import com.example.asus.admin.model.peruser.LapPeruserItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.asus.admin.helper.Constan.KEY_DATA;

public class AdapterPeruser extends RecyclerView.Adapter<AdapterPeruser.ViewHolder> {
    private Context context;
    private List<LapPeruserItem> list;
    private LaporanAdapterListener listener;

    public AdapterPeruser(Context context, List<LapPeruserItem> list) {
        this.context = context;
        this.list = list;
    }

    public AdapterPeruser(Context context, List<LapPeruserItem> list, LaporanAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public List<LapPeruserItem> getList() {
        return list;
    }

    public void setList(List<LapPeruserItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.txtNama.setText(list.get(position).getNama());
        viewHolder.notifCountText.setText(list.get(position).getTotal());
        viewHolder.txtAlamat.setText(list.get(position).getAlamat());

        if (!getList().get(position).getJenkel().equalsIgnoreCase("Laki - Laki")) {
            viewHolder.icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.girl, 0, 0, 0);
        }

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LapPeruserItem data = list.get(position);
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(KEY_DATA, data);
                context.startActivity(intent);
            }
        });

        viewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.onSelect(list.get(position));
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
        @BindView(R.id.txt_alamat)
        TextView txtAlamat;
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
        void onSelect(LapPeruserItem data);
    }
}