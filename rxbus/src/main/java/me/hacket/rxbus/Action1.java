package me.hacket.rxbus;

import androidx.annotation.NonNull;

@FunctionalInterface
public interface Action1<T> {
    void onReceive(@NonNull T data);
}
