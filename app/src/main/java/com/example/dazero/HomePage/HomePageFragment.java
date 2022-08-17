package com.example.dazero.HomePage;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dazero.MainActivity2;
import com.example.dazero.R;
import com.example.dazero.adapters.ItemViewModel;
import com.example.dazero.conology.Cronology;
import com.example.dazero.services.ServiceManagerSingleton;


public class HomePageFragment extends Fragment {
    private ItemViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        Button takePhotoButton = (Button) view.findViewById(R.id.takePhoto);
        takePhotoButton.setOnClickListener(v ->{
            dispatchTakePictureIntent();
        });

        /*
        Button searchPhotoButton = (Button) view.findViewById(R.id.searchPhoto);
        searchPhotoButton.setOnClickListener(v ->{
           dispatchGalleryIntent();
        });
*/
        Button cronologyButton = (Button) view.findViewById(R.id.chronology);
        cronologyButton.setOnClickListener(v ->{
            Intent i = new Intent(getContext(), Cronology.class);
            int id = ServiceManagerSingleton.getInstance(getContext()).getUserId();

            i.putExtra("id", id);
            startActivity(i);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("chiama", " chiamaaaa");
        if (requestCode== 3 && resultCode == RESULT_OK && data!= null) {
            Uri uri= data.getData();
            Intent intent = new Intent(getContext(), MainActivity2.class);
            intent.putExtra("foto", uri.toString());
            startActivity(intent);
        }
    }

    public void dispatchTakePictureIntent() {
        Intent intent=new Intent(getActivity(), MainActivity2.class);
        int id = ServiceManagerSingleton.getInstance(getContext()).getUserId();
        Log.d("maioa", "piu"+ServiceManagerSingleton.getInstance(getContext()).getUserId());
        intent.putExtra("id", id);
        intent.putExtra("option",0);
        startActivity(intent);
    }

    public void dispatchGalleryIntent(){
        Intent intent=new Intent(getActivity(), MainActivity2.class);
        int id = ServiceManagerSingleton.getInstance(getContext()).getUserId();
        intent.putExtra("id", id);
        intent.putExtra("option",1);
        startActivity(intent);
    }
}

