package com.example.asus.admin.menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.admin.R;
import com.example.asus.admin.adapter.LaporanAdapter;
import com.example.asus.admin.helper.Buka;
import com.example.asus.admin.model.ResponseInsert;
import com.example.asus.admin.model.laporan.PelaporanItem;
import com.example.asus.admin.model.laporan.ResponseLaporan;
import com.example.asus.admin.network.ApiService;
import com.example.asus.admin.network.RetroClient;
import com.example.asus.admin.shared.SharedLogin;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.admin.helper.Constan.URL_IMAGE;

public class MainActivity extends AppCompatActivity implements LaporanAdapter.LaporanAdapterListener {
    FloatingActionMenu materialDesignFAM;
    @BindView(R.id.material_design_floating_action_menu_item1)
    FloatingActionButton materialDesignFloatingActionMenuItem1;
    @BindView(R.id.material_design_floating_action_menu_item2)
    FloatingActionButton materialDesignFloatingActionMenuItem2;
    @BindView(R.id.material_design_floating_action_menu_item3)
    FloatingActionButton materialDesignFloatingActionMenuItem3;
    List<PelaporanItem> hasilPesan;
    LaporanAdapter adapterLap;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    TextView txtNamaK, txtGenderK, txtUmurK, txtAlamatK, txtNamaP, txtGenderP, txtHubP, txtJenisP, txtKronologi;
    ImageView imageView;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.edt_cari)
    EditText edtCari;
    @BindView(R.id.itemSender)
    CardView itemSender;
    @BindView(R.id.form_message)
    LinearLayout formMessage;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.txt_notif)
    TextView txtNotif;
    @BindView(R.id.material_design_android_floating_action_menu)
    FloatingActionMenu materialDesignAndroidFloatingActionMenu;
    EditText edtStatus;
    Spinner spinner;
    String x = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        getLap();
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                try {
                    if (editable.toString() == null || editable.toString().trim().isEmpty()) {
                        adapterLap.setList(hasilPesan);
                        return;
                    } else {
                        filter(editable.toString());
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        });
    }

    private void filter(String text) {
        ArrayList<PelaporanItem> filteredValues = new ArrayList<>(hasilPesan);
        for (PelaporanItem value : hasilPesan) {
            if (!value.getNamaKorban().toLowerCase().contains(text.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        adapterLap.setList(filteredValues);
    }

    @OnClick({R.id.material_design_floating_action_menu_item1, R.id.material_design_floating_action_menu_item2, R.id.material_design_floating_action_menu_item3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.material_design_floating_action_menu_item1:
                Buka.buka(MainActivity.this, Berita.class);
                break;
            case R.id.material_design_floating_action_menu_item2:
                Buka.buka(MainActivity.this, Chat.class);
                break;
            case R.id.material_design_floating_action_menu_item3:
                Buka.buka(MainActivity.this, Akun.class);
                break;
        }
    }


    //getLaporan
    private void getLap() {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        ApiService apiService = RetroClient.getApiService();
        Call<ResponseLaporan> call = apiService.getLaporan();
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
                        adapterLap = new LaporanAdapter(MainActivity.this, list, MainActivity.this);
                        //  swipeMain.setRefreshing(false);
                        rvList.setAdapter(adapterLap);
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
        dialog = new AlertDialog.Builder(MainActivity.this);
        //buat layout
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.pop_up, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
//        dialog.setIcon(R.drawable.ic_info);
//        dialog.setTitle("Data Laporan");

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
        //kronologi
        txtKronologi = dialogView.findViewById(R.id.txt_kronologi);

        //set
        Glide.with(MainActivity.this).load(URL_IMAGE + data.getGambar())
                .thumbnail(0.5f)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        txtNamaK.setText(data.getNamaKorban());
        txtGenderK.setText(data.getJkKorban());
        txtUmurK.setText(data.getUsiaKorb());
        txtAlamatK.setText(data.getAlamatKorban());
        //pelaku
        txtNamaP.setText(data.getNmPelaku());
        txtGenderP.setText(data.getJkPelaku());
        txtHubP.setText(data.getHubPelaku());
        txtJenisP.setText(data.getJenisKs());
        txtKronologi.setText(data.getKronologi());

        dialog.setNeutralButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(data);
            }
        });

        dialog.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                status(data);
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

    private void delete(final PelaporanItem data) {
        //buat object alarm
        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(MainActivity.this);
        //settting judul dan pesan
        aleBuilder.setTitle("Hapus Data");
        aleBuilder
                .setMessage("Apakah anda yakin ingin menghapus data ini?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterLap.hapusData(data.getIdLapor(), "pelaporan", "id_lapor");
                        getLap();
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

    private void status(final PelaporanItem data) {
        //buat dialog
        dialog = new AlertDialog.Builder(MainActivity.this);
        //buat layout
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.status, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //inisialisasi
        edtStatus = dialogView.findViewById(R.id.edt_pesan);
        spinner = dialogView.findViewById(R.id.sp);

        //
        final ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this, R.array.statud,
                R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterStatus);

        //select sp
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                x = adapterStatus.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.setNeutralButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                update(data, x, edtStatus.getText().toString());
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

    private void update(PelaporanItem data, String status, String pesam) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Send Pesan");
        progressDialog.show();

        ApiService apiService = RetroClient.getApiService();
        Call<ResponseInsert> call = apiService.updateLaporan(data.getIdLapor(), status, pesam);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.body().getCode() == 200) {
                    progressDialog.dismiss();
                    getLap();
                } else {
                    Toast.makeText(MainActivity.this, "Gagal Kirim Data", Toast.LENGTH_SHORT).show();
                }
            }

            //
            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Log.e("Tag", "Error jaringan saat insert :" + t.getMessage());
                Toast.makeText(MainActivity.this, "Error jaringan saat insert", Toast.LENGTH_SHORT).show();


            }
        });
    }
}