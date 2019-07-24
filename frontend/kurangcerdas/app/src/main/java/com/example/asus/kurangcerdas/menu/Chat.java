package com.example.asus.kurangcerdas.menu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asus.kurangcerdas.R;
import com.example.asus.kurangcerdas.adapter.AdapterChat;
import com.example.asus.kurangcerdas.model.ResponseInsert;
import com.example.asus.kurangcerdas.model.chat.ChatItem;
import com.example.asus.kurangcerdas.model.chat.ResponseChat;
import com.example.asus.kurangcerdas.network.ApiService;
import com.example.asus.kurangcerdas.network.RetroClient;
import com.example.asus.kurangcerdas.shared.SharedLogin;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {
    @BindView(R.id.rv_pesan)
    RecyclerView rvPesan;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.itemSender)
    CardView itemSender;
    @BindView(R.id.form_message)
    LinearLayout formMessage;
    @BindView(R.id.swipe_main)
    SmartRefreshLayout swipeMain;
    @BindView(R.id.constrain_main)
    ConstraintLayout constrainMain;
    SharedLogin sharedLogin;
    SimpleDateFormat dateFormat;
    Date date;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedLogin = new SharedLogin(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvPesan.setLayoutManager(layoutManager);
        getPesan();

        swipeMain.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getPesan();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        swipeMain.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getPesan();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    private void getPesan() {
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseChat> call = apiService.getChat(sharedLogin.getSharedString(SharedLogin.SP_ID));
        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                List<ChatItem> hasilPesan = response.body().getChat();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().isStatus() == true) {
                    AdapterChat adapterPesan = new AdapterChat(hasilPesan, Chat.this);
                    //  swipeMain.setRefreshing(false);
                    rvPesan.setAdapter(adapterPesan);


                } else {
                    Log.e("Tag", "Gagal req data ");
                }
            }

            @Override
            public void onFailure(Call<ResponseChat> call, Throwable t) {
                Log.e("Tag", "Gagal jaringan " + t.getMessage());

            }
        });
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked1() {
        String pesan = edtMessage.getText().toString();

        if (pesan.isEmpty()) {
            edtMessage.setError("Field TIdak Bisa Kosong");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(Chat.this);
            progressDialog.setMessage("Send Pesan");
            progressDialog.show();

            ApiService apiService = RetroClient.getApiService();
            Call<ResponseInsert> call = apiService.insertChat(sharedLogin.getSharedString(SharedLogin.SP_ID), "1",
                    edtMessage.getText().toString(), dateFormat.format(date), "0");
            call.enqueue(new Callback<ResponseInsert>() {
                @Override
                public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                    if (response.body().getCode() == 200) {
                        progressDialog.dismiss();
                        edtMessage.setText("");
                        getPesan();
                    } else {
                        Toast.makeText(Chat.this, "Gagal Kirim Data", Toast.LENGTH_SHORT).show();
                    }
                }

                //
                @Override
                public void onFailure(Call<ResponseInsert> call, Throwable t) {
                    Log.e("Tag", "Error jaringan saat insert :" + t.getMessage());
                    Toast.makeText(Chat.this, "Error jaringan saat insert", Toast.LENGTH_SHORT).show();


                }
            });

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

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }
}
