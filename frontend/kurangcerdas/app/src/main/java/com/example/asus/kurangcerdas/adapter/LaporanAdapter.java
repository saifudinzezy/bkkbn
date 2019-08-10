package com.example.asus.kurangcerdas.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.kurangcerdas.R;
import com.example.asus.kurangcerdas.model.ResponseInsert;
import com.example.asus.kurangcerdas.model.laporan.PelaporanItem;
import com.example.asus.kurangcerdas.network.ApiService;
import com.example.asus.kurangcerdas.network.RetroClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.kurangcerdas.helper.ConvertDate.ubahTanggal2;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.ViewHolder> {
    private Context context;
    private List<PelaporanItem> list;
    private LaporanAdapterListener listener;

    public LaporanAdapter(Context context, List<PelaporanItem> list) {
        this.context = context;
        this.list = list;
    }

    public LaporanAdapter(Context context, List<PelaporanItem> list, LaporanAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public List<PelaporanItem> getList() {
        return list;
    }

    public void setList(List<PelaporanItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lap, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.txtNama.setText(list.get(position).getNamaKorban());
        viewHolder.txtTanggal.setText(ubahTanggal2(list.get(position).getTglKej()));
        viewHolder.txtJenis.setText(list.get(position).getJenisKs());
        viewHolder.txtStatus.setText(list.get(position).getStatus());
        viewHolder.txtKronologi.setText(list.get(position).getKronologi());
        viewHolder.lnPesan.setVisibility(View.GONE);

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelect(position, getList().get(position), v);
            }
        });

        if (viewHolder.txtStatus.getText().toString().equalsIgnoreCase("Diproses")) {
            //green
            viewHolder.linear.setBackgroundColor(Color.parseColor("#00796B"));
            viewHolder.txtStatus.setText("Diproses");
            viewHolder.txtPesan.setText(list.get(position).getPesan().toString());
        } else if (viewHolder.txtStatus.getText().toString().equalsIgnoreCase("Menunggu")) {
            //kuning
            viewHolder.linear.setBackgroundColor(Color.parseColor("#ffb300"));
            viewHolder.txtStatus.setText("Menunggu");
            viewHolder.txtPesan.setText(list.get(position).getPesan().toString());
        } else {
            //red
            viewHolder.linear.setBackgroundColor(Color.parseColor("#E43F3F"));
            viewHolder.txtStatus.setText("Ditolak");
            viewHolder.lnPesan.setVisibility(View.VISIBLE);
            viewHolder.txtPesan.setText(list.get(position).getPesan().toString());
        }
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_tanggal)
        TextView txtTanggal;
        @BindView(R.id.txt_nama)
        TextView txtNama;
        @BindView(R.id.txt_jenis)
        TextView txtJenis;
        @BindView(R.id.txt_kronologi)
        TextView txtKronologi;
        @BindView(R.id.txt_status)
        TextView txtStatus;
        @BindView(R.id.linear)
        LinearLayout linear;
        @BindView(R.id.txt_pesan)
        TextView txtPesan;
        @BindView(R.id.ln_pesan)
        LinearLayout lnPesan;
        @BindView(R.id.cv)
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface LaporanAdapterListener {
        void onSelect(int index, PelaporanItem data, View view);
    }

    public void hapusData(final String id, final String tabel, final String cari) {
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseInsert> call = apiService.delete(tabel, cari, id);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                try {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {

            }
        });
    }
}