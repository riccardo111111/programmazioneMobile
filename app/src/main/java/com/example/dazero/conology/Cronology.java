package com.example.dazero.conology;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.R;
import com.example.dazero.db.Result;
import com.example.dazero.services.ResultService;
import com.google.android.material.textfield.TextInputLayout;
import com.example.dazero.adapters.ListAdapter;

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


    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronology);

        textInputLayout= (TextInputLayout) findViewById(R.id.menu);
        autoCompleteTextView =(AutoCompleteTextView) findViewById(R.id.items);
        resultService=new ResultService(getApplicationContext());
        listView= findViewById(R.id.list_of_chronology_card);


        String [] items ={ "Month","Week","All"};
        ArrayAdapter<String> itemAdapter= new ArrayAdapter<>(getApplicationContext(),R.layout.item,items);
        autoCompleteTextView.setAdapter(itemAdapter);
        adatptList(showAllResults());
        Log.d("inside click","yeaasdasdasdasdaa before pos ");
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            listView.removeAllViewsInLayout();
            showResults((String) parent.getItemAtPosition(position));
        });
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
                ListAdapter listAdapter = new ListAdapter(getApplicationContext(),array);
                listView.setAdapter(listAdapter);
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
}