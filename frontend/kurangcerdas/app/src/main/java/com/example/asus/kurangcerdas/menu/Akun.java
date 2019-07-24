package com.example.asus.kurangcerdas.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.kurangcerdas.R;
import com.example.asus.kurangcerdas.login;
import com.example.asus.kurangcerdas.model.ResponseInsert;
import com.example.asus.kurangcerdas.network.ApiService;
import com.example.asus.kurangcerdas.network.RetroClient;
import com.example.asus.kurangcerdas.shared.SharedLogin;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.kurangcerdas.helper.FunctionError.cekEditText;
import static com.example.asus.kurangcerdas.helper.FunctionError.getTextEditText;
import static com.example.asus.kurangcerdas.helper.FunctionError.setErrorEditText;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_AGAMA;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_ALAMAT;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_DESA;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_JENKEL;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_KAB;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_KEC;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_NAMA;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_NIK;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_PASSWORD;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_RT;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_SUDAH_LOGIN2;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_TANGGAL;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_TEMPAT;

public class Akun extends AppCompatActivity {

    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.img_keluar)
    ImageView imgKeluar;
    ProgressDialog progressDialog;
    SharedLogin sharedLogin;
    @BindView(R.id.tx1)
    TextView tx1;
    @BindView(R.id.tx2)
    TextView tx2;
    @BindView(R.id.tx3)
    TextView tx3;
    @BindView(R.id.tx4)
    TextView tx4;
    @BindView(R.id.tx5)
    TextView tx5;
    @BindView(R.id.tx6)
    TextView tx6;
    @BindView(R.id.tx7)
    TextView tx7;
    @BindView(R.id.tx8)
    TextView tx8;
    @BindView(R.id.tx9)
    TextView tx9;
    @BindView(R.id.tx10)
    TextView tx10;
    @BindView(R.id.tx11)
    TextView tx11;
    @BindView(R.id.txtsandi)
    ShowHidePasswordEditText txtsandi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        ButterKnife.bind(this);
        sharedLogin = new SharedLogin(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Tunggu Sebentar...");

        if (sharedLogin.getSharedSudahLogin()) {
            //cache
            tx1.setText(sharedLogin.getSharedString(SP_NIK));
            tx2.setText(sharedLogin.getSharedString(SP_NAMA));
            tx3.setText(sharedLogin.getSharedString(SP_TEMPAT));
            tx4.setText(sharedLogin.getSharedString(SP_TANGGAL));
            tx5.setText(sharedLogin.getSharedString(SP_JENKEL));
            tx6.setText(sharedLogin.getSharedString(SP_ALAMAT));
            tx7.setText(sharedLogin.getSharedString(SP_RT));
            tx8.setText(sharedLogin.getSharedString(SP_DESA));
            tx9.setText(sharedLogin.getSharedString(SP_KEC));
            tx10.setText(sharedLogin.getSharedString(SP_KAB));
            tx11.setText(sharedLogin.getSharedString(SP_AGAMA));
            txtsandi.setText(sharedLogin.getSharedString(SP_PASSWORD));

        }
    }

    @OnClick({R.id.img_edit, R.id.img_keluar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_edit:
                if (cekEditText(txtsandi)) {
                    setErrorEditText(txtsandi, "Sandi Kosong");
                }else {
                    simpan(sharedLogin.getSharedString(SP_NIK), getTextEditText(txtsandi));
                }
                break;
            case R.id.img_keluar:
                sharedLogin.saveSharedBoolean(SP_SUDAH_LOGIN2, false);
                startActivity(new Intent(Akun.this, login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
        }
    }

    private void simpan(final String nik, final String password) {
        //eksekusi
        progressDialog.show();
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.updateUser(nik, password);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                progressDialog.dismiss();
                if (response.body().getCode() == 200) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //simpan di sharef pref
                    //simpan id_user, nama_user, email dan password
                    sharedLogin.saveSharedString(SP_PASSWORD, password);
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
