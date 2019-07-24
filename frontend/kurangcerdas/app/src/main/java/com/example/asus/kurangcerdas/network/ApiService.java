package com.example.asus.kurangcerdas.network;

import com.example.asus.kurangcerdas.model.ResponseBerita;
import com.example.asus.kurangcerdas.model.ResponseInsert;
import com.example.asus.kurangcerdas.model.ResponseNoUrut;
import com.example.asus.kurangcerdas.model.ResponseUser;
import com.example.asus.kurangcerdas.model.chat.ResponseChat;
import com.example.asus.kurangcerdas.model.laporan.ResponseLaporan;

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
    @POST("read/login_user.php")
    Call<ResponseUser> login_user(@Field("nama") String id, @Field("password") String password);

    @GET("read/no_user.php")
    Call<ResponseNoUrut> getNoUrut();

    @GET("read/berita.php")
    Call<ResponseBerita> getBerita();

    @FormUrlEncoded
    @POST("read/all_laporan.php")
    Call<ResponseLaporan> getLaporan(@Field("id") String id);

    @FormUrlEncoded
    @POST("read/chat_user.php")
    Call<ResponseChat> getChat(@Field("id") String id);

    @FormUrlEncoded
    @POST("create/chat.php")
    Call<ResponseInsert> insertChat(@Field("id_user") String id_user, @Field("id_admin") String id_admin,
                                       @Field("pesan") String pesan, @Field("waktu") String waktu, @Field("kode") String kode);

    //create
    @FormUrlEncoded
    @POST("create/user.php")
    Call<ResponseInsert> insretUser(@Field("nik") String nik, @Field("nama") String nama,
                                       @Field("tempat_lahir") String tempat_lahir, @Field("tanggal_lahir") String tanggal_lahir,  @Field("jenkel") String jenkel,
                                       @Field("alamat") String alamat,  @Field("rt") String rt, @Field("desa") String desa,  @Field("kec") String kec,
                                        @Field("kabupaten") String kabupaten,  @Field("agama") String agama, @Field("password") String password);


    @Multipart
    @POST("create/laporan.php")
    Call<ResponseInsert> insretLap(@Part("id_user") String id,
                                       @Part MultipartBody.Part image,
                                       @Part("hub_pelap") String hubPelap,
                                       @Part("nama_korban") String namaKorban,
                                       @Part("jk_korban") String jkKorban,
                                       @Part("usia_korb") String usiaKorb,
                                       @Part("alamat_korban") String alamatKorban,
                                       @Part("nm_pelaku") String nmPelaku,
                                       @Part("jk_pelaku") String jkPelaku,
                                       @Part("hub_pelaku") String hubPelaku,
                                       @Part("jenis_ks") String jenisKs,
                                       @Part("tgl_kej") String tglKej,
                                       @Part("kronologi") String kronologi);


    //update
    @FormUrlEncoded
    @POST("update/user.php")
    Call<ResponseInsert> updateUser(@Field("nik") String nik,@Field("password") String password);

    @FormUrlEncoded
    @POST("delete/delete.php")
    Call<ResponseInsert> delete(@Field("tabel") String tabel, @Field("cari") String cari, @Field("id_data") String idData);
}