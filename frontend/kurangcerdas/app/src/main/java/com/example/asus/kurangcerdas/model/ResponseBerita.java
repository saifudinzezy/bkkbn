package com.example.asus.kurangcerdas.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBerita{

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("berita")
	private List<BeritaItem> berita;

	@SerializedName("sukses")
	private String sukses;

	@SerializedName("count")
	private int count;

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}

	public void setBerita(List<BeritaItem> berita){
		this.berita = berita;
	}

	public List<BeritaItem> getBerita(){
		return berita;
	}

	public void setSukses(String sukses){
		this.sukses = sukses;
	}

	public String getSukses(){
		return sukses;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	@Override
 	public String toString(){
		return 
			"ResponseBerita{" + 
			"pesan = '" + pesan + '\'' + 
			",berita = '" + berita + '\'' + 
			",sukses = '" + sukses + '\'' + 
			",count = '" + count + '\'' + 
			"}";
		}
}