package ru.vasilev.testtaskvasilev.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import ru.vasilev.testtaskvasilev.R;
import ru.vasilev.testtaskvasilev.data.IO.IOType;
import ru.vasilev.testtaskvasilev.ui.main.GpsFragment;
import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.ui.main.AlbumsFragment;
import ru.vasilev.testtaskvasilev.ui.main.SectionsPagerAdapter;

import static ru.vasilev.testtaskvasilev.ui.main.AlbumsFragment.IO_TYPE_NAME;

public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnListFragmentInteractionListener {

    public static final String album = "album";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onListFragmentInteraction(Album item, IOType ioType) {
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putExtra(album, item);
        intent.putExtra(IO_TYPE_NAME, ioType.toString());
        startActivity(intent);
    }
}