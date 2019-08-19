package io.prplz.skypixel.property;

import java.util.function.Supplier;

public class Property<T> implements Supplier<T> {

    private T value;

    public Property(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}