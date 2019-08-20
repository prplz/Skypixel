package io.prplz.skypixel.gui;

import io.prplz.skypixel.property.Property;

import java.util.function.Function;

public class EnumPropertyButton<T extends Enum<T>> extends Button {

    private final Property<T> property;
    private final Class<T> clazz;
    private final Function<T, String> formatter;

    public EnumPropertyButton(Property<T> property, Class<T> clazz, Function<T, String> formatter, int width) {
        super("", width);
        this.property = property;
        this.clazz = clazz;
        this.formatter = formatter;
        text = formatter.apply(property.get());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        T value = property.get();
        T[] values = clazz.getEnumConstants();
        value = values[(value.ordinal() + 1) % values.length];
        property.set(value);
        text = formatter.apply(value);
    }
}
