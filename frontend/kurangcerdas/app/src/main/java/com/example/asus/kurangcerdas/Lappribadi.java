package com.example.asus.kurangcerdas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
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
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_ID;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_JENKEL;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_KAB;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_KEC;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_NAMA;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_NIK;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_RT;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_TANGGAL;
import static com.example.asus.kurangcerdas.shared.SharedLogin.SP_TEMPAT;

public class Lappribadi extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedLogin sharedLogin;
    @BindView(R.id.txt_lahir)
    TextView edtTanggal;
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
    @BindView(R.id.edt_hub)
    Spinner edtHub;
    @BindView(R.id.tx13)
    EditText tx13;
    @BindView(R.id.laki)
    RadioButton laki;
    @BindView(R.id.perempuan)
    RadioButton perempuan;
    @BindView(R.id.edt_jenkel)
    RadioGroup edtJenkel;
    @BindView(R.id.tx14)
    EditText tx14;
    @BindView(R.id.tx15)
    EditText tx15;
    @BindView(R.id.laki1)
    RadioButton laki1;
    @BindView(R.id.perempuan2)
    RadioButton perempuan2;
    @BindView(R.id.edt_jenkel1)
    RadioGroup edtJenkel1;
    @BindView(R.id.edt_hub1)
    Spinner edtHub1;
    @BindView(R.id.edt_ks)
    Spinner edtKs;
    @BindView(R.id.tx22)
    EditText tx22;
    @BindView(R.id.btn_buatlap)
    Button btnBuatlap;
    @BindView(R.id.open_image)
    Button openImage;
    @BindView(R.id.txt_view)
    TextView txtView;
    @BindView(R.id.showImg)
    ImageView showImg;
    @BindView(R.id.tx17)
    EditText tx17;
    private Calendar myCalendar;
    String hub, hub1, ks, jk, jk1;

    private int GALLERY = 1, CAMERA = 2;
    String partImage = "";
    boolean update = false;
    private static final String IMAGE_DIRECTORY = "/demonuts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lappribadi);
        ButterKnife.bind(this);

        requestMultiplePermissions();

        sharedLogin = new SharedLogin(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Tunggu Sebentar...");

        edtJenkel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                jk = rb.getText().toString();
            }
        });

        edtJenkel1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                jk1 = rb.getText().toString();
            }
        });

        //spiner status
        final ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this, R.array.Hub,
                R.layout.support_simple_spinner_dropdown_item);
        edtHub.setAdapter(adapterStatus);

        //select sp
        edtHub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hub = adapterStatus.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spiner status
        final ArrayAdapter<CharSequence> adapterStatus1 = ArrayAdapter.createFromResource(this, R.array.Hub,
                R.layout.support_simple_spinner_dropdown_item);
        edtHub1.setAdapter(adapterStatus1);

        //select sp
        edtHub1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hub1 = adapterStatus1.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spiner status
        final ArrayAdapter<CharSequence> adapterStatus2 = ArrayAdapter.createFromResource(this, R.array.Kasus,
                R.layout.support_simple_spinner_dropdown_item);
        edtKs.setAdapter(adapterStatus);

        //select sp
        edtKs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ks = adapterStatus2.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        }
        myCalendar = Calendar.getInstance();

    }

    private void kosongkan() {
        FunctionError.kosongkan(tx13);
        FunctionError.kosongkan(tx14);
        FunctionError.kosongkan(tx14);
        FunctionError.kosongkan(tx15);
        FunctionError.kosongkan(tx17);
        FunctionError.kosongkan(tx22);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick({R.id.txt_lahir, R.id.open_image, R.id.txt_view, R.id.btn_buatlap})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_image:
                showPictureDialog();
                break;
            case R.id.txt_view:
                if (showImg.getVisibility() == View.GONE) {
                    showImg.setVisibility(View.VISIBLE);
                    Drawable showImg = getResources().getDrawable(R.drawable.ic_visibility_grey_900_24dp);
                    //sesuaikan image sesuai isian bisa di start/awal, top/atas,end/akhir, bottom/bawah
                    txtView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, showImg, null);
                } else {
                    showImg.setVisibility(View.GONE);
                    Drawable showImg = getResources().getDrawable(R.drawable.ic_eye_hide
                    );
                    //sesuaikan image sesuai isian bisa di start/awal, top/atas,end/akhir, bottom/bawah
                    txtView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, showImg, null);
                }
                break;
            case R.id.txt_lahir:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String formatTanggal = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        Toast.makeText(Lappribadi.this, "" + ubahTanggal2(sdf.format(myCalendar.getTime())), Toast.LENGTH_SHORT).show();
                        edtTanggal.setText(ubahTanggal2(sdf.format(myCalendar.getTime())));
                    }
                },
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_buatlap:
                switch (view.getId()) {
                    case R.id.btn_buatlap:
                        if (cekEditText(tx13)) {
                            setErrorEditText(tx13, "Kosong");
                        } else if (cekEditText(tx14)) {
                            setErrorEditText(tx14, "Kosong");
                        } else if (cekEditText(tx15)) {
                            setErrorEditText(tx15, "Kosong");
                        } else if (cekEditText(tx17)) {
                            setErrorEditText(tx17, "Kosong");
                        } else if (cekEditText(tx22)) {
                            setErrorEditText(tx22, "Kosong");
                        } else {
                            simpan(sharedLogin.getSharedString(SP_ID), hub, getTextEditText(tx13), jk, getTextEditText(tx14), getTextEditText(tx15), getTextEditText(tx17),
                                    jk1, hub1, ks, ubahTanggal3(getTextView(edtTanggal)), getTextEditText(tx22), partImage);
                        }
                        break;
                }
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
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    //open camera
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                    Toast.makeText(Lappribadi.this, "Image Saved! " + path, Toast.LENGTH_SHORT).show();
                    partImage = path;
                    showImg.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Lappribadi.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            showImg.setImageBitmap(thumbnail);
            partImage = saveImage(thumbnail);
            Toast.makeText(Lappribadi.this, "Image Saved! " + thumbnail, Toast.LENGTH_SHORT).show();
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

    private void simpan(String id, String hubPelap, String namaKorban, String jkKorban, String usiaKorb, String alamatKorban,
                        String nmPelaku, String jkPelaku, String hubPelaku, String jenisKs, String tglKej,
                        String kronologi, String part) {
        progressDialog.show();
        File imageFile = new File(part);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        //parm 1 samakan dengan parameter di backend
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.insretLap(id, partImage, hubPelap, namaKorban, jkKorban, usiaKorb, alamatKorban,
                nmPelaku, jkPelaku, hubPelaku, jenisKs, tglKej, kronologi);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                progressDialog.dismiss();
                if (response.body().getCode() == 200) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

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

}
