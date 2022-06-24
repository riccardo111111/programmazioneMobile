package com.example.dazero.services;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import okhttp3.OkHttpClient;

public class ServiceManagerSingleton {
    private static ServiceManagerSingleton mInstance;
    private RequestQueue mRequestQueue;
    private StrictMode.ThreadPolicy policy;
    private static Context mCtx;
    private OkHttpClient client;

    private ServiceManagerSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();


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

    public OkHttpClient getHttpClient() {
        if (client == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            client = new OkHttpClient();
        }
        return client;
    }

    public StrictMode.ThreadPolicy getSecutityPolicy(){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            if(this.policy==null){
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
