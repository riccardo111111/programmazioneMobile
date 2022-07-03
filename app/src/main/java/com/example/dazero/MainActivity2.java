package com.example.dazero;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.Result;
import com.example.dazero.services.BitmapConverter;
import com.example.dazero.services.MachineLearning.Elaborazione;
import com.example.dazero.services.ResultService;
import com.example.dazero.services.ServiceManagerSingleton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class MainActivity2 extends AppCompatActivity {
    TextView result, confidence;
    ImageView imageView;
    Button picture, save;
    int imageSize = 224;
    String r;
    int id;
    Bitmap image;
    int[] risultato;
    float[] accurateza;
    Elaborazione elaborazione;

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

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        elaborazione = new Elaborazione(getApplicationContext());
        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.image_view);
        picture = findViewById(R.id.take_picture);
        save = findViewById(R.id.save_button);


        if (getIntent().getIntExtra("option", 0) == 0) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity2.this, "Permit", Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            } else {
                //Request camera permission if we don't have it.
                Toast.makeText(MainActivity2.this, "Not Permit", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        } else if (getIntent().getIntExtra("option", 0) == 1) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "pick"), 3);
        }

        picture.setOnClickListener(view -> {
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
        });

        save.setOnClickListener(v -> {
            new Thread(() -> {
                Log.d("p", "gherhea");
                ResultService resultService = ServiceManagerSingleton.getInstance(getApplicationContext()).getResultService();
                id = ServiceManagerSingleton.getInstance(getApplicationContext()).getUserId();
                Log.d("p", "gherhea");

                BitmapConverter bitmap = new BitmapConverter(elaborazione.getImage());
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new java.util.Date());
                Log.d("string", bitmap.BitMapToString());
                Result result = new Result(0,
                        id,
                        bitmap.BitMapToString(),
                        r,
                        timeStamp,
                        null);
                Log.d("p", "result:  " + result);

                //db.resultDao().insertResult(result);
                resultService.createResult(result);
                Log.d("p", "gherhea2");
            }).start();
            Log.d("p", "gherhea3");

            Intent intent = new Intent(MainActivity2.this, Tabs.class);
            intent.putExtra("id", String.valueOf(id));
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");
            elaborazione.elaborazione(image);
        } else if (requestCode == 3) {
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(data.getData());
                image = BitmapFactory.decodeStream(inputStream);
                elaborazione.elaborazione(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        viewResult();
    }

    public void viewResult() {
        this.imageView.setImageBitmap(elaborazione.getImage());

        accurateza = elaborazione.getAccurateza();
        risultato = elaborazione.getRisultato();
        result.setText(classes[risultato[0]]);
        String s = "";

        for (int i = 0; i < 3; i++) {
            s += String.format("%s: %.1f%%\n", classes[risultato[i]], accurateza[i]);
            this.r += (classes[risultato[i]] + "," + accurateza[i] + ";");
        }
        confidence.setText(s);

    }
}