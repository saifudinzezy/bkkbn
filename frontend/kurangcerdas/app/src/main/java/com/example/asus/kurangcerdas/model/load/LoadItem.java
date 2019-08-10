package com.example.asus.kurangcerdas.model.load;

import com.google.gson.annotations.SerializedName;

public class LoadItem{

	@SerializedName("foto")
	private String foto;

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	@Override
 	public String toString(){
		return 
			"LoadItem{" + 
			"foto = '" + foto + '\'' + 
			"}";
		}
}