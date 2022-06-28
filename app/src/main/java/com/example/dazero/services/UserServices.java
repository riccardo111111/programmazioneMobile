package com.example.dazero.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.dazero.db.User;

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


public class UserServices extends Service {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client;
    String host = "10.0.2.2";
    //String host = "10.0.2.2";
    String port = "8080";
    String getAllUsers = "http://" + host + ":" + port + "/serverMobile/rest/users/all";
    String getUserById = "http://" + host + ":" + port + "/serverMobile/rest/users/";
    String createUser = "http://" + host + ":" + port + "/serverMobile/rest/users/create";
    String getUserByMail = "http://" + host + ":" + port + "/serverMobile/rest/users/mail/";
    String deleteUserById = "http://" + host + ":" + port + "/serverMobile/rest/users/delete/";

    Context context;

    public UserServices(Context context) {
        this.context = context;
        ServiceManagerSingleton.getInstance(context).getSecutityPolicy();
        client = ServiceManagerSingleton.getInstance(context).getHttpClient();
    }

    public ArrayList<User> getGetAllUsers() {
        // Instantiate the RequestQueue.
        String result = "ciao";

        try {
            result = run(getAllUsers);

            return convertResultToUserList(result);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public User getUserByID(int id) {
        String result = "ciao";

        try {
            result = run(getUserById + id);
            Log.d("userbyId", result);

            if (convertResultToUserList(result).size() == 0)
                return null;
            else
                return convertResultToUserList(result).get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public User getUserByMailAndPassword(String mail, String password) {
        String result = "ciao";

        try {
            result = run(getUserById + mail + "/" + password);
            Log.d("userbyId", result);

            if (convertResultToUserList(result).size() == 0)
                return null;
            else
                return convertResultToUserList(result).get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteUserByID(int id) {

        try {
            Log.d("delete", createDelete(deleteUserById + id));
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


    public User getUserByMail(String mail) {
        String result = "ciao";

        try {
            result = run(getUserByMail + mail);
            Log.d("userbyId", result);
            return convertResultToUserList(result).get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createUser(String name, String surname, String password, String mail) {
        OkHttpClient client = new OkHttpClient();


        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("id", 0);
            jsonUser.put("name", name);
            jsonUser.put("surname", surname);
            jsonUser.put("password", password);
            jsonUser.put("mail", mail);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody formBody = RequestBody.create(jsonUser.toString(), JSON);

        Request request = new Request.Builder()
                .url(createUser)
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

    public User convertJsonObjectToUser(JSONObject obj) throws JSONException {
        int id = Integer.parseInt(obj.getString("id"));
        String name = obj.getString("name");
        String mail = obj.getString("mail");
        String surname = obj.getString("surname");
        String password = obj.getString("password");
        Log.d("object user", obj.toString());
        return ServiceManagerSingleton.getInstance(this.context).
                saveNewUser(id, name, surname, mail, password);
    }

    public ArrayList<User> convertResultToUserList(String result) {
        ArrayList<User> userList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                userList.add(convertJsonObjectToUser(jsonArray.getJSONObject(i)));
                Log.d("parse json", i + " " + jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userList;
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