package ru.vasilev.testtaskvasilev.mvp.presenters;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.vasilev.testtaskvasilev.mvp.views.GpsLocationView;

@InjectViewState
public class GpsLocationPresenter extends MvpPresenter<GpsLocationView> {
    private boolean isStarted = false;

    public void changeStateGeolocation() {
        if (!isStarted) {
            getViewState().startGPS();
        } else {
            getViewState().stopGPS();
        }
        getViewState().setMusic(!isStarted);
        isStarted = !isStarted;
    }
}
