package ru.vasilev.testtaskvasilev;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vasilev.testtaskvasilev.call.APIService;
import ru.vasilev.testtaskvasilev.call.ApiServiceFactory;
import ru.vasilev.testtaskvasilev.data.Photo;

public class AlbumActivity extends AppCompatActivity implements PhotosRecyclerViewAdapter.OnClickPhotoListener {

    private RecyclerView recyclerView;
    public final static String PHOTO_IMAGE = "photo-image";
    private PhotosRecyclerViewAdapter.OnClickPhotoListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        listener = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        View view = findViewById(R.id.list);
        int albumId = getIntent().getIntExtra(MainActivity.ID, -1);
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            ApiServiceFactory apiServiceFactory = new ApiServiceFactory();
            APIService apiService = apiServiceFactory.Read();
            Call<List<Photo>> listCallPhotos = apiService.loadPhotos(albumId);
            listCallPhotos.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    if (response.isSuccessful()) {
                        List<Photo> photos = response.body();
                        PhotosRecyclerViewAdapter adapterPhotos =
                                new PhotosRecyclerViewAdapter(photos, listener);
                        recyclerView.setAdapter(adapterPhotos);
                        adapterPhotos.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickPhoto(Photo item) {
        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
        intent.putExtra(PHOTO_IMAGE, item.getUrl());
        startActivity(intent);
    }

}
