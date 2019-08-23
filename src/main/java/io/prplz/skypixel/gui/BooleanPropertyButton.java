package io.prplz.skypixel.gui;

import io.prplz.skypixel.Translation;
import io.prplz.skypixel.property.Property;

import java.util.function.Function;

public class BooleanPropertyButton extends Button {

    private final Property<Boolean> property;
    private final Function<String, String> formatter;

    public BooleanPropertyButton(Property<Boolean> property, Function<String, String> formatter, int width) {
        super("", width);
        this.property = property;
        this.formatter = formatter;
        text = formatter.apply(onOffText(property.get()));
    }

    private String onOffText(boolean value) {
        return (value ? Translation.SETTING_ON : Translation.SETTING_OFF).format();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean value = !property.get();
        property.set(value);
        text = formatter.apply(onOffText(value));
    }
}
