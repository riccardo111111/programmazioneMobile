package com.example.dazero;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.SingIn.SingInActivity;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home_page);

        Intent i = new Intent(MainActivity.this, SingInActivity.class);
        startActivity(i);
        //finish();

    }


}