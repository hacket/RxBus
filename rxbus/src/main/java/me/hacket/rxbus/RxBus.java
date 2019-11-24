package me.hacket.rxbus;

import com.jakewharton.rxrelay3.PublishRelay;
import com.jakewharton.rxrelay3.Relay;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public final class RxBus<V> {

    private final Relay<BusMessage<V>> mNormalRelay;
    private final Relay<BusMessage<V>> mStickyRelay;

    private final ConcurrentMap<String, BusMessage<V>> mStickyEventMap = new ConcurrentHashMap<>();

    private static final RxBus INSTANCE = new RxBus();

    private RxBus() {
        mNormalRelay = PublishRelay.<BusMessage<V>>create().toSerialized();
        mStickyRelay = PublishRelay.<BusMessage<V>>create().toSerialized();
    }

    public static <T> RxBus<T> getDefault() {
        return INSTANCE;
    }

    public void post(@NonNull String tag, @NonNull V value) {
        post(BusMessage.of(tag, value));
    }

    public void post(@NonNull String tag) {
        post(BusMessage.of(tag));
    }

    private void post(@NonNull BusMessage<V> message) {
        Utils.requireNonNull(message, "BusMessage is null");
        Utils.requireNonNull(message.getTag(), "tag is null");
        Utils.requireNonNull(message.getValue(), "event is null");
        mNormalRelay.accept(message);
    }

    public void postSticky(@NonNull String tag, @NonNull V value) {
        postSticky(BusMessage.of(tag, value));
    }

    public void postSticky(@NonNull String tag) {
        postSticky(BusMessage.of(tag));
    }

    private void postSticky(@NonNull BusMessage<V> message) {
        Utils.requireNonNull(message, "BusMessage is null");
        Utils.requireNonNull(message.getTag(), "tag is null");
        Utils.requireNonNull(message.getValue(), "event is null");
        mStickyEventMap.put(message.getTag(), message);
        mStickyRelay.accept(message);
    }

    public void receive(
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull String tag,
            @NonNull Action1<V> action1) {
        receive(lifecycleOwner, tag, action1, AndroidSchedulers.mainThread());
    }

    public void receive(
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull String tag,
            @NonNull Action1<V> action1,
            @NonNull Scheduler scheduler) {
        Utils.requireNonNull(lifecycleOwner, "lifecycleOwner is null");
        Utils.requireNonNull(scheduler, "scheduler is null");
        Disposable disposable = toObservable(mNormalRelay, tag)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribeWith(new RxBusReceiver<V>(){
                    @Override
                    public void receive(@NonNull V data) {
                        action1.onReceive(data);
                    }
                });
        lifecycleOwner.getLifecycle()
                .addObserver(new BusLifeObserver(tag, disposable));
    }

    private Observable<V> toObservableSticky(
            @NonNull Relay<BusMessage<V>> relay,
            @NonNull final String tag) {
        Observable<V> observable = toObservable(relay, tag);
        final BusMessage<V> busMessage = mStickyEventMap.get(tag);
        if (busMessage != null) {
            return observable
                    .mergeWith(Observable.create(emitter -> {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(busMessage.getValue());
                            emitter.onComplete();
                        }
                    }))
                    .doOnNext(v -> removeStickyEvent(tag));
        } else {
            return observable;
        }
    }

    public void receiveSticky(
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull String tag,
            @NonNull Action1<V> action1) {
        receiveSticky(lifecycleOwner, tag, action1, AndroidSchedulers.mainThread());
    }

    public void receiveSticky(
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull String tag,
            @NonNull Action1<V> action1,
            @NonNull Scheduler scheduler) {
        Utils.requireNonNull(lifecycleOwner, "lifecycleOwner is null");
        Utils.requireNonNull(scheduler, "scheduler is null");
        Disposable disposable = toObservableSticky(mStickyRelay, tag)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribeWith(new RxBusReceiver<V>(){
                    @Override
                    public void receive(@NonNull V data) {
                        action1.onReceive(data);
                    }
                });
        lifecycleOwner.getLifecycle()
                .addObserver(new BusLifeObserver(tag, disposable));
    }

    private Observable<V> toObservable(
            @NonNull Relay<BusMessage<V>> relay,
            final @NonNull String tag) {
        return relay
                .filter(message -> {
                    boolean checkTag = message.checkTag(tag);
                    if (!checkTag) {
                        Utils.logw("filter check tag: " + tag, "RxBus check tag failed. message tag: " + message.getTag() + "，receive tag: " + tag);
                        return false;
                    }
                    boolean checkValue = message.checkValue();
                    if (!checkValue) {
                        Utils.logw("filter check value: " + tag, "RxBus check value failed. : " + message.getTag() + ", value: " + message.getValue());
                        return false;
                    }
                    return true;
                })
                .map(message -> {
                    Utils.logi("map:" + tag, "RxBus map value : " + message.getValue());
                    return message.getValue();
                })
                .doOnSubscribe(disposable -> Utils.logi("doOnSubscribe:" + tag, "RxBus Subscribe: " + disposable.isDisposed()))
                .doOnDispose(() -> Utils.logw("doOnDispose:" + tag, "The BusMessage Dispose"));
    }

    public void removeAllStickyEvents() {
        mStickyEventMap.clear();
    }

    public void removeStickyEvent(@NonNull String tag) {
        Utils.requireNonNull(tag, "tag is null");
        BusMessage<V> remove = mStickyEventMap.remove(tag);
        Utils.logw("removeStickyEvent", "complete remove value by tag: " + tag + "，" + remove);
    }

    private boolean hasObservable() {
        return mNormalRelay.hasObservers();
    }

}
