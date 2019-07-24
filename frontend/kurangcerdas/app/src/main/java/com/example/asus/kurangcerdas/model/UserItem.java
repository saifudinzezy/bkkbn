package com.example.asus.kurangcerdas.model;

import com.google.gson.annotations.SerializedName;


public class UserItem{

	@SerializedName("id")
	private String id;

	@SerializedName("nik")
	private String nik;

	@SerializedName("desa")
	private String desa;

	@SerializedName("tangga_lahirl")
	private String tanggalLahir;

	@SerializedName("rt")
	private String rt;

	@SerializedName("password")
	private String password;

	@SerializedName("nama")
	private String nama;

	@SerializedName("tempat_lahir")
	private String tempatLahir;

	@SerializedName("kec")
	private String kec;

	@SerializedName("agama")
	private String agama;

	@SerializedName("jenkel")
	private String jenkel;

	@SerializedName("kabupaten")
	private String kabupaten;

	@SerializedName("alamat")
	private String alamat;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNik(String nik){
		this.nik = nik;
	}

	public String getNik(){
		return nik;
	}

	public void setDesa(String desa){
		this.desa = desa;
	}

	public String getDesa(){
		return desa;
	}

	public void setTanggalLahir(String tanggaLahirl){
		this.tanggalLahir = tanggalLahir;
	}

	public String getTanggalLahir(){
		return tanggalLahir;
	}

	public void setRt(String rt){
		this.rt = rt;
	}

	public String getRt(){
		return rt;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setTempatLahir(String tempatLahir){
		this.tempatLahir = tempatLahir;
	}

	public String getTempatLahir(){
		return tempatLahir;
	}

	public void setKec(String kec){
		this.kec = kec;
	}

	public String getKec(){
		return kec;
	}

	public void setAgama(String agama){
		this.agama = agama;
	}

	public String getAgama(){
		return agama;
	}

	public void setJenkel(String jenkel){
		this.jenkel = jenkel;
	}

	public String getJenkel(){
		return jenkel;
	}

	public void setKabupaten(String kabupaten){
		this.kabupaten = kabupaten;
	}

	public String getKabupaten(){
		return kabupaten;
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
			"UserItem{" + 
			"nik = '" + nik + '\'' + 
			",desa = '" + desa + '\'' + 
			",tangga_lahirl = '" + tanggalLahir + '\'' +
			",rt = '" + rt + '\'' + 
			",password = '" + password + '\'' + 
			",nama = '" + nama + '\'' + 
			",tempat_lahir = '" + tempatLahir + '\'' + 
			",kec = '" + kec + '\'' + 
			",agama = '" + agama + '\'' + 
			",jenkel = '" + jenkel + '\'' + 
			",kabupaten = '" + kabupaten + '\'' + 
			",alamat = '" + alamat + '\'' + 
			"}";
		}
}