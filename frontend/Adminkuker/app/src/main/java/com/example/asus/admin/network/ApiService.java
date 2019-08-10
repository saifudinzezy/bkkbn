package com.example.asus.admin.network;

import com.example.asus.admin.model.ResponseAdmin;
import com.example.asus.admin.model.ResponseBerita;
import com.example.asus.admin.model.ResponseInsert;
import com.example.asus.admin.model.chat.ResponseChat;
import com.example.asus.admin.model.group.ResponseGroup;
import com.example.asus.admin.model.laporan.ResponseLaporan;
import com.example.asus.admin.model.peruser.ResponsePeruser;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    //read
       @FormUrlEncoded
    @POST("read/login_admin.php")
       Call<ResponseAdmin> loginAdmin(@Field("email") String email, @Field("sandi") String sandi);

    @GET("read/berita.php")
    Call<ResponseBerita> getBerita();

    @GET("read/chat_admin.php")
    Call<ResponseGroup> getGroupChat();

    @FormUrlEncoded
    @POST("read/chat_user.php")
    Call<ResponseChat> getChat(@Field("id") String id);

    @FormUrlEncoded
    @POST("create/chat.php")
    Call<ResponseInsert> insertChat(@Field("id_user") String id_user, @Field("id_admin") String id_admin,
                                    @Field("pesan") String pesan, @Field("waktu") String waktu, @Field("kode") String kode);

    @Multipart
    @POST("create/berita.php")
    Call<ResponseInsert> insertBerta(@Part("judul") String judul,
                                     @Part MultipartBody.Part image,
                                     @Part("artikel") String artikel,
                                     @Part("tanggal") String tanggal);

    @GET("read/laporan_peruser.php")
    Call<ResponsePeruser> getPeruser();

    @GET("read/all_laporan.php")
    Call<ResponseLaporan> getLaporan();

    @FormUrlEncoded
    @POST("read/all_laporan.php")
    Call<ResponseLaporan> getLaporanId(@Field("id") String id);

    //update
    @FormUrlEncoded
    @POST("update/admin.php")
    Call<ResponseInsert> updateAdmin(@Field("id_admin") String id_admin, @Field("nama") String nama, @Field("sandi") String sandi,
                                     @Field("email") String email);

    @FormUrlEncoded
    @POST("update/laporan.php")
    Call<ResponseInsert> updateLaporan(@Field("id") String id, @Field("status") String status, @Field("pesan") String pesan);

    //berita
    @Multipart
    @POST("update/berita.php")
    Call<ResponseInsert> updateBerita1(@Part("id") int id,
                                       @Part("judul") String judul,
                                       @Part MultipartBody.Part image,
                                       @Part("artikel") String artikel,
                                       @Part("hapus") String hapus,
                                       @Part("tanggal") String tanggal);

    @Multipart
    @POST("update/berita.php")
    Call<ResponseInsert> updateBerita2(@Part("id") int id,
                                       @Part("judul") String judul,
                                       @Part("artikel") String artikel,
                                       @Part("tanggal") String tanggal);

    //delete
    @FormUrlEncoded
    @POST("delete/delete.php")
    Call<ResponseInsert> delete(@Field("tabel") String tabel, @Field("cari") String cari, @Field("id_data") String idData);

}