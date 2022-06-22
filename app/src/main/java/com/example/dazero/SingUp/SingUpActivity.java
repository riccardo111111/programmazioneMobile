package com.example.dazero.SingUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.SingIn.SingInActivity;
import com.example.dazero.databinding.ActivitySingUpBinding;
import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.User;

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
    public void set(View v){
        Log.i("testooo", binding.editTextEmail.getText().toString());
        if (!"".equals(binding.editTextName.getText().toString())
                && !"".equals(binding.editTextEmail.getText().toString()) &&
                !"".equals(binding.editTextMobile.getText().toString())
                && !"".equals(binding.editTextPassword.getText().toString())){
    saveNewUser(binding.editTextName.getText().toString(), binding.editTextEmail.getText().toString(),
            binding.editTextMobile.getText().toString(),
            binding.editTextPassword.getText().toString());
        }else {
            Toast.makeText(SingUpActivity.this,
                    "Please fill up the fields", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveNewUser(String firstName, String email, String mobilePhone, String password) {
        AppDatabase db  = AppDatabase.getDbInstance(this.getApplicationContext());

        User user = new User();
        user.firstName = firstName;
        user.email = email;
        user.mobileNumber=mobilePhone;
        user.password=password;
        db.userDao().insertUser(user);

        finish();
        Intent intent=new Intent(this, SingInActivity.class);
        startActivity(intent);

    }
}