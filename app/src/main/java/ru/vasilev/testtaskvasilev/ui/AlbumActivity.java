package ru.vasilev.testtaskvasilev.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vasilev.testtaskvasilev.R;
import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.data.IO.IOType;
import ru.vasilev.testtaskvasilev.data.IO.db.DBHelper;
import ru.vasilev.testtaskvasilev.data.IO.network.APIService;
import ru.vasilev.testtaskvasilev.data.IO.network.ApiServiceFactory;
import ru.vasilev.testtaskvasilev.data.Photo;
import ru.vasilev.testtaskvasilev.data.adapters.PhotosRecyclerViewAdapter;

import static ru.vasilev.testtaskvasilev.ui.main.AlbumsFragment.IO_TYPE_NAME;

public class AlbumActivity extends AppCompatActivity implements PhotosRecyclerViewAdapter.OnClickPhotoListener {

    private RecyclerView recyclerView;
    public final static String PHOTO_IMAGE = "photo-image";
    private PhotosRecyclerViewAdapter.OnClickPhotoListener listener;
    private Album album;
    List<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        listener = this;
        IOType ioType = IOType.valueOf(getIntent().getExtras().getString(IO_TYPE_NAME));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        View view = findViewById(R.id.list);
        album = getIntent().getParcelableExtra(MainActivity.album);
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            switch (ioType) {
                case Network:
                    ApiServiceFactory apiServiceFactory = new ApiServiceFactory();
                    APIService apiService = apiServiceFactory.Read();
                    Call<List<Photo>> listCallPhotos = apiService.loadPhotos(album.getId());
                    listCallPhotos.enqueue(new Callback<List<Photo>>() {
                        @Override
                        public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                            if (response.isSuccessful()) {
                                photos = response.body();
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
                    break;
                case DB:
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    photos = dbHelper.selectPhotos(album.getId());

                    PhotosRecyclerViewAdapter adapterPhotos =
                            new PhotosRecyclerViewAdapter(photos, listener);
                    recyclerView.setAdapter(adapterPhotos);
                    adapterPhotos.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.addToFavourite:
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                dbHelper.insertOrReplaceAlbum(album, photos);
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
