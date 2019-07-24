package com.example.asus.kurangcerdas.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChatItem implements Parcelable {

	@SerializedName("id_chat")
	private String idChat;

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("id_admin")
	private String idAdmin;

	@SerializedName("waktu")
	private String waktu;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("nama_user")
	private String namaUser;

	@SerializedName("nama_admin")
	private String namaAdmin;

	@SerializedName("kode")
	private String kode;

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public void setIdChat(String idChat){
		this.idChat = idChat;
	}

	public String getIdChat(){
		return idChat;
	}

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}

	public void setIdAdmin(String idAdmin){
		this.idAdmin = idAdmin;
	}

	public String getIdAdmin(){
		return idAdmin;
	}

	public void setWaktu(String waktu){
		this.waktu = waktu;
	}

	public String getWaktu(){
		return waktu;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setNamaUser(String namaUser){
		this.namaUser = namaUser;
	}

	public String getNamaUser(){
		return namaUser;
	}

	public void setNamaAdmin(String namaAdmin){
		this.namaAdmin = namaAdmin;
	}

	public String getNamaAdmin(){
		return namaAdmin;
	}

	@Override
 	public String toString(){
		return 
			"ChatItem{" + 
			"id_chat = '" + idChat + '\'' + 
			",pesan = '" + pesan + '\'' + 
			",id_admin = '" + idAdmin + '\'' + 
			",waktu = '" + waktu + '\'' + 
			",id_user = '" + idUser + '\'' + 
			",nama_user = '" + namaUser + '\'' + 
			",nama_admin = '" + namaAdmin + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.idChat);
		dest.writeString(this.pesan);
		dest.writeString(this.idAdmin);
		dest.writeString(this.waktu);
		dest.writeString(this.idUser);
		dest.writeString(this.namaUser);
		dest.writeString(this.namaAdmin);
		dest.writeString(this.kode);
	}

	public ChatItem() {
	}

	protected ChatItem(Parcel in) {
		this.idChat = in.readString();
		this.pesan = in.readString();
		this.idAdmin = in.readString();
		this.waktu = in.readString();
		this.idUser = in.readString();
		this.namaUser = in.readString();
		this.namaAdmin = in.readString();
		this.kode = in.readString();
	}

	public static final Parcelable.Creator<ChatItem> CREATOR = new Parcelable.Creator<ChatItem>() {
		@Override
		public ChatItem createFromParcel(Parcel source) {
			return new ChatItem(source);
		}

		@Override
		public ChatItem[] newArray(int size) {
			return new ChatItem[size];
		}
	};
}