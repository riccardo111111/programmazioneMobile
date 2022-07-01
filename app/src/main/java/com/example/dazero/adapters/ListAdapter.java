package com.example.dazero.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.dazero.R;

import com.example.dazero.db.Result;
import com.example.dazero.detailedView.DetailedView;
import com.example.dazero.services.ResultService;

import java.sql.Blob;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Result> {
    ResultService resultService;

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
        Button deleteAction =convertView.findViewById(R.id.delete_action) ;
        Button viewAction = convertView.findViewById(R.id.view_button);



        imageView.setImageBitmap(convertStringToBitmap(result.bytes));
        title.setText(result.idResult+"");
        date.setText(result.date);
        description.setText(result.labels);
        deleteAction.setOnClickListener(v -> { resultService.deleteResultByID(result.idResult);
            Toast.makeText(getContext(),"Search canceled id:"+result.idResult,Toast.LENGTH_LONG).show();
        });
        viewAction.setOnClickListener(v -> {
            //resultService= new ResultService(getContext());
            if(result.labels.length()==0)
                result.labels="null";

            Intent i = new Intent(getContext(), DetailedView.class);
            i.putExtra("idResult", result.idResult);
            i.putExtra("labels", result.labels);
            i.putExtra("idUser", result.idUser);
            i.putExtra("date", result.date);
            i.putExtra("image",convertStringToBitmap(result.bytes));
            getContext().startActivity(i);

        });


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
