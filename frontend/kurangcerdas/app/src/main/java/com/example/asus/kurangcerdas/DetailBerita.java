package com.example.asus.kurangcerdas;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.asus.kurangcerdas.model.BeritaItem;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.asus.kurangcerdas.helper.Constan.KEY_DATA;
import static com.example.asus.kurangcerdas.helper.Constan.URL_IMAGE;

public class DetailBerita extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.artikel)
    HtmlTextView artikel;
    BeritaItem data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getParcelableExtra(KEY_DATA);
        if (data != null) {
            Glide.with(this)
                    .load(URL_IMAGE + data.getGambar())
                    .into(img);
            artikel.setHtml(data.getArtikel());
            getSupportActionBar().setTitle(data.getJudul());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
