package com.example.asus.kurangcerdas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @BindView(R.id.txt_image)
    TextView txtImage;
    @BindView(R.id.image)
    ImageView image;
    private Calendar myCalendar;
    ArrayAdapter<CharSequence> adapterStatus;
    String agama, jk;
    private int GALLERY = 1, CAMERA = 2;
    String partImage = "";
    private static final String IMAGE_DIRECTORY = "/demonuts";

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
        requestMultiplePermissions();

        edtJenkel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
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

    @OnClick({R.id.txt_lahir, R.id.txt_image, R.id.btn_daftar, R.id.txt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_lahir:
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
                break;
            case R.id.txt_image:
                showPictureDialog();
                break;
            case R.id.btn_daftar:
                if (cekEditText(edtNik)) {
                    setErrorEditText(edtNik, "NIK Kosong");
                } else if (cekEditText(edtNama)) {
                    setErrorEditText(edtNama, "NAMA Kosong");
                } else if (cekEditText(edtTempat)) {
                    setErrorEditText(edtTempat, "Tempat Lahir Kosong");
                } else if (cekEditText(edtAlamat)) {
                    setErrorEditText(edtAlamat, "Masukan Jalan/Dusun");
                } else if (cekEditText(edtRt)) {
                    setErrorEditText(edtRt, "RT/RW Kosong");
                } else if (cekEditText(edtKec)) {
                    setErrorEditText(edtKec, "Kecamatan Kosong");
                } else if (cekEditText(edtKab)) {
                    setErrorEditText(edtKab, "Kabupaten Kosong");
                } else if (cekEditText(edtPass)) {
                    setErrorEditText(edtPass, "Password Kosong");
                } else {
                    register(getTextEditText(edtNik), getTextEditText(edtNama),
                            getTextEditText(edtTempat), ubahTanggal3(getTextView(edtTanggal)), jk,
                            getTextEditText(edtAlamat), getTextEditText(edtRt), getTextEditText(edtDesa), getTextEditText(edtKec),
                            getTextEditText(edtKab), agama, getTextEditText(edtPass));
                }
                break;
            case R.id.txt_login:
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    //dialog
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    //open galleri
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    //open camera
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    //on result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(daftar.this, "Image Saved! " + path, Toast.LENGTH_SHORT).show();
                    partImage = path;
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(daftar.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
            partImage = saveImage(thumbnail);
            Toast.makeText(daftar.this, "Image Saved! " + thumbnail, Toast.LENGTH_SHORT).show();
        }
    }

    //save image
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    //permission
    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    //register
    //insert
    private void register(final String nik, final String nama, final String tempat_lahir, final String tanggal_lahir,
                          final String jenkel, final String alamat, final String rt, final String desa, final String kec, final String kabupaten, final String agama, final String password) {
        File imageFile = new File(this.partImage);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        //parm 1 samakan dengan parameter di backend
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        //eksekusi
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.registrasi(nik, nama, tempat_lahir, tanggal_lahir, jenkel, alamat, rt, desa, kec, kabupaten, agama, password, partImage);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(daftar.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getUser();
                    kosongkan();
                } else {
                    Toast.makeText(daftar.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(daftar.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
