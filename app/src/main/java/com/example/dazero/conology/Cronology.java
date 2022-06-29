package com.example.dazero.conology;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageSwitcher;
import android.widget.Toast;

import com.example.dazero.R;
import com.example.dazero.db.Result;
import com.example.dazero.db.ResultDao;
import com.example.dazero.services.ResultService;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Cronology extends AppCompatActivity {
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    ResultService resultService;
    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronology);


        textInputLayout=findViewById(R.id.menu);
        autoCompleteTextView = findViewById(R.id.items);
        resultService=new ResultService(getApplicationContext());

        String [] items ={ "Month","Week","All"};
        ArrayAdapter<String> itemAdapter= new ArrayAdapter<>(Cronology.this,R.layout.item,items);
        autoCompleteTextView.setAdapter(itemAdapter);
        autoCompleteTextView.setOnItemClickListener(

                (parent, view, position, id) -> Toast.makeText(getApplicationContext(),(String)parent.getItemAtPosition(position),Toast.LENGTH_LONG).show());
        showResults();
    }

    public void showResults(){
        this.idUser= getIntent().getIntExtra("id",0);
        ArrayList<Result> results= resultService.getResultByID(this.idUser);
        Log.d("results",String.valueOf(results.size()));

    }
}