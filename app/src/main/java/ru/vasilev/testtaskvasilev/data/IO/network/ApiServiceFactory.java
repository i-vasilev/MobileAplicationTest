package ru.vasilev.testtaskvasilev.data.IO.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceFactory {
    public APIService Read() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        final APIService apiService = retrofit.create(APIService.class);
        return apiService;
    }

}
