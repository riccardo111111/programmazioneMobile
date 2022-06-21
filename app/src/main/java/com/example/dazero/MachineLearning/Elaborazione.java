package com.example.dazero.MachineLearning;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.dazero.ml.ModelTFLITE;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Elaborazione extends Service {
    ImageView imageView;
    Button picture;
    int imageSize = 224;

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "entrato", Toast.LENGTH_LONG).show();
        return START_STICKY;

    }


    private void classifyImage(Bitmap bitmap) {
        try {
            ModelTFLITE model = ModelTFLITE.newInstance(getApplicationContext());

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
            int[] lista = {maxPos, second, third};

            for (int i = 0; i < 3; i++) {
                s += String.format("%s: %.1f%%\n", classes[lista[i]], confidences[lista[i]] * 100);
            }

            //confidence.setText(s);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
/*
    Bundle bitmap = getIntent().getExtras();
        if(bitmap !=null)

    {
        int resid = bitmap.getInt("resId");
        imageView.setImageBitmap(resid);
    }

 */





    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
