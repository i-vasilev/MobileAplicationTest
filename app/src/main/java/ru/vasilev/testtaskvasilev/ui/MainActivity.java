package ru.vasilev.testtaskvasilev.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                final Fragment fragment = sectionsPagerAdapter.fragments.get(position);
                if (fragment instanceof AlbumsFragment) {
                    if (((AlbumsFragment) fragment).getIOType() == IOType.DB)
                        ((AlbumsFragment) fragment).updateList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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