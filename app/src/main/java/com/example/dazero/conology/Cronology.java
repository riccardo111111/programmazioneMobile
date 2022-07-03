package com.example.dazero.conology;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.R;
import com.example.dazero.adapters.ListAdapter;
import com.example.dazero.db.Result;
import com.example.dazero.services.CronologyService;
import com.example.dazero.services.ResultService;
import com.example.dazero.services.ServiceManagerSingleton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Cronology extends AppCompatActivity {
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    ResultService resultService;
    ListView listView;

    CronologyService cronologyService;
    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronology);

        textInputLayout= (TextInputLayout) findViewById(R.id.menu);
        autoCompleteTextView =(AutoCompleteTextView) findViewById(R.id.items);
        resultService= ServiceManagerSingleton.getInstance(getApplicationContext()).getResultService();
        listView= findViewById(R.id.list_of_chronology_card);
         idUser = ServiceManagerSingleton.getInstance(getApplicationContext()).getUserId();

        cronologyService=new CronologyService(resultService, idUser);

        String [] items ={ "Month","Week","All"};
        ArrayAdapter<String> itemAdapter= new ArrayAdapter<>(getApplicationContext(),R.layout.item,items);
        adatptList(cronologyService.showAllResults());
        autoCompleteTextView.setAdapter(itemAdapter);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            listView.removeAllViewsInLayout();
                showResults((String) parent.getItemAtPosition(position));
        });
    }
    public void showResults(String filter){
        Log.d("inside click","yeaaa "+filter);
            if(filter=="All"){
                if(cronologyService.showAllResults()==null){
                    Toast.makeText(getApplicationContext(), " take some photo first", Toast.LENGTH_LONG).show();
                }else{
                    adatptList(cronologyService.showAllResults());
                }
            }
            if(filter =="Week"){
                if(cronologyService.showResultOfTheWeek()==null){
                    Toast.makeText(getApplicationContext(), " take some photo first", Toast.LENGTH_LONG).show();
                }else{
                    adatptList(cronologyService.showResultOfTheWeek());
                }
            }

            if(filter =="Month"){
                if(cronologyService.showResultOfTheMonth()==null){
                    Toast.makeText(getApplicationContext(), " take some photo first", Toast.LENGTH_LONG).show();
                }else{
                    adatptList(cronologyService.showResultOfTheMonth());
                }
            }
    }

    public void adatptList(ArrayList<Result> array){
        if(array!=null) {
            ListAdapter listAdapter = new ListAdapter(getApplicationContext(), array);
            listView.setAdapter(listAdapter);
        }else{
            Toast.makeText(getApplicationContext(),"elements not present",Toast.LENGTH_LONG).show();
        }
    }


}