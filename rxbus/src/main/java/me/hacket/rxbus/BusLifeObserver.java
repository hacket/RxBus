package me.hacket.rxbus;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.rxjava3.disposables.Disposable;

class BusLifeObserver implements DefaultLifecycleObserver {

    private final Disposable disposable;
    private final String type;

    public BusLifeObserver(String type, Disposable disposable) {
        this.type = type;
        this.disposable = disposable;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            Utils.logw("BusObserver onDestroy: " + type, "dispose");
        }
    }

}
