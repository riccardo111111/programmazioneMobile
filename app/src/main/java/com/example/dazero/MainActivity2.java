package com.example.dazero;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.ml.ModelTFLITE;
import com.example.dazero.services.ResultService;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity2 extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture, home, save;
    int imageSize = 224;
    Bitmap image;
    int[] lista=new int[3];
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        home = findViewById(R.id.button4);
        save = findViewById(R.id.button5);


        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity2.this, "Permit", Toast.LENGTH_SHORT).show();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
        } else {
            //Request camera permission if we don't have it.
            Toast.makeText(MainActivity2.this, "Not Permit", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
        picture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity2.this, "Permit", Toast.LENGTH_SHORT).show();
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);

                } else {
                    //Request camera permission if we don't have it.
                    Toast.makeText(MainActivity2.this, "Not Permit", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, Tabs.class);
                startActivity(intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultService resultService= new ResultService(getApplicationContext());
                int id = Integer.parseInt(getIntent().getStringExtra("id"));
                resultService.createResult(0, id,  "2022-06-04", image.toString(),
                        this.lista[0],);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());

            bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
            this.image=bitmap;

            imageView.setImageBitmap(bitmap);

            bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);

            classifyImage(bitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void classifyImage(Bitmap bitmap) {
        try {
            ModelTFLITE model = ModelTFLITE.newInstance(MainActivity2.this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocate(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];

            bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
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
            int second = 0;
            int third = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidences) {
                    maxConfidences = confidences[i];

                    third = second;
                    second = maxPos;
                    maxPos = i;
                }
            }


            result.setText(classes[maxPos]);
            String s = "";
            this.lista[0] =maxPos;
            this.lista[1] =second;
            this.lista[2] =third;

            for (int i = 0; i < 3; i++) {
                s += String.format("%s: %.1f%%\n", classes[lista[i]], confidences[lista[i]] * 100);
            }


            confidence.setText(s);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}