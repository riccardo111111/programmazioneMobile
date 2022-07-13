package com.example.dazero.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.R;
import com.example.dazero.SingIn.SingInActivity;
import com.example.dazero.databinding.ActivityProfileSettingsBinding;
import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.User;
import com.example.dazero.services.ServiceManagerSingleton;
import com.example.dazero.services.UserServices;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;


public class ProfileSettings extends AppCompatActivity {
    private ActivityProfileSettingsBinding binding;
    private boolean is8char = false, hasUpper = false, hasnum = false, hasSpecialSymbol = false, isSignupClickable = false;

    private boolean n = false, s = false, e = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String id = getIntent().getStringExtra("id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        Log.d("log", id);
        AppDatabase db = ServiceManagerSingleton.getInstance(getApplicationContext()).db;
        User user = db.userDao().findProfileById(Integer.parseInt(id));
        binding.editTextName.setText(user.name);
        binding.editTextSurname.setText(user.surname);

        binding.editTextEmail.setText(user.email);
        UserServices userServices = ServiceManagerSingleton.getInstance(getApplicationContext()).getUserServices();

        inputChanged();

        binding.signUpButton.setOnClickListener(
                v -> {
                    if (!(is8char && hasnum && hasSpecialSymbol && hasUpper)) {
                        Toast.makeText(this, "la password non rispetta i requisiti di sicurezza",
                                Toast.LENGTH_LONG).show();
                    } else if (n && s && e) {
                        user.name = binding.editTextName.getText().toString();
                        user.email = binding.editTextEmail.getText().toString();
                        user.surname = binding.editTextSurname.getText().toString();
                        user.password = binding.editTextPassword.getText().toString();
                        user.password= DigestUtils.sha384Hex(user.password);
                        userServices.updateUser(user);
                        db.userDao().updateUser(user);

                        Toast.makeText(ProfileSettings.this, "update User", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d("if", "n: "+ n +" s: " +s+" e: "+ e+ " is8char: "+is8char);
                        Toast.makeText(ProfileSettings.this, "riempire tutti i campi" , Toast.LENGTH_SHORT).show();
                    }
                });

        binding.signUpButton2.setOnClickListener(
                v -> {
                    userServices.deleteUserByID(Integer.parseInt(id));
                    Intent intent = new Intent(ProfileSettings.this, SingInActivity.class);
                    startActivity(intent);
                }
        );
    }

    @SuppressLint("ResourceType")
    private void passwordValidate() {
        String name = binding.editTextName.getText().toString();
        String surname = binding.editTextSurname.getText().toString();
        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();

        if (name.isEmpty()) {
            binding.editTextName.setError("Please Enter Full name ");
            n = false;
        } else {
            n = true;
        }
        if (email.isEmpty()) {
            binding.editTextEmail.setError("Please Enter Email ");
            e = false;
        } else {
            e = true;
        }
        if (surname.isEmpty()) {
            binding.editTextSurname.setError("Please Enter Surname ");
            s = false;
        } else {
            s = true;
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
                passwordValidate();
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