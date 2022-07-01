package com.example.dazero.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dazero.R;
import com.example.dazero.SingIn.SingInActivity;
import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.Result;
import com.example.dazero.db.User;
import com.example.dazero.services.ResultService;
import com.example.dazero.services.ServiceManagerSingleton;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    private User user;
    private TextView mail;
    private TextView password;
    private TextView searches;
    private Button logout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstaceState) {
        String id = getActivity().getIntent().getStringExtra("id");
        // Log.d("profile frag",id);
        //Toast.makeText(getActivity(),,Toast.LENGTH_LONG).show();
        AppDatabase db = ServiceManagerSingleton.getInstance(getContext()).db;
        if (id != null) {
            this.user = db.userDao().findProfileById(Integer.parseInt(id));
        }
        if (user == null) {
            displayFragment(view);
        } else {
            displayFragment(view);
            Toast.makeText(getActivity(), this.user.name, Toast.LENGTH_LONG).show();
            mail.setText(this.user.email);
            password.setText(this.user.password);
        }

        ResultService resultService = new ResultService(getContext());
        ArrayList<Result> results = resultService.getResultByID(Integer.parseInt(id));
        if (results == null) {
            displayFragment(view);
        } else {
            displayFragment(view);
            searches.setText(results.size() + " searches");
        }

        logout.setOnClickListener( l ->{
            db.userDao().delete(this.user);

            Intent i = new Intent(getContext(), SingInActivity.class);
            getContext().startActivity(i);
        });


        // Inflate the layout for this fragment
    }

    public void displayFragment(View view) {
        mail = (TextView) view.findViewById(R.id.mail);
        password = (TextView) view.findViewById(R.id.password);
        searches = (TextView) view.findViewById(R.id.search);
        logout = (Button) view.findViewById(R.id.log_out);
    }



}