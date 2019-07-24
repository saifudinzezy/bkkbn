package com.example.asus.kurangcerdas.shared;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedLogin {
    //key
    public static final String SP_LEY = "Login";

    //key value
    public static final String SP_ID = "id";
    public static final String SP_NIK = "nik";
    public static final String SP_NAMA = "nama";
    public static final String SP_TEMPAT = "tempat_lahir";
    public static final String SP_TANGGAL = "tanggal_lahir";
    public static final String SP_JENKEL = "jenkel";
    public static final String SP_ALAMAT = "alamat";
    public static final String SP_RT = "rt";
    public static final String SP_DESA = "desa";
    public static final String SP_KEC = "kec";
    public static final String SP_KAB = "kabupaten";
    public static final String SP_AGAMA = "agama";
    public static final String SP_PASSWORD = "password";
    public static final String SP_SUDAH_LOGIN = "sudah_login";
    public static final String SP_SUDAH_LOGIN2 = "sudah_login2";

    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    //buat sharefpref dari class lain
    public SharedLogin(Context context) {
        //dengan key sampahku
        sp = context.getSharedPreferences(SP_LEY, Context.MODE_PRIVATE);
        this.context = context;
        spEditor = sp.edit();
    }

    public void saveSharedString(String keySP, String value) {
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSharedBoolean(String keySP, boolean value) {
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public Boolean getSharedSudahLogin() {
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

    public Boolean getSharedSudahLogin2() {
        return sp.getBoolean(SP_SUDAH_LOGIN2, false);
    }

    public String getSharedString(String key) {
        return sp.getString(key, "");
    }

}