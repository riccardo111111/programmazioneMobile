package com.example.dazero.conology;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dazero.R;
import com.example.dazero.adapters.ListAdapter;

import com.example.dazero.db.Result;
import com.example.dazero.services.BitmapConverter;
import com.example.dazero.services.ResultService;
import com.google.android.material.textfield.TextInputLayout;
import com.google.type.DateTime;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Cronology extends AppCompatActivity {
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    ResultService resultService;
    ListView listView;
    Button deleteAction ;
    Button viewAction ;

    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronology);


        textInputLayout= (TextInputLayout) findViewById(R.id.menu);
        autoCompleteTextView =(AutoCompleteTextView) findViewById(R.id.items);
        resultService=new ResultService(getApplicationContext());
        listView= findViewById(R.id.list_of_chronology_card);
        deleteAction =findViewById(R.id.delete_action);
        viewAction =findViewById(R.id.view_button);



        String [] items ={ "Month","Week","All"};
        ArrayAdapter<String> itemAdapter= new ArrayAdapter<>(getApplicationContext(),R.layout.item,items);
        autoCompleteTextView.setAdapter(itemAdapter);
        Log.d("inside click","yeaasdasdasdasdaa before pos ");
        adatptList(showResultOfTheWeek());

       

    }



    public void showResults(String filter){

        Log.d("inside click","yeaaa "+filter);
            if(filter=="All"){
                if(showAllResults()==null){
                    Toast.makeText(getApplicationContext(), " take some photo first", Toast.LENGTH_LONG).show();
                }else{
                    adatptList(showAllResults());

                }
            }

            if(filter =="Week"){
                if(showResultOfTheWeek()==null){
                    Toast.makeText(getApplicationContext(), " take some photo first", Toast.LENGTH_LONG).show();
                }else{
                    adatptList(showResultOfTheWeek());

                }
            }

            if(filter =="Month"){
                if(showResultOfTheMonth()==null){
                    Toast.makeText(getApplicationContext(), " take some photo first", Toast.LENGTH_LONG).show();
                }else{
                    adatptList(showResultOfTheMonth());

                }
            }







    }

    public void adatptList(ArrayList<Result> array){
        new Thread(new Runnable() {
            public void run() {
                ListAdapter listAdapter = new ListAdapter(getApplicationContext(),array);
                listView.setAdapter(listAdapter);
        }}).start();

    }

    public ArrayList<Result> showAllResults(){
        idUser= getIntent().getIntExtra("id",0);

        if(resultService.getResultByID(idUser)==null){
            return null;
        }

        return  resultService.getResultByID(idUser);
    }

    public ArrayList<Result> showResultOfTheMonth(){

        idUser= getIntent().getIntExtra("id",0);
        ArrayList<Result> array = resultService.getResultByID(idUser);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();

        if(array==null){
            return null;
        }else{
            for (int i=0;i<array.size();i++){



                try {
                    if (!isWithinRange(
                            new SimpleDateFormat("yyyy-MM-dd").parse(array.get(i).date),
                            result,
                            new Date())
                            ){
                        array.remove(i);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return  array;
    }

    public ArrayList<Result> showResultOfTheWeek(){
        idUser= getIntent().getIntExtra("id",0);
        ArrayList<Result> array = resultService.getResultByID(idUser);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, -1);
        Date result = cal.getTime();

        if(array==null){
            return null;
        }else{
            for (int i=0;i<array.size();i++){

                try {
                    if (!isWithinRange(
                            new SimpleDateFormat("yyyy-MM-dd").parse(array.get(i).date),
                            result,
                            new Date())
                    ){
                        array.remove(i);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return  array;
    }

    boolean isWithinRange(Date testDate,Date startDate,Date endDate) {
        return !(testDate.before(startDate) || testDate.after(endDate));
    }

    public void manageButtons(int i ,ArrayList<Result> array){
        int finalI = i;
        deleteAction.setOnClickListener(v ->
                resultService.deleteResultByID(array.get(finalI).idResult));
        viewAction.setOnClickListener(v ->{}
                //resultService.deleteResultByID(array.get(finalI).idResult
        );
    }
}