package com.example.asus.admin.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.admin.Addberita;
import com.example.asus.admin.R;
import com.example.asus.admin.adapter.BeritaAdapter;
import com.example.asus.admin.helper.Buka;
import com.example.asus.admin.model.BeritaItem;
import com.example.asus.admin.model.ResponseBerita;
import com.example.asus.admin.model.ResponseInsert;
import com.example.asus.admin.network.ApiService;
import com.example.asus.admin.network.RetroClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Berita extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wrap)
    AppBarLayout wrap;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.txt_notif)
    TextView txtNotif;
    @BindView(R.id.tambah)
    FloatingActionButton tambah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Berita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        // hide fab
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && tambah.getVisibility() == View.VISIBLE) {
                    tambah.hide();
                } else if (dy < 0 && tambah.getVisibility() != View.VISIBLE) {
                    tambah.show();
                }
            }
        });
        getBerita();
    }

    private void getBerita() {
        pb.setVisibility(View.VISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseBerita> call = apiService.getBerita();
        call.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                List<BeritaItem> hasilPesan = response.body().getBerita();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));

                if (response.body().getSukses().equalsIgnoreCase("true")) {
                    try {
                        rvList.setVisibility(View.VISIBLE);
                        ArrayList<BeritaItem> list = new ArrayList<>();
                        list.addAll(hasilPesan);

                        BeritaAdapter adapterPesan1 = new BeritaAdapter(Berita.this, list, new BeritaAdapter.OnFunction() {
                            @Override
                            public void onDelete(BeritaItem data) {
                                onDeleted(data);
                            }
                        });
                        //  swipeMain.setRefreshing(false);
                        rvList.setAdapter(adapterPesan1);
                        adapterPesan1.notifyDataSetChanged();
                        pb.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(Berita.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    Toast.makeText(Berita.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                    rvList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {
                Toast.makeText(Berita.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                rvList.setVisibility(View.GONE);
            }
        });
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getBerita();
    }

    public void hapusData(final String id, final String tabel, final String cari) {
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseInsert> call = apiService.delete(tabel, cari, id);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                try {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(Berita.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getBerita();
                    } else {
                        Toast.makeText(Berita.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Berita.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.tambah)
    public void onClick() {
        Buka.buka(Berita.this, Addberita.class);
    }

    public void onDeleted(final BeritaItem data) {
        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(Berita.this);
        //settting judul dan pesan
        aleBuilder.setTitle("Hapus Data");
        aleBuilder
                .setMessage("Apakah anda yakin ingin menghapus data ini?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapusData(data.getIdBerita(), "berita", "id_berita");
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //cancel
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = aleBuilder.create();
        alertDialog.show();
    }
}
