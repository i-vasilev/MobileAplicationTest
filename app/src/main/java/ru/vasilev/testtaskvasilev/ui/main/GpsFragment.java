package ru.vasilev.testtaskvasilev.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import ru.vasilev.testtaskvasilev.R;
import ru.vasilev.testtaskvasilev.mvp.presenters.GpsLocationPresenter;
import ru.vasilev.testtaskvasilev.mvp.views.GpsLocationView;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class GpsFragment extends MvpAppCompatFragment implements GpsLocationView {

    @InjectPresenter
    public GpsLocationPresenter mGpsLocationPresenter;

    private TextView textView;
    private LocationManager locationManager;
    private MediaPlayer mediaPlayer;
    private Button button;
    private Context mContext;

    public GpsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 5);
                }
            }

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps, container, false);
        locationManager = (LocationManager) inflater.getContext().getSystemService(Context.LOCATION_SERVICE);
        button = view.findViewById(R.id.gps);
        textView = view.findViewById(R.id.infoGps);
        mediaPlayer = MediaPlayer.create(this.getContext(), R.raw.music);
        button.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (inflater.getContext().checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mGpsLocationPresenter.changeStateGeolocation();
                }
            }
        });

        return view;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showLocation(Location location) {
        if (location == null)
            return;
        textView.setText(formatLocation(location));
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(Objects.requireNonNull(mContext).getString(R.string.coordinatesString),
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void startGPS() {
        final FragmentActivity activity = Objects.requireNonNull(this.getActivity());

        int permissionStatus = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1, 1, locationListener);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    10001);
        }
        button.setText(R.string.Stop);
    }

    @Override
    public void stopGPS() {
        locationManager.removeUpdates(locationListener);
        button.setText(R.string.Start);
    }

    @Override
    public void setMusic(boolean isStarted) {
        if (isStarted) {
            mediaPlayer.start();
        } else {
            try {
                mediaPlayer.stop();
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (IOException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
