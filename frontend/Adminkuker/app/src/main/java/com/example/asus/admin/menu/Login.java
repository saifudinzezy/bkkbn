package com.example.asus.admin.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.admin.R;
import com.example.asus.admin.model.AdminItem;
import com.example.asus.admin.model.ResponseAdmin;
import com.example.asus.admin.network.ApiService;
import com.example.asus.admin.network.RetroClient;
import com.example.asus.admin.shared.SharedLogin;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.admin.helper.FunctionError.cekEditText;
import static com.example.asus.admin.helper.FunctionError.setErrorEditText;
import static com.example.asus.admin.shared.SharedLogin.SP_EMAIL;
import static com.example.asus.admin.shared.SharedLogin.SP_ID;
import static com.example.asus.admin.shared.SharedLogin.SP_NAMA;
import static com.example.asus.admin.shared.SharedLogin.SP_SANDI;
import static com.example.asus.admin.shared.SharedLogin.SP_SUDAH_LOGIN;
import static com.example.asus.admin.shared.SharedLogin.SP_SUDAH_LOGIN2;

public class Login extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedLogin sharedLogin;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.button3)
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedLogin = new SharedLogin(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        //cek apakah user sudah login
        if (sharedLogin.getSharedSudahLogin()) {
            if (sharedLogin.getSharedSudahLogin2()) {
                //cek login kedua
                startActivity(new Intent(Login.this, AllLap.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
            //cache
            editText.setText(sharedLogin.getSharedString(SP_EMAIL));
            editText2.setText(sharedLogin.getSharedString(SP_SANDI));
        }
    }


    @OnClick({R.id.btnLogin, R.id.button3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                break;
            case R.id.button3:
                if (cekEditText(editText)) {
                    editText.setError("Nama Kosong");
                } else if (TextUtils.isEmpty(editText2.getText().toString())) {
                    setErrorEditText(editText2, "Masukan Nama");
                } else {
                    login(editText.getText().toString(), editText2.getText().toString());
                }
                break;
        }
    }

    private void login(String email, final String sandi) {
        //eksekusi
        progressDialog.show();
        ApiService service = RetroClient.getApiService();
        Call<ResponseAdmin> login = service.loginAdmin(email, sandi);
        login.enqueue(new Callback<ResponseAdmin>() {
            @Override
            public void onResponse(Call<ResponseAdmin> call, Response<ResponseAdmin> response) {
                //cek code apakah sukses atau tidak
                progressDialog.dismiss();
                if (response.body().getCode() == 200) {
                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    List<AdminItem> dataAdmin = response.body().getAdmin();
                    //simpan di sharef pref
                    //simpan id_user, nama_user, email dan password
                    sharedLogin.saveSharedString(SP_ID, dataAdmin.get(0).getIdAdmin());
                    sharedLogin.saveSharedString(SP_NAMA, dataAdmin.get(0).getNama());
                    sharedLogin.saveSharedString(SP_EMAIL, dataAdmin.get(0).getEmail());
                    sharedLogin.saveSharedString(SP_SANDI, editText2.getText().toString());

                    //buat cache
                    sharedLogin.saveSharedBoolean(SP_SUDAH_LOGIN, true);
                    //cek login
                    sharedLogin.saveSharedBoolean(SP_SUDAH_LOGIN2, true);
                    //buka home jika berhasil login
                    startActivity(new Intent(Login.this, AllLap.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    //hancurkan activity
                    finish();
                } else {
                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAdmin> call, Throwable t) {
                //hilangkan loading
                progressDialog.dismiss();
                Toast.makeText(Login.this, "Koneksi Gagal Keserver", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}