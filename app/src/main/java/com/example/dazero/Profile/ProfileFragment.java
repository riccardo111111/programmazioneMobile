package com.example.dazero.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dazero.R;
import com.example.dazero.db.User;
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
        this.user=userService.getUserByID(21);

        if(user==null){
            displayFragment(view);
        }else{
            displayFragment(view);
            Toast.makeText(getActivity(),this.user.name,Toast.LENGTH_LONG).show();
            mail.setText(this.user.email);
            password.setText(this.user.password);
        }


        // Inflate the layout for this fragment
    }

    public void displayFragment(View view){
        mail= view.findViewById(R.id.mail);
        password= view.findViewById(R.id.password);
    }

}