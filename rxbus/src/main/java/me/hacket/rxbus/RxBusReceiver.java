package me.hacket.rxbus;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.observers.ResourceObserver;

public abstract class RxBusReceiver<T> extends ResourceObserver<T> {

    @Override
    public final void onNext(T t) {
        try {
            receive(t);
            Utils.logi("RxBusReceiver", "receive : " + t);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logw("RxBusReceiver", "error : " + e.getMessage());
        }
    }

    @Override
    public final void onError(Throwable e) {

    }

    @Override
    public final void onComplete() {

    }

    public abstract void receive(@NonNull T data);

}
