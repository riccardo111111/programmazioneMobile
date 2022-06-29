package com.example.dazero.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Layer;

import com.example.dazero.R;
import com.example.dazero.db.Result;

import java.sql.Blob;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Result> {

    public ListAdapter(@NonNull Context context, ArrayList<Result> results) {
        super(context, R.layout.cronology_card,results);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Result result = getItem(position);

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.cronology_card,parent,false);

        }

        ImageView imageView =convertView.findViewById(R.id.image_card);
        TextView title= convertView.findViewById(R.id.title_card);
        TextView description= convertView.findViewById(R.id.description_card);
        TextView date = convertView.findViewById(R.id.date_card);



        imageView.setImageBitmap(convertStringToBitmap(result.bytes));
        title.setText(result.idResult+"");
        date.setText(result.date);
        description.setText(result.labels);


        return convertView;
    }

    public static Bitmap convertStringToBitmap(byte[] bytes){
        if (bytes != null) {
            bytes= Base64.decode(bytes, Base64.DEFAULT);

            return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
        }
        return null;
    }


}
