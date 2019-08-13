package com.example.asus.admin.model.peruser;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LapPeruserItem implements Parcelable {

	@SerializedName("nik")
	private String nik;

	@SerializedName("total")
	private String total;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("tmp")
	private String tmp;

	@SerializedName("tgl")
	private String tgl;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("jenkel")
	private String jenkel;

	@SerializedName("id_lapor")
	private String idLapor;

	@SerializedName("alamat")
	private String alamat;

	public void setNik(String nik){
		this.nik = nik;
	}

	public String getNik(){
		return nik;
	}

	public void setTotal(String total){
		this.total = total;
	}

	public String getTotal(){
		return total;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setTmp(String tmp){
		this.tmp = tmp;
	}

	public String getTmp(){
		return tmp;
	}

	public void setTgl(String tgl){
		this.tgl = tgl;
	}

	public String getTgl(){
		return tgl;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setJenkel(String jenkel){
		this.jenkel = jenkel;
	}

	public String getJenkel(){
		return jenkel;
	}

	public void setIdLapor(String idLapor){
		this.idLapor = idLapor;
	}

	public String getIdLapor(){
		return idLapor;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	@Override
 	public String toString(){
		return 
			"LapPeruserItem{" + 
			"nik = '" + nik + '\'' + 
			",total = '" + total + '\'' + 
			",nama = '" + nama + '\'' + 
			",foto = '" + foto + '\'' + 
			",tmp = '" + tmp + '\'' + 
			",tgl = '" + tgl + '\'' + 
			",id_user = '" + idUser + '\'' + 
			",jenkel = '" + jenkel + '\'' + 
			",id_lapor = '" + idLapor + '\'' + 
			",alamat = '" + alamat + '\'' + 
			"}";
		}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nik);
        dest.writeString(this.total);
        dest.writeString(this.nama);
        dest.writeString(this.foto);
        dest.writeString(this.tmp);
        dest.writeString(this.tgl);
        dest.writeString(this.idUser);
        dest.writeString(this.jenkel);
        dest.writeString(this.idLapor);
        dest.writeString(this.alamat);
    }

    public LapPeruserItem() {
    }

    protected LapPeruserItem(Parcel in) {
        this.nik = in.readString();
        this.total = in.readString();
        this.nama = in.readString();
        this.foto = in.readString();
        this.tmp = in.readString();
        this.tgl = in.readString();
        this.idUser = in.readString();
        this.jenkel = in.readString();
        this.idLapor = in.readString();
        this.alamat = in.readString();
    }

    public static final Parcelable.Creator<LapPeruserItem> CREATOR = new Parcelable.Creator<LapPeruserItem>() {
        @Override
        public LapPeruserItem createFromParcel(Parcel source) {
            return new LapPeruserItem(source);
        }

        @Override
        public LapPeruserItem[] newArray(int size) {
            return new LapPeruserItem[size];
        }
    };
}