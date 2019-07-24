package com.example.asus.admin.menu;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.asus.admin.R;
import com.example.asus.admin.adapter.AdapterGroup;
import com.example.asus.admin.model.group.ChatItem;
import com.example.asus.admin.model.group.ResponseGroup;
import com.example.asus.admin.network.ApiService;
import com.example.asus.admin.network.RetroClient;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_pesan)
    RecyclerView rvPesan;
    @BindView(R.id.swipe_main)
    SmartRefreshLayout swipeMain;
    @BindView(R.id.constrain_main)
    ConstraintLayout constrainMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        Call<ResponseGroup> call = apiService.getGroupChat();
        call.enqueue(new Callback<ResponseGroup>() {
            @Override
            public void onResponse(Call<ResponseGroup> call, Response<ResponseGroup> response) {
                List<ChatItem> hasilPesan = response.body().getChat();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().isStatus() == true) {
                    AdapterGroup adapterPesan = new AdapterGroup(Chat.this, hasilPesan);
                    //  swipeMain.setRefreshing(false);
                    rvPesan.setAdapter(adapterPesan);


                } else {
                    Log.e("Tag", "Gagal req data ");
                }
            }

            @Override
            public void onFailure(Call<ResponseGroup> call, Throwable t) {
                Log.e("Tag", "Gagal jaringan " + t.getMessage());

            }
        });
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
