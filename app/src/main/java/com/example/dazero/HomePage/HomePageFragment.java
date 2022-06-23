package com.example.dazero.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dazero.MainActivity2;
import com.example.dazero.R;
import com.example.dazero.adapters.ItemViewModel;


public class HomePageFragment extends Fragment {
    TextView result, confidence;
    ImageView imageView;
    Button picture;
    int imageSize = 224;

    private ItemViewModel viewModel;
    final int REQUEST_IMAGE_CAPTURE = 1;

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

        Button searchPhotoButton = (Button) view.findViewById(R.id.searchPhoto);
        searchPhotoButton.setOnClickListener(v ->{
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            final int ACTIVITY_SELECT_IMAGE = 1234;
            startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
        });
    }

    private void dispatchTakePictureIntent() {
        Intent intent=new Intent(getActivity(), MainActivity2.class);
        getActivity().startActivity(intent);
    }

}

