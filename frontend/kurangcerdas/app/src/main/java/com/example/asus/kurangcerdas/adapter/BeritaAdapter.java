package com.example.asus.kurangcerdas.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.kurangcerdas.DetailBerita;
import com.example.asus.kurangcerdas.R;
import com.example.asus.kurangcerdas.model.BeritaItem;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.asus.kurangcerdas.helper.Constan.KEY_DATA;
import static com.example.asus.kurangcerdas.helper.Constan.URL_IMAGE;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {
    Context context;
    List<BeritaItem> list;

    public BeritaAdapter(Context context, List<BeritaItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_berita, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //ini link untuk gambar
        String linkGambarMovie = list.get(position).getGambar();
        Glide.with(context)
                .load(URL_IMAGE + linkGambarMovie)
                .into(holder.imageView);

        holder.txtJudul.setText(list.get(position).getJudul());
        holder.txtBerita.setHtml(list.get(position).getArtikel());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeritaItem data = list.get(position);
                Intent intent = new Intent(context, DetailBerita.class);
                intent.putExtra(KEY_DATA, data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.txt_judul)
        TextView txtJudul;
        @BindView(R.id.txt_berita)
        HtmlTextView txtBerita;
        @BindView(R.id.cv)
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}