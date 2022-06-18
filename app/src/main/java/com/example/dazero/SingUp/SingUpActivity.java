package com.example.dazero.SingUp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.databinding.ActivitySingUpBinding;

public class SingUpActivity extends AppCompatActivity {

    private ActivitySingUpBinding binding;
    private ProgressDialog dialog;
    private static final int RC_SIGN_IN = 45;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());


        dialog = new ProgressDialog(SingUpActivity.this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("Creating your Account");

    }
}