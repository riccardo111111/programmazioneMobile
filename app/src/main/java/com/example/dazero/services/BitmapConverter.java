package com.example.dazero.services;


import android.content.Context;

import android.graphics.Bitmap;
import android.util.Log;


import java.io.ByteArrayOutputStream;

public class BitmapConverter implements Runnable {
     private volatile String imageCoverted;
     private volatile Thread bitmapThread;
     private volatile Bitmap image;
     private volatile Context context;
    public BitmapConverter(Bitmap image) {
        setBitmap(image);
    }


    public void sendPhoto(){

    }
    public String getImageCoverted(){
        return this.imageCoverted;
    }

    public void setContext(Context context){
        this.context=context;
    }

    public void setBitmap(Bitmap image){
        this.image=image;
    }


    public String BitMapToString(){

        if(this.image==null){
            Log.d("image","nulllll");
        }else{
            Log.d("image","ci sta");
        }
        Log.d("tipo ", image.getClass().getSimpleName());

            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            this.image.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();

            String temp = "0x";
            for(int i=0 ; i< b.length;i++){
                temp+=byteToHex(b[i]);
            }

            Log.d("image temp",temp);
       return temp;
    }

    public String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }



    @Override
    public void run() {
    }
}