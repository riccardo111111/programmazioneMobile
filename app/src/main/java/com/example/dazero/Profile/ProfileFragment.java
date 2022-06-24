package com.example.dazero.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dazero.R;
import com.example.dazero.SingIn.SingInActivity;
import com.example.dazero.models.User;
import com.example.dazero.services.UserServices;


public class ProfileFragment extends Fragment {
    
    private User user;
    private TextView mail;
    private TextView password;
    private TextView searches;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstaceState){
        UserServices userService= new UserServices(getActivity());
        this.user=userService.getUserByID(16);
        mail= view.findViewById(R.id.mail);
        password= view.findViewById(R.id.password);
        Toast.makeText(getActivity(),this.user.getName(),Toast.LENGTH_LONG).show();
        mail.setText(this.user.getMail());
        password.setText(this.user.getPassword());
        // Inflate the layout for this fragment
    }

}