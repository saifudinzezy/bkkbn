package com.example.asus.kurangcerdas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.kurangcerdas.helper.FunctionError;
import com.example.asus.kurangcerdas.model.ResponseUser;
import com.example.asus.kurangcerdas.model.UserItem;
import com.example.asus.kurangcerdas.network.ApiService;
import com.example.asus.kurangcerdas.network.RetroClient;
import com.example.asus.kurangcerdas.shared.SharedLogin;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.kurangcerdas.helper.FunctionError.cekEditText;
import static com.example.asus.kurangcerdas.helper.FunctionError.setErrorEditText;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_AGAMA;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_ALAMAT;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_DESA;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_ID;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_JENKEL;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_KAB;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_KEC;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_NAMA;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_NIK;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_PASSWORD;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_RT;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_SUDAH_LOGIN;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_SUDAH_LOGIN2;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_TANGGAL;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_TEMPAT;

public class login extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedLogin sharedLogin;
    @BindView(R.id.logologin)
    ImageView logologin;
    @BindView(R.id.txtlogin)
    EditText txtlogin;
    @BindView(R.id.akun)
    LinearLayout akun;
    @BindView(R.id.txtsandi)
    EditText txtsandi;
    @BindView(R.id.pass)
    LinearLayout pass;
    @BindView(R.id.butmasuk)
    Button butmasuk;
    @BindView(R.id.daftar)
    TextView daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedLogin = new SharedLogin(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Memeriksa...");

        //cek apakah user sudah login
        if (sharedLogin.getSharedSudahLogin()) {
            if (sharedLogin.getSharedSudahLogin2()) {
                //cek login kedua
                startActivity(new Intent(login.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }

        }

    }

    @OnClick({R.id.butmasuk, R.id.daftar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.butmasuk:
                if (cekEditText(txtlogin)) {
                    txtlogin.setError("NAMA Kosong");
                } else if (TextUtils.isEmpty(txtsandi.getText().toString())) {
                    setErrorEditText(txtsandi, "Masukan NAMA");
                } else {
                    login(txtsandi.getText().toString(), txtsandi.getText().toString());
                }
                break;
            case R.id.daftar:
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), daftar.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
                startActivity(intent);
                finish();
                break;
        }
    }
    private void login(String email, final String password) {
        //eksekusi
        progressDialog.show();
        ApiService service = RetroClient.getApiService();
        Call<ResponseUser> login = service.login_user(email, password);
        login.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                //cek code apakah sukses atau tidak
                progressDialog.dismiss();
                if (response.body().getCode() == 200) {
                    Toast.makeText(login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    List<UserItem> dataUser = response.body().getUser();
                    //simpan di sharef pref
                    //simpan id_user, nama_user, email dan password
                    sharedLogin.saveSharedString(SP_ID, dataUser.get(0).getId());
                    sharedLogin.saveSharedString(SP_NAMA, dataUser.get(0).getNama());
                    sharedLogin.saveSharedString(SP_PASSWORD, txtsandi.getText().toString());
                    sharedLogin.saveSharedString(SP_NIK,dataUser.get(0).getNik());
                    sharedLogin.saveSharedString(SP_TEMPAT, dataUser.get(0).getTempatLahir());
                    sharedLogin.saveSharedString(SP_TANGGAL, dataUser.get(0).getTanggalLahir());
                    sharedLogin.saveSharedString(SP_JENKEL, dataUser.get(0).getJenkel());
                    sharedLogin.saveSharedString(SP_ALAMAT, dataUser.get(0).getAlamat());
                    sharedLogin.saveSharedString(SP_RT, dataUser.get(0).getRt());
                    sharedLogin.saveSharedString(SP_DESA, dataUser.get(0).getDesa());
                    sharedLogin.saveSharedString(SP_KEC, dataUser.get(0).getKec());
                    sharedLogin.saveSharedString(SP_KAB, dataUser.get(0).getKabupaten());
                    sharedLogin.saveSharedString(SP_AGAMA, dataUser.get(0).getAgama());
                    kosongkan();
                    //buat cache
                    sharedLogin.saveSharedBoolean(SP_SUDAH_LOGIN, true);
                    //cek login
                    sharedLogin.saveSharedBoolean(SP_SUDAH_LOGIN2, true);
                                        //buka home jika berhasil login
                    startActivity(new Intent(login.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    //hancurkan activity
                    finish();
                } else {
                    Toast.makeText(login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                //hilangkan loading
                progressDialog.dismiss();
                Toast.makeText(login.this, "Koneksi Gagal Keserver", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    private void kosongkan() {
        FunctionError.kosongkan(txtlogin);
        FunctionError.kosongkan(txtsandi);
    }

}

