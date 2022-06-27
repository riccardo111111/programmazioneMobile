package com.example.dazero.services;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.dazero.db.AppDatabase;
import com.example.dazero.db.User;

import okhttp3.OkHttpClient;

public class ServiceManagerSingleton {
    private static ServiceManagerSingleton mInstance;
    private RequestQueue mRequestQueue;
    private StrictMode.ThreadPolicy policy;
    private static Context mCtx;
    private OkHttpClient client;

    public static AppDatabase db;

    private ServiceManagerSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        db = AppDatabase.getDbInstance(context);
    }

    public static synchronized ServiceManagerSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ServiceManagerSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public User saveNewUser(int id, String firstName, String email, String surname, String password) {
        User user = new User();
        user.uid = id;
        user.name = firstName;
        user.surname = surname;
        user.email = email;
        user.password = password;
        if (db.userDao().findProfileById(user.uid) == null) {
            db.userDao().insertUser(user);
        }
        return user;
    }

    public OkHttpClient getHttpClient() {
        if (client == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            client = new OkHttpClient();
        }
        return client;
    }

    public StrictMode.ThreadPolicy getSecutityPolicy() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            if (this.policy == null) {
                this.policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //your codes here
            }

        }
        return this.policy;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
