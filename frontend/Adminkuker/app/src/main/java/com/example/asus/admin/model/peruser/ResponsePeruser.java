package com.example.asus.admin.model.peruser;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePeruser implements Parcelable {

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private int code;

	@SerializedName("lap_peruser")
	private List<LapPeruserItem> lapPeruser;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setLapPeruser(List<LapPeruserItem> lapPeruser){
		this.lapPeruser = lapPeruser;
	}

	public List<LapPeruserItem> getLapPeruser(){
		return lapPeruser;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponsePeruser{" + 
			"msg = '" + msg + '\'' + 
			",code = '" + code + '\'' + 
			",lap_peruser = '" + lapPeruser + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.msg);
		dest.writeInt(this.code);
		dest.writeTypedList(this.lapPeruser);
		dest.writeByte(this.status ? (byte) 1 : (byte) 0);
	}

	public ResponsePeruser() {
	}

	protected ResponsePeruser(Parcel in) {
		this.msg = in.readString();
		this.code = in.readInt();
		this.lapPeruser = in.createTypedArrayList(LapPeruserItem.CREATOR);
		this.status = in.readByte() != 0;
	}

	public static final Parcelable.Creator<ResponsePeruser> CREATOR = new Parcelable.Creator<ResponsePeruser>() {
		@Override
		public ResponsePeruser createFromParcel(Parcel source) {
			return new ResponsePeruser(source);
		}

		@Override
		public ResponsePeruser[] newArray(int size) {
			return new ResponsePeruser[size];
		}
	};
}