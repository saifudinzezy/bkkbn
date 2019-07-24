package com.example.asus.admin.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseLaporan{

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("response")
	private String response;

	@SerializedName("pelaporan")
	private List<PelaporanItem> pelaporan;

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setPelaporan(List<PelaporanItem> pelaporan){
		this.pelaporan = pelaporan;
	}

	public List<PelaporanItem> getPelaporan(){
		return pelaporan;
	}

	@Override
 	public String toString(){
		return 
			"ResponseLaporan{" + 
			"pesan = '" + pesan + '\'' + 
			",response = '" + response + '\'' + 
			",pelaporan = '" + pelaporan + '\'' + 
			"}";
		}
}