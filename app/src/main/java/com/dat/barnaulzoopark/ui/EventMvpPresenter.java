package com.dat.barnaulzoopark.ui;

import android.support.annotation.Nullable;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAT on 1/7/2017.
 */

public class EventMvpPresenter<V extends MvpView> extends MvpBasePresenter<V> {

    protected EventBus eventBus;

    public EventMvpPresenter(@Nullable EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        if (eventBus != null) {
            eventBus.register(this);
        }
    }

    @Override
    public void detachView(boolean retainInstance) {
        if (eventBus != null) {
            eventBus.unregister(this);
        }
        super.detachView(retainInstance);
    }
}