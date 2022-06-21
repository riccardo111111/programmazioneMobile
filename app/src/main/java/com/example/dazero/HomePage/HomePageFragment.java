package com.example.dazero.HomePage;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dazero.MainActivity2;
import com.example.dazero.R;
import com.example.dazero.adapters.ItemViewModel;
import com.example.dazero.ml.ModelTFLITE;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void dispatchTakePictureIntent() {
        Intent intent=new Intent(getActivity(), MainActivity2.class);
        getActivity().startActivity(intent);
  /*
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
        } else {
            //Request camera permission if we don't have it.
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }

 */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(bitmap.getWidth(),bitmap.getHeight());
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,dimension,dimension);
            imageView.setImageBitmap(bitmap);
            bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);
            classifyImage(bitmap);
/*
            Intent intent=new Intent(HomePageFragment.this, Elaborazione.class);
            intent.putExtra("resId", bitmap);
            startActivity(intent);
 */

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void classifyImage(Bitmap bitmap) {
        try {
            ModelTFLITE model = ModelTFLITE.newInstance(getContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocate(4 * imageSize * imageSize* 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize *imageSize];

            bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            int pixel =0;
            for (int i=0 ; i<imageSize; i++){
                for (int j=0; j<imageSize; j++){
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val & 0xFF)) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            ModelTFLITE.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidences = 0;
            int second=0;
            int third =0;
            for (int i=0; i<confidences.length; i++){
                if (confidences[i] > maxConfidences){
                    maxConfidences = confidences[i];

                    third= second;
                    second =maxPos;
                    maxPos = i;
                }
            }
            String[] classes = {"Apple___Apple_scab", "Apple___Black_rot", "Apple___Cedar_apple_rust",
                    "Apple___healthy", "Blueberry___healthy", "Cherry_(including_sour)___Powdery_mildew",
                    "Cherry_(including_sour)___healthy", "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot",
                    "Corn_(maize)___Common_rust_", "Corn_(maize)___Northern_Leaf_Blight", "Corn_(maize)___healthy",
                    "Grape___Black_rot", "Grape___Esca_(Black_Measles)", "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)",
                    "Grape___healthy", "Orange___Haunglongbing_(Citrus_greening)", "Peach___Bacterial_spot",
                    "Peach___healthy", "Pepper,_bell___Bacterial_spot", "Pepper,_bell___healthy",
                    "Potato___Early_blight", "Potato___Late_blight", "Potato___healthy",
                    "Raspberry___healthy", "Soybean___healthy", "Squash___Powdery_mildew",
                    "Strawberry___Leaf_scorch", "Strawberry___healthy", "Tomato___Bacterial_spot",
                    "Tomato___Early_blight", "Tomato___Late_blight", "Tomato___Leaf_Mold",
                    "Tomato___Septoria_leaf_spot", "Tomato___Spider_mites Two-spotted_spider_mite",
                    "Tomato___Target_Spot", "Tomato___Tomato_Yellow_Leaf_Curl_Virus",
                    "Tomato___Tomato_mosaic_virus", "Tomato___healthy"};

            //result.setText(classes[maxPos]);

            String s = "";
            int [] lista={maxPos, second, third};

            for (int i=0; i<3; i++){
                s+= String.format("%s: %.1f%%\n", classes[lista[i]], confidences[lista[i]] * 100);
            }

            //confidence.setText(s);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}

