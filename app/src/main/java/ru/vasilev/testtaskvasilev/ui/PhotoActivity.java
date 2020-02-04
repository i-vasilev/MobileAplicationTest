package ru.vasilev.testtaskvasilev.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import ru.vasilev.testtaskvasilev.R;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        String url = getIntent().getStringExtra(AlbumActivity.PHOTO_IMAGE);
        String title = getIntent().getStringExtra(AlbumActivity.PHOTO_TITLE);
        actionBar.setTitle(title);
        Uri uri = Uri.parse(url);
        ImageView photoView = findViewById(R.id.photoView);
        Picasso.get().load(uri).into(photoView);
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
}
