package ru.vasilev.testtaskvasilev.mvp.views;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import moxy.viewstate.strategy.alias.Skip;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface GpsLocationView extends MvpView {
    void startGPS();

    void stopGPS();

    @Skip
    void setMusic(boolean isStarted);
}
