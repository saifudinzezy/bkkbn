package com.example.asus.admin.menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.admin.R;
import com.example.asus.admin.adapter.AdapterPeruser;
import com.example.asus.admin.helper.Buka;
import com.example.asus.admin.model.peruser.LapPeruserItem;
import com.example.asus.admin.model.peruser.ResponsePeruser;
import com.example.asus.admin.network.ApiService;
import com.example.asus.admin.network.RetroClient;
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
import static com.example.asus.admin.helper.ConvertDate.ubahTanggal2;

public class AllLap extends AppCompatActivity implements AdapterPeruser.LaporanAdapterListener {

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
    @BindView(R.id.material_design_floating_action_menu_item1)
    FloatingActionButton materialDesignFloatingActionMenuItem1;
    @BindView(R.id.material_design_floating_action_menu_item2)
    FloatingActionButton materialDesignFloatingActionMenuItem2;
    @BindView(R.id.material_design_floating_action_menu_item3)
    FloatingActionButton materialDesignFloatingActionMenuItem3;
    @BindView(R.id.material_design_android_floating_action_menu)
    FloatingActionMenu materialDesignAndroidFloatingActionMenu;
    List<LapPeruserItem> hasilPesan;
    AdapterPeruser adapterLap;
    //
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    TextView txtNama, txtGender, txtAlamat, txtTmp, txtNik;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lap);
        ButterKnife.bind(this);

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
        ArrayList<LapPeruserItem> filteredValues = new ArrayList<>(hasilPesan);
        for (LapPeruserItem value : hasilPesan) {
            if (!value.getNama().toLowerCase().contains(text.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        adapterLap.setList(filteredValues);
    }

    @OnClick({R.id.material_design_floating_action_menu_item1, R.id.material_design_floating_action_menu_item2, R.id.material_design_floating_action_menu_item3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.material_design_floating_action_menu_item1:
                Buka.buka(AllLap.this, Berita.class);
                break;
            case R.id.material_design_floating_action_menu_item2:
                Buka.buka(AllLap.this, Chat.class);
                break;
            case R.id.material_design_floating_action_menu_item3:
                Buka.buka(AllLap.this, Akun.class);
                break;
        }
    }

    private void getLap() {
        final ProgressDialog dialog = new ProgressDialog(AllLap.this);
        dialog.setMessage("Loading...");
        dialog.show();

        ApiService apiService = RetroClient.getApiService();
        Call<ResponsePeruser> call = apiService.getPeruser();
        call.enqueue(new Callback<ResponsePeruser>() {
            @Override
            public void onResponse(Call<ResponsePeruser> call, Response<ResponsePeruser> response) {
                hasilPesan = response.body().getLapPeruser();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().getCode() == 200) {
                    try {
                        //
                        ArrayList<LapPeruserItem> list = new ArrayList<>();
                        list.addAll(hasilPesan);
                        adapterLap = new AdapterPeruser(AllLap.this, list, AllLap.this);
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
            public void onFailure(Call<ResponsePeruser> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSelect(LapPeruserItem data) {
        //buat dialog
        dialog = new AlertDialog.Builder(AllLap.this);
        //buat layout
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.pop_up_user, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        //inisialisasi
        txtNama = dialogView.findViewById(R.id.txt_nama);
        txtGender = dialogView.findViewById(R.id.txt_gender);
        txtAlamat = dialogView.findViewById(R.id.txt_alamat);
        txtNik = dialogView.findViewById(R.id.txt_nik);
        txtTmp = dialogView.findViewById(R.id.txt_tgl);
        imageView = dialogView.findViewById(R.id.image);

        //set
        Glide.with(AllLap.this).load(URL_IMAGE + data.getFoto())
                .thumbnail(0.5f)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        txtNama.setText(data.getNama());
        txtGender.setText(data.getJenkel());
        txtNik.setText(data.getNik());
        txtAlamat.setText(data.getAlamat());
        txtTmp.setText(data.getTmp() + ", " + ubahTanggal2(data.getTgl()));

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
}
