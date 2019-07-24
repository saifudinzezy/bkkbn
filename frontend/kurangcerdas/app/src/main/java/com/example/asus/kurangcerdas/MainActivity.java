package com.example.asus.kurangcerdas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.asus.kurangcerdas.helper.Buka;
import com.example.asus.kurangcerdas.menu.Akun;
import com.example.asus.kurangcerdas.menu.Berita;
import com.example.asus.kurangcerdas.menu.Chat;
import com.example.asus.kurangcerdas.menu.Statuslap;
import com.example.asus.kurangcerdas.model.ResponseBerita;
import com.example.asus.kurangcerdas.network.ApiService;
import com.example.asus.kurangcerdas.network.RetroClient;
import com.example.asus.kurangcerdas.shared.SharedLogin;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.kurangcerdas.helper.Constan.URL_IMAGE;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener, View.OnClickListener {
    private SliderLayout sliderLayout;
    private ImageView btninfo,btnakun,btnchat,btntambah, btnStatus;
    SharedLogin sharedLogin;
    HashMap<String, String> HashMapForURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedLogin = new SharedLogin(this);

        //cek apakah user sudah login
        if (sharedLogin.getSharedSudahLogin()) {
            if (!sharedLogin.getSharedSudahLogin2()) {
                //cek login kedua
                startActivity(new Intent(MainActivity.this, login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }

        konek();

        sliderLayout = (SliderLayout) findViewById(R.id.slider);

        //Call this method if you want to add images from URL .
        //AddImagesUrlOnline();
        getBerita();

        //Call this method to add images from local drawable folder .
        //AddImageUrlFormLocalRes();

        //Call this method to stop automatic sliding.
        //sliderLayout.stopAutoCycle();

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(4000);

        sliderLayout.addOnPageChangeListener(MainActivity.this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image1:
                //TODO BUKA HALAMAN
                Buka.buka(MainActivity.this, tentang.class);
                break;
            case R.id.gbakun:
                Buka.buka(MainActivity.this, Akun.class);
                break;
            case R.id.gbchat:
                Buka.buka(MainActivity.this, Chat.class);
                break;
            case R.id.gbtambah:
                Buka.buka(MainActivity.this, Lappribadi.class);
                break;
                case R.id.gbstatus:
                Buka.buka(MainActivity.this, Statuslap.class);
                break;
        }}

    private void konek() {
        btninfo = (ImageView) findViewById(R.id.image1);
        btnakun = (ImageView) findViewById(R.id.gbakun);
        btnchat = (ImageView) findViewById(R.id.gbchat);
        btntambah = (ImageView) findViewById(R.id.gbtambah);
        btnStatus = (ImageView) findViewById(R.id.gbstatus);


        btninfo.setOnClickListener(this);
        btnakun.setOnClickListener(this);
        btnchat.setOnClickListener(this);
        btntambah.setOnClickListener(this);
        btnStatus.setOnClickListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Buka.buka(MainActivity.this, Berita.class);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getBerita() {
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseBerita> call = apiService.getBerita();
        call.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                //Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().getSukses().equalsIgnoreCase("true")) {
                    try {
                        HashMapForURL = new HashMap<String, String>();

                        for (int i = 0; i < response.body().getBerita().size(); i++) {
                            HashMapForURL.put(response.body().getBerita().get(i).getJudul(),
                                    URL_IMAGE + response.body().getBerita().get(i).getGambar());
                        }

                        for (String name : HashMapForURL.keySet()) {

                            TextSliderView textSliderView = new TextSliderView(MainActivity.this);

                            textSliderView
                                    .description(name)
                                    .image(HashMapForURL.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(MainActivity.this);

                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", name);

                            sliderLayout.addSlider(textSliderView);
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                }
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}


