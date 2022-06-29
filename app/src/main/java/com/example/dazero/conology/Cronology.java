package com.example.dazero.conology;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dazero.R;
import com.example.dazero.adapters.ListAdapter;

import com.example.dazero.db.Result;
import com.example.dazero.services.ResultService;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Cronology extends AppCompatActivity {
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    ResultService resultService;
    ListView listView;

    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronology);


        textInputLayout=findViewById(R.id.menu);
        autoCompleteTextView = findViewById(R.id.items);
        resultService=new ResultService(getApplicationContext());
        listView= findViewById(R.id.list_of_chronology_card);
        Button deleteAction =findViewById(R.id.delete_action);
        Button viewAction =findViewById(R.id.view_button);



        String [] items ={ "Month","Week","All"};
        ArrayAdapter<String> itemAdapter= new ArrayAdapter<>(Cronology.this,R.layout.item,items);
        autoCompleteTextView.setAdapter(itemAdapter);
        autoCompleteTextView.setOnItemClickListener(

                (parent, view, position, id) -> Toast.makeText(getApplicationContext(),(String)parent.getItemAtPosition(position),Toast.LENGTH_LONG).show());
        showResults();
    }



    public void showResults(){


                if(showAllResults()==null){
                    Toast.makeText(getApplicationContext(), " take some photo first", Toast.LENGTH_LONG).show();
                }else{
                    ListAdapter listAdapter = new ListAdapter(getApplicationContext(),showAllResults());
                    listView.setAdapter(listAdapter);

                }





    }


    public ArrayList<Result> showAllResults(){
        idUser= getIntent().getIntExtra("id",0);
        return  resultService.getResultByID(idUser);
    }

    public ArrayList<Result> showResultOfTheMonth(){
        idUser= getIntent().getIntExtra("id",0);
        return  resultService.getResultByID(idUser);
    }

    public ArrayList<Result> showResultOfTheWeek(){
        idUser= getIntent().getIntExtra("id",0);
        return  resultService.getResultByID(idUser);
    }
}