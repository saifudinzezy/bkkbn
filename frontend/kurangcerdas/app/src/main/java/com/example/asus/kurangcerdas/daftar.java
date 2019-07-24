package com.example.asus.kurangcerdas;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.kurangcerdas.helper.FunctionError;
import com.example.asus.kurangcerdas.model.ResponseInsert;
import com.example.asus.kurangcerdas.model.ResponseNoUrut;
import com.example.asus.kurangcerdas.network.ApiService;
import com.example.asus.kurangcerdas.network.RetroClient;
import com.example.asus.kurangcerdas.shared.SharedLogin;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.kurangcerdas.helper.ConvertDate.ubahTanggal2;
import static com.example.asus.kurangcerdas.helper.ConvertDate.ubahTanggal3;
import static com.example.asus.kurangcerdas.helper.FunctionError.cekEditText;
import static com.example.asus.kurangcerdas.helper.FunctionError.getTextEditText;
import static com.example.asus.kurangcerdas.helper.FunctionError.getTextView;
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
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_TANGGAL;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_TEMPAT;

public class daftar extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedLogin sharedLogin;
    @BindView(R.id.txt_no_urut)
    TextView txtNoUrut;
    @BindView(R.id.edt_nik)
    EditText edtNik;
    @BindView(R.id.edt_nama)
    EditText edtNama;
    @BindView(R.id.edt_tempat)
    EditText edtTempat;
    @BindView(R.id.txt_lahir)
    TextView edtTanggal;
    @BindView(R.id.edt_alamat)
    EditText edtAlamat;
    @BindView(R.id.edt_rt)
    EditText edtRt;
    @BindView(R.id.edt_desa)
    EditText edtDesa;
    @BindView(R.id.edt_kec)
    EditText edtKec;
    @BindView(R.id.edt_kab)
    EditText edtKab;
    @BindView(R.id.edt_pass)
    EditText edtPass;
    @BindView(R.id.btn_daftar)
    Button btnDaftar;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.logodaftar)
    ImageView logodaftar;
    @BindView(R.id.edt_agama)
    Spinner edtAgama;
    @BindView(R.id.laki)
    RadioButton laki;
    @BindView(R.id.perempuan)
    RadioButton perempuan;
    @BindView(R.id.edt_jenkel)
    RadioGroup edtJenkel;
    private Calendar myCalendar;
    ArrayAdapter<CharSequence> adapterStatus;
    String agama,jk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        ButterKnife.bind(this);
        myCalendar = Calendar.getInstance();
        sharedLogin = new SharedLogin(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Tunggu Sebentar...");


        edtJenkel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb= (RadioButton) findViewById(checkedId);
                jk = rb.getText().toString();
            }
        });


        //spiner status
        final ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this, R.array.Agama,
                R.layout.support_simple_spinner_dropdown_item);
        edtAgama.setAdapter(adapterStatus);

        //select sp
        edtAgama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agama = adapterStatus.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getUser();
    }

    @OnClick({R.id.btn_daftar, R.id.txt_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_daftar:
                if (cekEditText(edtNik)) {
                    setErrorEditText(edtNik, "NIK Kosong");
                } else if (cekEditText(edtNama)) {
                    setErrorEditText(edtNama, "NAMA Kosong");
                } else if (cekEditText(edtTempat)) {
                    setErrorEditText(edtTempat, "Tempat Lahir Kosong");
                }/* else if (cekEditText(edtTanggal)) {
                    setErrorEditText(edtTanggal, "Tanggal Lahir Kosong");
                }  else if (cekEditText(edtJenkel)) {
                    setErrorEditText(edtJenkel, "Jenis Kelamin Kosong");
                } */ else if (cekEditText(edtAlamat)) {
                    setErrorEditText(edtAlamat, "Masukan Jalan/Dusun");
                } else if (cekEditText(edtRt)) {
                    setErrorEditText(edtRt, "RT/RW Kosong");
                } else if (cekEditText(edtKec)) {
                    setErrorEditText(edtKec, "Kecamatan Kosong");
                } else if (cekEditText(edtKab)) {
                    setErrorEditText(edtKab, "Kabupaten Kosong");
                }/* else if (cekEditText(edtAgama)) {
                    setErrorEditText(edtAgama, "Agama Kosong");
                } */ else if (cekEditText(edtPass)) {
                    setErrorEditText(edtPass, "Password Kosong");
                } else {
                    simpan(getTextEditText(edtNik), getTextEditText(edtNama),
                            getTextEditText(edtTempat), ubahTanggal3(getTextView(edtTanggal)), jk,
                            getTextEditText(edtAlamat), getTextEditText(edtRt), getTextEditText(edtDesa), getTextEditText(edtKec),
                            getTextEditText(edtKab), agama, getTextEditText(edtPass));
                }
                break;
            case R.id.txt_login:
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), login.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void simpan(final String nik, final String nama, final String tempat_lahir, final String tanggal_lahir, final String jenkel, final String alamat, final String rt, final String desa, final String kec, final String kabupaten, final String agama, final String password) {
        progressDialog.show();
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.insretUser(nik, nama, tempat_lahir, tanggal_lahir, jenkel, alamat, rt, desa, kec, kabupaten, agama, password);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                progressDialog.dismiss();
                if (response.body().getCode() == 200) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //simpan di sharef pref
                    //simpan id_user, nama_user, email dan password
                    sharedLogin.saveSharedString(SP_NIK, nik);
                    sharedLogin.saveSharedString(SP_NAMA, nama);
                    sharedLogin.saveSharedString(SP_TEMPAT, tempat_lahir);
                    sharedLogin.saveSharedString(SP_TANGGAL, tanggal_lahir);
                    sharedLogin.saveSharedString(SP_JENKEL, jenkel);
                    sharedLogin.saveSharedString(SP_ALAMAT, alamat);
                    sharedLogin.saveSharedString(SP_RT, rt);
                    sharedLogin.saveSharedString(SP_DESA, desa);
                    sharedLogin.saveSharedString(SP_KEC, kec);
                    sharedLogin.saveSharedString(SP_KAB, kabupaten);
                    sharedLogin.saveSharedString(SP_AGAMA, agama);
                    sharedLogin.saveSharedString(SP_PASSWORD, password);

                    getUser();
                    kosongkan();
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.e("error", t.getMessage());
            }
        });
    }

    private void getUser() {
        progressDialog.show();
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseNoUrut> call = apiService.getNoUrut();
        call.enqueue(new Callback<ResponseNoUrut>() {
            @Override
            public void onResponse(Call<ResponseNoUrut> call, Response<ResponseNoUrut> response) {
                //Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().getSukses().equalsIgnoreCase("true")) {
                    try {
                        txtNoUrut.setText(response.body().getNoUrut());
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        progressDialog.dismiss();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseNoUrut> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

   /*  */

    private void kosongkan() {
        FunctionError.kosongkan(edtNik);
        FunctionError.kosongkan(edtNama);
        FunctionError.kosongkan(edtTempat);
//        FunctionError.kosongkan(edtTanggal);
//        FunctionError.kosongkan(edtJenkel);
        FunctionError.kosongkan(edtAlamat);
        FunctionError.kosongkan(edtRt);
        FunctionError.kosongkan(edtDesa);
        FunctionError.kosongkan(edtKec);
        FunctionError.kosongkan(edtKab);
//        FunctionError.kosongkan(edtAgama);
        FunctionError.kosongkan(edtPass);
    }

    @OnClick(R.id.txt_lahir)
    public void onClick() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String formatTanggal = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                Toast.makeText(daftar.this, "" + ubahTanggal2(sdf.format(myCalendar.getTime())), Toast.LENGTH_SHORT).show();
                edtTanggal.setText(ubahTanggal2(sdf.format(myCalendar.getTime())));
            }
        },
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}
