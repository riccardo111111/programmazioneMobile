package com.example.dazero.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.dazero.conology.Cronology;


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
            startActivityForResult(i, 3);
        });

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
/*
            Log.d("chiama", " chiamaaaa1");
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
 */
            Intent intent = new Intent(getContext(), MainActivity2.class);
            intent.putExtra("foto", uri.toString());
            startActivity(intent);
        }
    }

    public byte[] BitMapToString(Bitmap image) {

        if (image == null) {
            Log.d("image", "nulllll");
        } else {
            Log.d("image", "ci sta");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return b;
    }


    private void dispatchTakePictureIntent() {
        Intent intent=new Intent(getActivity(), MainActivity2.class);

        int id = ServiceManagerSingleton.getInstance(getContext()).getUserId();
        Log.d("maioa", "piu"+ServiceManagerSingleton.getInstance(getContext()).getUserId());
        intent.putExtra("id", id);
        startActivity(intent);
    }

}

