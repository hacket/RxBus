package me.hacket.rxbus;

import android.text.TextUtils;

import androidx.annotation.NonNull;

final class BusMessage<V> {

    private String tag;
    private V value;

    private static final Object EMPTY = new Object();

    public static <V> BusMessage<V> of(@NonNull String type, @NonNull V msg) {
        return new BusMessage<>(type, msg);
    }

    public static BusMessage of(@NonNull String type) {
        return new BusMessage<>(type, EMPTY);
    }

    private BusMessage(@NonNull String tag, @NonNull V value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean checkTag(@NonNull String tag) {
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        return TextUtils.equals(tag, this.tag);
    }

    public boolean checkValue() {
        return value != null || value == EMPTY;
    }

    @Override
    public String toString() {
        return "BusMessage{" +
                "tag='" + tag + '\'' +
                ", value=" + value +
                '}';
    }

}
