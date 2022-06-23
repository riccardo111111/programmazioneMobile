package com.example.dazero.SingIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.SingUp.SingUpActivity;
import com.example.dazero.Tabs;
import com.example.dazero.databinding.ActivitySingInBinding;
import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.User;


public class SingInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private ActivitySingInBinding binding;
    private ProgressDialog dialog;
    private static final int RC_SIGN_IN = 45;
    private static final String TAG1 = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getSupportActionBar().hide();
        dialog = new ProgressDialog(SingInActivity.this);
        dialog.setTitle("Sign In");
        dialog.setMessage("SignIn to your Account");


        /**
         * Sign In With Email and Password
         */
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG1, "MyClass.getView() — get item number ");
                dialog.show();
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(SingInActivity.this, "Please fill up the fields",
                            Toast.LENGTH_SHORT).show();
                } else {

                    AppDatabase db = AppDatabase.getDbInstance(SingInActivity.this);

                    User user = db.userDao().findProfile(binding.editTextEmail.getText().toString(),
                            binding.editTextPassword.getText().toString());
                        Log.i("non presente", String.valueOf(user!=null));
                    if (user==null) {
                        dialog.dismiss();
                        Toast.makeText(SingInActivity.this, "account insesistente",
                                Toast.LENGTH_SHORT).show();
                    } else {
                       // Log.i("profilo", user.email);
                        Intent i = new Intent(SingInActivity.this, Tabs.class);
                        i.putExtra("name", user.name);
                        i.putExtra("surname", user.surname);
                        i.putExtra("email", user.email);
                        i.putExtra("password", user.password);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
        binding.registerNow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SingInActivity.this, SingUpActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}