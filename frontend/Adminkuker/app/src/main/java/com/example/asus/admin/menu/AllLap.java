package com.example.asus.admin.menu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class AllLap extends AppCompatActivity {

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
                        adapterLap = new AdapterPeruser(AllLap.this, list);
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

}
