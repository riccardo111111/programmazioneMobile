package com.example.dazero.SingUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dazero.SingIn.SingInActivity;
import com.example.dazero.databinding.ActivitySignUpBinding;
import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.User;

public class SingUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private ProgressDialog dialog;
    private static final int RC_SIGN_IN = 45;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());


        dialog = new ProgressDialog(SingUpActivity.this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("Creating your Account");


    }

    public void set(View v){
        Log.i("testooo", binding.editTextEmail.getText().toString());
        if (!"".equals(binding.editTextName.getText().toString())
                && !"".equals(binding.editTextEmail.getText().toString()) &&
                !"".equals(binding.editTextSurname.getText().toString())
                && !"".equals(binding.editTextPassword.getText().toString())){
    saveNewUser(binding.editTextName.getText().toString(), binding.editTextEmail.getText().toString(),
            binding.editTextSurname.getText().toString(),
            binding.editTextPassword.getText().toString());

        }else {
            Toast.makeText(SingUpActivity.this,
                    "Please fill up the fields", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveNewUser(String firstName, String email, String surname, String password) {
        AppDatabase db  = AppDatabase.getDbInstance(this.getApplicationContext());

        User user = new User();
        user.name = firstName;
        user.surname=surname;

        user.email = email;
        user.password=password;
        db.userDao().insertUser(user);

        finish();
        Intent intent=new Intent(this, SingInActivity.class);
        startActivity(intent);

    }
}