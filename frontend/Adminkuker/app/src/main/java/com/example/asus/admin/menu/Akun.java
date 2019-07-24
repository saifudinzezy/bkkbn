package com.example.asus.admin.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.admin.R;
import com.example.asus.admin.model.ResponseInsert;
import com.example.asus.admin.network.ApiService;
import com.example.asus.admin.network.RetroClient;
import com.example.asus.admin.shared.SharedLogin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.admin.helper.FunctionError.cekEditText;
import static com.example.asus.admin.helper.FunctionError.getTextEditText;
import static com.example.asus.admin.helper.FunctionError.setErrorEditText;
import static com.example.asus.admin.shared.SharedLogin.SP_EMAIL;
import static com.example.asus.admin.shared.SharedLogin.SP_ID;
import static com.example.asus.admin.shared.SharedLogin.SP_NAMA;
import static com.example.asus.admin.shared.SharedLogin.SP_SANDI;
import static com.example.asus.admin.shared.SharedLogin.SP_SUDAH_LOGIN2;

public class Akun extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedLogin sharedLogin;
    @BindView(R.id.edt_nama)
    EditText edtNama;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_pass)
    EditText edtPass;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.img_keluar)
    ImageView imgKeluar;

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
            edtEmail.setText(sharedLogin.getSharedString(SP_EMAIL));
            edtPass.setText(sharedLogin.getSharedString(SP_SANDI));
            edtNama.setText(sharedLogin.getSharedString(SP_NAMA));
        }
    }

    @OnClick({R.id.img_edit, R.id.img_keluar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_edit:
                if (cekEditText(edtNama)) {
                    setErrorEditText(edtNama, "Nama Kosong");
                } else if (cekEditText(edtEmail)) {
                    setErrorEditText(edtEmail, "Email Kosong");
                } else if (cekEditText(edtPass)) {
                    setErrorEditText(edtPass, "Password Kosong");
                } else {
                    Toast.makeText(this, "Tunggu...", Toast.LENGTH_SHORT).show();
                    simpan(sharedLogin.getSharedString(SP_ID), getTextEditText(edtNama), getTextEditText(edtEmail),
                            getTextEditText(edtPass));
                }
                break;
            case R.id.img_keluar:
                sharedLogin.saveSharedBoolean(SP_SUDAH_LOGIN2, false);
                startActivity(new Intent(Akun.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
        }
    }

    private void simpan(final String id_admin, final String nama, final String email, final String sandi) {
        //eksekusi
        progressDialog.show();
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.updateAdmin(id_admin, nama, sandi, email);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                progressDialog.dismiss();
                if (response.body().getCode() == 200) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //simpan di sharef pref
                    //simpan id_user, nama_user, email dan password
                    sharedLogin.saveSharedString(SP_ID, id_admin);
                    sharedLogin.saveSharedString(SP_NAMA, nama);
                    sharedLogin.saveSharedString(SP_SANDI, sandi);
                    sharedLogin.saveSharedString(SP_EMAIL, email);
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
