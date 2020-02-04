package ru.vasilev.testtaskvasilev.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.vasilev.testtaskvasilev.R;
import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.data.IO.IOType;
import ru.vasilev.testtaskvasilev.data.IO.db.DBHelper;
import ru.vasilev.testtaskvasilev.data.IO.network.APIService;
import ru.vasilev.testtaskvasilev.data.IO.network.RetrofitClient;
import ru.vasilev.testtaskvasilev.data.Photo;
import ru.vasilev.testtaskvasilev.data.adapters.PhotosRecyclerViewAdapter;

import static ru.vasilev.testtaskvasilev.ui.main.AlbumsFragment.IO_TYPE_NAME;

public class AlbumActivity extends AppCompatActivity implements PhotosRecyclerViewAdapter.OnClickPhotoListener {

    private RecyclerView recyclerView;
    public final static String PHOTO_IMAGE = "photo-image";
    public final static String PHOTO_TITLE = "photo-title";
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
        actionBar.setTitle(album.getTitle());
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            switch (ioType) {
                case Network:
                    APIService service = RetrofitClient.getInstance().create(APIService.class);
                    fetchDate(service);
                    break;
                case DB:
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    displayData(dbHelper.selectPhotos(album.getId()));
                    break;
            }
        }
    }

    private void fetchDate(APIService service) {
        if (isInternetAvailable()) {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(service.loadPhotos(album.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::displayData));
        }
    }


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void displayData(List<Photo> photos) {
        this.photos = photos;
        PhotosRecyclerViewAdapter adapterPhotos =
                new PhotosRecyclerViewAdapter(photos, listener);
        recyclerView.setAdapter(adapterPhotos);
    }

    private boolean checkInDB() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        return dbHelper.getAlbumById(album.getId()) != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (checkInDB()) {
            menu.getItem(0).setIcon(android.R.drawable.star_big_on);
        } else {
            menu.getItem(0).setIcon(android.R.drawable.star_big_off);
        }
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
                if (!checkInDB()) {
                    dbHelper.insertOrReplaceAlbum(album, photos);
                    item.setIcon(android.R.drawable.star_big_on);
                } else {
                    dbHelper.removeAlbum(album);
                    item.setIcon(android.R.drawable.star_big_off);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickPhoto(Photo item) {
        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
        intent.putExtra(PHOTO_IMAGE, item.getUrl());
        intent.putExtra(PHOTO_TITLE, item.getTitle());
        startActivity(intent);
    }

}
