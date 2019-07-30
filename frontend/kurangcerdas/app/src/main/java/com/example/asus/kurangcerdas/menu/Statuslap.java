package com.example.asus.kurangcerdas.menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.kurangcerdas.R;
import com.example.asus.kurangcerdas.adapter.LaporanAdapter;
import com.example.asus.kurangcerdas.model.laporan.PelaporanItem;
import com.example.asus.kurangcerdas.model.laporan.ResponseLaporan;
import com.example.asus.kurangcerdas.network.ApiService;
import com.example.asus.kurangcerdas.network.RetroClient;
import com.example.asus.kurangcerdas.shared.SharedLogin;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.kurangcerdas.helper.Constan.URL_IMAGE;

public class Statuslap extends AppCompatActivity implements LaporanAdapter.LaporanAdapterListener,
        SearchView.OnQueryTextListener {
    @BindView(R.id.rv)
    RecyclerView rv;
    SharedLogin sharedLogin;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    List<PelaporanItem> hasilPesan;
    LaporanAdapter adapterLap;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    TextView txtNamaK, txtGenderK, txtUmurK, txtAlamatK, txtNamaP, txtGenderP, txtHubP, txtJenisP, txtKronologi, txtStatusKorban,
            txtStatusPelaku;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statuslap);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Laporan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv.setLayoutManager(new LinearLayoutManager(this));
        sharedLogin = new SharedLogin(this);
        getLap(sharedLogin.getSharedString(SharedLogin.SP_ID));
//        Toast.makeText(this, "" + sharedLogin.getSharedString(SharedLogin.SP_ID), Toast.LENGTH_SHORT).show();
    }

    //getLaporan
    private void getLap(String kode) {
        final ProgressDialog dialog = new ProgressDialog(Statuslap.this);
        dialog.setMessage("Loading...");
        dialog.show();

        ApiService apiService = RetroClient.getApiService();
        Call<ResponseLaporan> call = apiService.getLaporan(kode);
        call.enqueue(new Callback<ResponseLaporan>() {
            @Override
            public void onResponse(Call<ResponseLaporan> call, Response<ResponseLaporan> response) {
                hasilPesan = response.body().getPelaporan();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().getResponse().equalsIgnoreCase("true")) {
                    try {
                        //
                        ArrayList<PelaporanItem> list = new ArrayList<>();
                        list.addAll(hasilPesan);
                        adapterLap = new LaporanAdapter(Statuslap.this, list, Statuslap.this);
                        //  swipeMain.setRefreshing(false);
                        rv.setAdapter(adapterLap);
                        dialog.dismiss();
                    } catch (Exception e) {

                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseLaporan> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSelect(int index, final PelaporanItem data, View view) {
        //buat dialog
        dialog = new AlertDialog.Builder(Statuslap.this);
        //buat layout
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.pop_up, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //inisialisasi
        //korban
        txtNamaK = dialogView.findViewById(R.id.txt_nama_k);
        txtGenderK = dialogView.findViewById(R.id.txt_gender_k);
        txtUmurK = dialogView.findViewById(R.id.txt_umur_k);
        txtAlamatK = dialogView.findViewById(R.id.txt_alamat_k);
        imageView = dialogView.findViewById(R.id.image);
        //pelaku
        txtNamaP = dialogView.findViewById(R.id.txt_nama_p);
        txtGenderP = dialogView.findViewById(R.id.txt_gender_p);
        txtHubP = dialogView.findViewById(R.id.txt_hub_p);
        txtJenisP = dialogView.findViewById(R.id.txt_jenis_p);
        txtStatusPelaku = dialogView.findViewById(R.id.txt_status_pelaku);
        txtStatusKorban = dialogView.findViewById(R.id.txt_status_korban);
        //kronologi
        txtKronologi = dialogView.findViewById(R.id.txt_kronologi);

        //set
        Glide.with(Statuslap.this).load(URL_IMAGE + data.getGambar())
                .thumbnail(0.5f)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        txtNamaK.setText(data.getNamaKorban());
        txtGenderK.setText(data.getJkKorban());
        txtStatusKorban.setText(data.getS_korban());
        txtUmurK.setText(data.getUsiaKorb());
        txtAlamatK.setText(data.getAlamatKorban());
        //pelaku
        txtNamaP.setText(data.getNmPelaku());
        txtGenderP.setText(data.getJkPelaku());
        txtHubP.setText(data.getHubPelaku());
        txtJenisP.setText(data.getJenisKs());
        txtKronologi.setText(data.getKronologi());
        txtStatusPelaku.setText(data.getS_pelapor());

        dialog.setNeutralButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(data);
            }
        });

        dialog.setNegativeButton("TUTUP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //keluar
                dialog.dismiss();
            }
        });
        //show dialog
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Cari..");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            adapterLap = new LaporanAdapter(Statuslap.this, hasilPesan, this);
            rv.setAdapter(adapterLap);
            return false;
        }

        ArrayList<PelaporanItem> filteredValues = new ArrayList<>(hasilPesan);
        for (PelaporanItem value : hasilPesan) {
            if (!value.getNamaKorban().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        adapterLap.setList(filteredValues);
        return false;
    }

    private void delete(final PelaporanItem data) {
        //buat object alarm
        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(Statuslap.this);
        //settting judul dan pesan
        aleBuilder.setTitle("Hapus Data");
        aleBuilder
                .setMessage("Apakah anda yakin ingin menghapus data ini?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterLap.hapusData(data.getIdLapor(), "pelaporan", "id_lapor");
                        getLap(sharedLogin.getSharedString(SharedLogin.SP_ID));
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
