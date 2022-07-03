package com.example.dazero.detailedView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.R;



public class DetailedView extends AppCompatActivity {

    Button plantType;
    Button plantResults;
    ImageView plantImage;
    TextView accuracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        instantiateElements();
        placeData();
        activateButtons();


    }

    public void placeData(){
        Intent i = getIntent();

        String [] parts=i.getStringExtra("labels").split(",");
        String [] numbers = i.getStringExtra("labels").split(";");

        if(checkInvalidData(i.getStringExtra("labels").split(","))){
           plantType.setText("undefined");
           plantResults.setText("undefined");
            plantImage.setImageBitmap((Bitmap) i.getParcelableExtra("image"));
            accuracy.setText("0.0%");
        }else{
            plantType.setText(parts[0].replace("_", " ").substring(5,parts[0].length()));
            plantResults.setText(parts[1].replaceAll("[^A-z]", " ").replace("_"," ")
                    + "\n accuracy: "+numbers[1].replaceAll("[^0-9]", "").substring(0,2)+"% \n"
                    +parts[2].replaceAll("[^A-z]", " ").replace("_"," ")
                    + "\n accuracy: "+numbers[2].replaceAll("[^0-9]", "").substring(0,2)+"%");
            plantImage.setImageBitmap((Bitmap) i.getParcelableExtra("image"));
            accuracy.setText(numbers[0].replaceAll("[^0-9]", "").substring(0,2)+"%");
        }



    }

    public boolean checkInvalidData(String [] text){

        for (String part:text) {

            if(part.contains("NaN")){
                Log.d("datail",part);
                return true;
            }

        }
        return false;
    }




    public void instantiateElements(){
         plantType = findViewById(R.id.plant_type);
         plantResults = findViewById(R.id.sickness);
         plantImage = (ImageView) findViewById(R.id.image);
         accuracy = findViewById(R.id.accuracy);


    }

    public void activateButtons(){
        plantType.setOnClickListener(v -> {
            String plantTypeText = (String) plantType.getText();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?&q=" + plantTypeText)));
        });

        plantResults.setOnClickListener(v -> {
            String plantSicknessText = (String) plantResults.getText();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?&q=" + plantSicknessText)));
        });
    }
}