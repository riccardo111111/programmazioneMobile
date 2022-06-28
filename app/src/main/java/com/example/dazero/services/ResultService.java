package com.example.dazero.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.dazero.db.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultService extends Service {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client;
    String host = "192.168.1.5";
    String port = "8080";
    String getAllResults = "http://" + host + ":" + port + "/serverMobile/rest/results/all";
    String getResultById = "http://" + host + ":" + port + "/serverMobile/rest/results/";
    String createResult = "http://" + host + ":" + port + "/serverMobile/rest/results/create";
    //String getUserByMail = "http://" + host + ":" + port + "/serverMobile/rest/results/mail/";
    String deleteResultById = "http://" + host + ":" + port + "/serverMobile/rest/results/delete/";

    Context context;

    public ResultService(Context context) {
        this.context = context;
        ServiceManagerSingleton.getInstance(context).getSecutityPolicy();
        client = ServiceManagerSingleton.getInstance(context).getHttpClient();
    }

    public ArrayList<Result> getGetAllResults() {
        // Instantiate the RequestQueue.
        String result = "ciao";

        try {
            result = run(getAllResults);

            return convertResultToUserList(result);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<Result> getResultByID(int id) {
        String result = "ciao";

        try {
            result = run(getResultById + id);
            Log.d("userbyId", result);

            if (convertResultToUserList(result).size() == 0)
                return null;
            else
                return convertResultToUserList(result);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    public void deleteResultByID(int id) {

        try {
            Log.d("delete", createDelete(deleteResultById + id));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String createDelete(String url) throws IOException {
        Request request = new Request.Builder().url(url).delete().build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }




    public void createResult(int idResult, int idUser, String date, String bytes, String label, byte[] photo) {
        OkHttpClient client = new OkHttpClient();


        JSONObject jsonResult = new JSONObject();
        try {

            jsonResult.put("id_user", idUser);
            jsonResult.put("date", date);
            jsonResult.put("labels", label);
            jsonResult.put("photo",bytes);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody formBody = RequestBody.create(jsonResult.toString(), JSON);

        Request request = new Request.Builder()
                .url(createResult)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.d("create user ", response.toString());
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Result convertJsonObjectToUser(JSONObject obj) throws JSONException {
        int idResult = Integer.parseInt(obj.getString("idResult"));
        int idUser = Integer.parseInt(obj.getString("idUser"));
        String date = obj.getString("date");
        String bytes = obj.getString("bytes");
        String label = obj.getString("label");

        Log.d("object Resutl", obj.toString());
        return ServiceManagerSingleton.getInstance(this.context).
                saveNewResult(idResult, idUser, date, bytes.getBytes(), label,null);

    }

    public ArrayList<Result> convertResultToUserList(String result) {
        ArrayList<Result> ResultList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                ResultList.add(convertJsonObjectToUser(jsonArray.getJSONObject(i)));
                Log.d("parse json", i + " " + jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ResultList;
    }

    public String run(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}