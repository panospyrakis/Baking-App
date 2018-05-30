package com.udacity.spyrakis.bakingapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.services.BakingAppApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pspyrakis on 30/5/18.
 */
public class BaseActivity extends AppCompatActivity {

    BakingAppApiService service;
    boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActive = true;
    }
    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    public void onResume(){
        super.onResume();
        isActive = true;
    }
    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    public void setUpNetworkCalls() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getApplicationContext().getString(R.string.base_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(BakingAppApiService.class);
    }
}
