package com.example.dazero.detailedView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dazero.R;

public class DetailedView extends AppCompatActivity {

    Button plantType;
    Button plantSickness;
    ImageView plantImage;
    TextView accuracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        instantiateElements();
        placeData();
        Log.d(" detailed", " onCreate");


    }

    public void placeData(){
        Intent i = getIntent();
        plantType.setText(i.getStringExtra("labels"));
        plantSickness.setText(i.getStringExtra("labels"));
        accuracy.setText("58.9%");
        Log.d(" detailed", " placeData");

    }

    public void instantiateElements(){
         plantType = findViewById(R.id.plant_type);
         plantSickness = findViewById(R.id.sickness);
        // plantImage = (ImageView) findViewById(R.id.image);
         accuracy = findViewById(R.id.accuracy);
        Log.d(" detailed", " instantiateElements");


    }
}