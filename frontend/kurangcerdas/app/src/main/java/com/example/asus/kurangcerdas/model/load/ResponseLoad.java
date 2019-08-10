package com.example.asus.kurangcerdas.model.load;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseLoad{

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private int code;

	@SerializedName("load")
	private List<LoadItem> load;

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

	public void setLoad(List<LoadItem> load){
		this.load = load;
	}

	public List<LoadItem> getLoad(){
		return load;
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
			"ResponseLoad{" + 
			"msg = '" + msg + '\'' + 
			",code = '" + code + '\'' + 
			",load = '" + load + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}