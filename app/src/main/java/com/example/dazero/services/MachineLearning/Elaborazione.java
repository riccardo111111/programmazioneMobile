package com.example.dazero.services.MachineLearning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;

import com.example.dazero.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Elaborazione{
    int imageSize = 224;
    int[] risultato;
    float[] accurateza= new float[3];
    Context applicationContext;
    Bitmap image;

    public Elaborazione(Context applicationContext) {
        this.applicationContext=applicationContext;
    }

    public Bitmap getImage() {
        return image;
    }

    public void elaborazione(Bitmap bitmap) {
        int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
        bitmap = rotateImage(bitmap, 90);
        image=bitmap;

        bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
        classifyImage(bitmap);
    }

    public int[] getRisultato() {
        return risultato;
    }

    public float[] getAccurateza() {
        return accurateza;
    }

    private void classifyImage(Bitmap bitmap) {
        try {
            Model model = Model.newInstance(applicationContext);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocate(4 * imageSize * imageSize * 3);
            //byteBuffer.order(ByteOrder.nativeOrder());

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
            Model.Outputs outputs = model.process(inputFeature0);
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

            risultato=new int[]{maxPos, second, third};

            for (int i = 0; i < 3; i++) {
                accurateza[i] = (confidences[risultato[i]] * 100);
            }
            // Releases model resources if no longer used.
            model.close();

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


    private static Bitmap rotateImageIfRequired(Bitmap bitmap, String selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }
}
