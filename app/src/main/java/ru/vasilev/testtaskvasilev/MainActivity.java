package ru.vasilev.testtaskvasilev;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import ru.vasilev.testtaskvasilev.ui.main.GpsFragment;
import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.ui.main.AlbumsFragment;
import ru.vasilev.testtaskvasilev.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnListFragmentInteractionListener, GpsFragment.OnFragmentInteractionListener {

    public static final String ID = "id";

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
    public void onListFragmentInteraction(Album item) {
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putExtra(ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}