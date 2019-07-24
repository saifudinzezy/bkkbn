package com.example.asus.admin.model.laporan;

import com.google.gson.annotations.SerializedName;

public class PelaporanItem{

	@SerializedName("alamat_korban")
	private String alamatKorban;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("nm_pelaku")
	private String nmPelaku;

	@SerializedName("gambar")
	private String gambar;

	@SerializedName("jenis_ks")
	private String jenisKs;

	@SerializedName("usia_korb")
	private String usiaKorb;

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("hub_pelap")
	private String hubPelap;

	@SerializedName("nama")
	private String nama;

	@SerializedName("jk_korban")
	private String jkKorban;

	@SerializedName("jk_pelaku")
	private String jkPelaku;

	@SerializedName("kronologi")
	private String kronologi;

	@SerializedName("id_lapor")
	private String idLapor;

	@SerializedName("nama_korban")
	private String namaKorban;

	@SerializedName("hub_pelaku")
	private String hubPelaku;

	@SerializedName("tgl_kej")
	private String tglKej;

	@SerializedName("status")
	private String status;

	public void setAlamatKorban(String alamatKorban){
		this.alamatKorban = alamatKorban;
	}

	public String getAlamatKorban(){
		return alamatKorban;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setNmPelaku(String nmPelaku){
		this.nmPelaku = nmPelaku;
	}

	public String getNmPelaku(){
		return nmPelaku;
	}

	public void setGambar(String gambar){
		this.gambar = gambar;
	}

	public String getGambar(){
		return gambar;
	}

	public void setJenisKs(String jenisKs){
		this.jenisKs = jenisKs;
	}

	public String getJenisKs(){
		return jenisKs;
	}

	public void setUsiaKorb(String usiaKorb){
		this.usiaKorb = usiaKorb;
	}

	public String getUsiaKorb(){
		return usiaKorb;
	}

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}

	public void setHubPelap(String hubPelap){
		this.hubPelap = hubPelap;
	}

	public String getHubPelap(){
		return hubPelap;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setJkKorban(String jkKorban){
		this.jkKorban = jkKorban;
	}

	public String getJkKorban(){
		return jkKorban;
	}

	public void setJkPelaku(String jkPelaku){
		this.jkPelaku = jkPelaku;
	}

	public String getJkPelaku(){
		return jkPelaku;
	}

	public void setKronologi(String kronologi){
		this.kronologi = kronologi;
	}

	public String getKronologi(){
		return kronologi;
	}

	public void setIdLapor(String idLapor){
		this.idLapor = idLapor;
	}

	public String getIdLapor(){
		return idLapor;
	}

	public void setNamaKorban(String namaKorban){
		this.namaKorban = namaKorban;
	}

	public String getNamaKorban(){
		return namaKorban;
	}

	public void setHubPelaku(String hubPelaku){
		this.hubPelaku = hubPelaku;
	}

	public String getHubPelaku(){
		return hubPelaku;
	}

	public void setTglKej(String tglKej){
		this.tglKej = tglKej;
	}

	public String getTglKej(){
		return tglKej;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"PelaporanItem{" + 
			"alamat_korban = '" + alamatKorban + '\'' + 
			",id_user = '" + idUser + '\'' + 
			",nm_pelaku = '" + nmPelaku + '\'' + 
			",gambar = '" + gambar + '\'' + 
			",jenis_ks = '" + jenisKs + '\'' + 
			",usia_korb = '" + usiaKorb + '\'' + 
			",pesan = '" + pesan + '\'' + 
			",hub_pelap = '" + hubPelap + '\'' + 
			",nama = '" + nama + '\'' + 
			",jk_korban = '" + jkKorban + '\'' + 
			",jk_pelaku = '" + jkPelaku + '\'' + 
			",kronologi = '" + kronologi + '\'' + 
			",id_lapor = '" + idLapor + '\'' + 
			",nama_korban = '" + namaKorban + '\'' + 
			",hub_pelaku = '" + hubPelaku + '\'' + 
			",tgl_kej = '" + tglKej + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}