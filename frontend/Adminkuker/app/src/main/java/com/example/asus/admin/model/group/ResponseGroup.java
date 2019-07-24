package com.example.asus.admin.model.group;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGroup{

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private int code;

	@SerializedName("chat")
	private List<ChatItem> chat;

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

	public void setChat(List<ChatItem> chat){
		this.chat = chat;
	}

	public List<ChatItem> getChat(){
		return chat;
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
			"ResponseGroup{" + 
			"msg = '" + msg + '\'' + 
			",code = '" + code + '\'' + 
			",chat = '" + chat + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}