package io.prplz.skypixel.gui;

import io.prplz.skypixel.Translation;
import io.prplz.skypixel.property.Property;

public class BooleanPropertyButton extends Button {

    private final Property<Boolean> property;
    private final Translation translation;

    public BooleanPropertyButton(Property<Boolean> property, Translation translation, int width) {
        super("", width);
        this.property = property;
        this.translation = translation;
        text = makeText(property.get());
    }

    private String makeText(boolean value) {
        return translation.format() + ": " + (value ? Translation.SETTING_ON : Translation.SETTING_OFF).format();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean value = !property.get();
        property.set(value);
        text = makeText(value);
    }
}
