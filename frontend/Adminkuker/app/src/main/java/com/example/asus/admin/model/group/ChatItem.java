package com.example.asus.admin.model.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChatItem implements Parcelable {

	@SerializedName("id_chat")
	private String idChat;

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("total")
	private String total;

	@SerializedName("nama")
	private String nama;

	@SerializedName("id_admin")
	private String idAdmin;

	@SerializedName("waktu")
	private String waktu;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("jenkel")
	private String jenkel;

	public String getJenkel() {
		return jenkel;
	}

	public void setJenkel(String jenkel) {
		this.jenkel = jenkel;
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

	@Override
 	public String toString(){
		return 
			"ChatItem{" + 
			"id_chat = '" + idChat + '\'' + 
			",pesan = '" + pesan + '\'' + 
			",total = '" + total + '\'' + 
			",nama = '" + nama + '\'' + 
			",id_admin = '" + idAdmin + '\'' + 
			",waktu = '" + waktu + '\'' + 
			",id_user = '" + idUser + '\'' + 
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
		dest.writeString(this.total);
		dest.writeString(this.nama);
		dest.writeString(this.idAdmin);
		dest.writeString(this.waktu);
		dest.writeString(this.idUser);
		dest.writeString(this.jenkel);
	}

	public ChatItem() {
	}

	protected ChatItem(Parcel in) {
		this.idChat = in.readString();
		this.pesan = in.readString();
		this.total = in.readString();
		this.nama = in.readString();
		this.idAdmin = in.readString();
		this.waktu = in.readString();
		this.idUser = in.readString();
		this.jenkel = in.readString();
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