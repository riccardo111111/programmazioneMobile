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
import com.example.dazero.services.ServiceManagerSingleton;
import com.example.dazero.services.UserServices;


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
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());


        /**
         * Sign In With Email and Password
         */
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(SingInActivity.this, "Please fill up the fields",
                            Toast.LENGTH_SHORT).show();
                } else {
                    UserServices userServices = ServiceManagerSingleton.getInstance(getApplicationContext()).getUserServices();
                    User user = userServices.getUserByMailAndPassword(binding.editTextEmail.getText().toString(),
                            binding.editTextPassword.getText().toString());
                    Log.d("Log", "user: "+ user);
                    if (user == null) {
                        user = db.userDao().findProfile(binding.editTextEmail.getText().toString(),
                                binding.editTextPassword.getText().toString());
                        if (user == null) {
                            dialog.dismiss();
                            Toast.makeText(SingInActivity.this, "account insesistente",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            userServices.createUser(user);
                        }
                    }
                    if (user != null) {
                        if (db.userDao().findProfileById(user.uid) == null) {
                            db.userDao().insertUser(user);
                        }
                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), Tabs.class);
                        i.putExtra("id", String.valueOf(user.uid));
                        startActivity(i);
                        finish();
                    }
                }
            }
    });
        binding.registerNow.setOnClickListener(new View.OnClickListener()

    {
        public void onClick (View v){
        Intent i = new Intent(SingInActivity.this, SingUpActivity.class);
        startActivity(i);
        finish();
    }
    });
}
}