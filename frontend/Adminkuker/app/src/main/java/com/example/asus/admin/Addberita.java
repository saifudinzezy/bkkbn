package com.example.asus.admin;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.admin.adapter.BeritaAdapter;
import com.example.asus.admin.helper.CRUDBerita;
import com.example.asus.admin.model.BeritaItem;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import net.dankito.richtexteditor.android.RichTextEditor;
import net.dankito.richtexteditor.android.toolbar.GroupedCommandsEditorToolbar;
import net.dankito.utils.android.permissions.IPermissionsService;
import net.dankito.utils.android.permissions.PermissionsService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.asus.admin.helper.Constan.KEY_DATA;
import static com.example.asus.admin.helper.Constan.URL_IMAGE;
import static com.example.asus.admin.helper.ConvertDate.tglHariIni;
import static com.example.asus.admin.helper.FunctionError.cekEditText;
import static com.example.asus.admin.helper.FunctionError.getTextEditText;
import static com.example.asus.admin.helper.FunctionError.setErrorEditText;

public class Addberita extends AppCompatActivity implements CRUDBerita.OnReset, BeritaAdapter.OnFunction {
    //var img
    ProgressDialog progressDialog;
    BeritaItem data;
    IPermissionsService permissionsService;
    CRUDBerita crudBerita;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wrap)
    AppBarLayout wrap;
    @BindView(R.id.edt_judul)
    EditText edtJudul;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.txt_insert)
    TextView txtInsert;
    @BindView(R.id.txt_place)
    TextView txtPlace;
    @BindView(R.id.txt_view)
    TextView txtView;
    @BindView(R.id.editor)
    RichTextEditor editor;
    @BindView(R.id.editorToolbar)
    GroupedCommandsEditorToolbar editorToolbar;
    private int GALLERY = 1, CAMERA = 2;
    String partImage = "";
    boolean update = false;
    private static final String IMAGE_DIRECTORY = "/demonuts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_berita);
        ButterKnife.bind(this);

        requestMultiplePermissions();
        permissionsService = new PermissionsService(this);
        crudBerita = new CRUDBerita(this, this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Tunggu Sebentar...");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Berita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editorToolbar.setEditor(editor);

        editor.setEditorFontSize(20);
        editor.setPadding((4 * (int) getResources().getDisplayMetrics().density));
        editor.setHtml(" ");

        data = getIntent().getParcelableExtra(KEY_DATA);
        if (data != null) {
            //Toast.makeText(this, data.getIdAdmin(), Toast.LENGTH_SHORT).show();
            Glide.with(this)
                    .load(URL_IMAGE + data.getGambar())
                    .into(img);
            edtJudul.setText(data.getJudul());
            editor.setHtml(data.getArtikel());
            txtPlace.setText(data.getGambar());
            getSupportActionBar().setTitle("Ubah Data");
        } else {
            getSupportActionBar().setTitle("Tambah Data");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_berita, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            if (data == null) {
                if (cekEditText(edtJudul)) {
                    setErrorEditText(edtJudul, "Judul Kosong");
                } else {
                    crudBerita.simpan(partImage, getTextEditText(edtJudul), editor.getCachedHtml(),
                            tglHariIni(), progressDialog);
                }
            } else {
                //jika place sama dengan nama imagenya
                if (data.getGambar() == txtPlace.getText().toString()) {
                    crudBerita.updateWithField(data.getIdBerita(), getTextEditText(edtJudul), editor.getCachedHtml(),
                            data.getTanggal());
                } else {
                    crudBerita.updateWithImage(partImage, data.getIdBerita(), getTextEditText(edtJudul),
                            editor.getCachedHtml(), data.getTanggal(), data.getGambar());
                }
                finish();
            }
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick({R.id.txt_insert, R.id.txt_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_insert:
                showPictureDialog();
                break;
            case R.id.txt_view:
                if (img.getVisibility() == View.GONE) {
                    img.setVisibility(View.VISIBLE);
                    Drawable img = getResources().getDrawable(R.drawable.ic_visibility_black_24dp);
                    //sesuaikan image sesuai isian bisa di start/awal, top/atas,end/akhir, bottom/bawah
                    txtView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, img, null);
                } else {
                    img.setVisibility(View.GONE);
                    Drawable img = getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp
                    );
                    //sesuaikan image sesuai isian bisa di start/awal, top/atas,end/akhir, bottom/bawah
                    txtView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, img, null);
                }
                break;
        }
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
//                    Toast.makeText(Addberita.this, "Image Saved! " + path, Toast.LENGTH_SHORT).show();
                    partImage = path;
                    img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Addberita.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(thumbnail);
            partImage = saveImage(thumbnail);
//            Toast.makeText(Addberita.this, "Image Saved! " + thumbnail, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onReset() {

    }

    @Override
    public void onDelete(BeritaItem data) {

    }
}
