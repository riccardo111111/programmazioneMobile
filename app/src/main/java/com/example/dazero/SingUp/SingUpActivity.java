package com.example.dazero.SingUp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.R;
import com.example.dazero.SingIn.SingInActivity;
import com.example.dazero.databinding.ActivitySignUpBinding;
import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.User;
import com.example.dazero.services.ServiceManagerSingleton;
import com.example.dazero.services.UserServices;

public class SingUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private ProgressDialog dialog;
    private static final int RC_SIGN_IN = 45;
    private boolean is8char = false, hasUpper = false, hasnum = false, hasSpecialSymbol = false, isSignupClickable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        dialog = new ProgressDialog(SingUpActivity.this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("Creating your Account");

        Log.d("Log", "SingUp1");
        inputChanged();

    }

    public void set(View v) {
        Log.i("testooo", binding.editTextEmail.getText().toString());
        if (is8char && hasnum && hasSpecialSymbol && hasUpper) {
            saveNewUser(binding.editTextName.getText().toString(), binding.editTextEmail.getText().toString(),
                    binding.editTextSurname.getText().toString(),
                    binding.editTextPassword.getText().toString());
            Intent intent = new Intent(this, SingInActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "la password non rispetta i requisiti di sicurezza",
                    Toast.LENGTH_LONG).show();
        }

    }


    private void saveNewUser(String firstName, String email, String surname, String password) {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        UserServices userServices = ServiceManagerSingleton.getInstance(getApplicationContext()).getUserServices();

        User user = new User();
        user.name = firstName;
        user.surname = surname;
        user.email = email;
        user.password = password;
        userServices.createUser(user);
        User u=userServices.getUserByMailAndPassword(email, password);
        Log.d("LOG", "bgdeat"+ u);
        db.userDao().insertUser(u);

        finish();
        Intent intent = new Intent(this, SingInActivity.class);
        startActivity(intent);

    }

    @SuppressLint("ResourceType")
    private void passwordValidate() {
        String name = binding.editTextName.getText().toString();
        String surname = binding.editTextSurname.getText().toString();
        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();

        if (name.isEmpty()) {
            binding.editTextName.setError("Please Enter Full name ");
        }
        if (email.isEmpty()) {
            binding.editTextEmail.setError("Please Enter Email ");
        }
        if (surname.isEmpty()) {
            binding.editTextSurname.setError("Please Enter Surname ");
        }
        // 8 character
        if (password.length() >= 8) {
            is8char = true;
            binding.card1.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
            Log.d("Log", "SingUp5");

        } else {
            is8char = false;
            binding.card1.setCardBackgroundColor(Color.parseColor(getString(R.color.colorGrey)));
        }
        //number
        if (password.matches("(.*[0-9].*)")) {
            hasnum = true;
            binding.card2.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasUpper = false;
            binding.card2.setCardBackgroundColor(Color.parseColor(getString(R.color.colorGrey)));
        }
        //upper case
        if (password.matches("(.*[A-Z].*)")) {
            hasUpper = true;
            binding.card3.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasUpper = false;
            binding.card3.setCardBackgroundColor(Color.parseColor(getString(R.color.colorGrey)));
        }
        //symbol
        if (password.matches("^(?=.*[_.()$&@]).*$")) {
            hasSpecialSymbol = true;
            binding.card4.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasSpecialSymbol = false;
            binding.card4.setCardBackgroundColor(Color.parseColor(getString(R.color.colorGrey)));
        }
        Log.d("Log", "SingUp6");
    }

    private void inputChanged() {
        Log.d("Log", "SingUp2");

        binding.editTextPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Log", "SingUp3");

                passwordValidate();
                Log.d("Log", "SingUp4");

                if (is8char && hasnum && hasSpecialSymbol && hasUpper) {
                    binding.signUpButton.setBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}