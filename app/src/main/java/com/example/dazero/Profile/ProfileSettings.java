package com.example.dazero.Profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.databinding.ActivityProfileSettingsBinding;
import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.User;
import com.example.dazero.services.ServiceManagerSingleton;
import com.example.dazero.services.UserServices;


public class ProfileSettings extends AppCompatActivity {
    private ActivityProfileSettingsBinding binding;

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
        binding.editTextPassword.setText(user.password);
        binding.editTextEmail.setText(user.email);
        UserServices userServices = new UserServices(getApplicationContext());


        binding.signUpButton.setOnClickListener(
                v -> {
                    userServices.updateUser(user);
                    Toast.makeText(userServices, "update User", Toast.LENGTH_SHORT).show();
                });
    }
}