package com.example.asus.kurangcerdas.helper;

import android.content.Context;
import android.content.Intent;

public class Buka {
    private static Intent intent;

    public static void buka(Context context, Class<?> link){
        intent =  new Intent(context.getApplicationContext(), link);
        context.startActivity(intent);
    }
}