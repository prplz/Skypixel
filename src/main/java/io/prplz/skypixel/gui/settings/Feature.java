package io.prplz.skypixel.gui.settings;

import io.prplz.skypixel.Translation;
import io.prplz.skypixel.gui.Pane;

import java.util.function.Supplier;

public class Feature {

    private final String name;
    private final Pane gui;
    private final Supplier<String> status;

    public Feature(String name, Supplier<String> status, Pane gui) {
        this.name = name;
        this.status = status;
        this.gui = gui;
    }

    public String getName() {
        return name;
    }

    public Pane getGui() {
        return gui;
    }

    public String getStatus() {
        return status.get();
    }

    public static Supplier<String> enabledIf(Supplier<Boolean> condition) {
        return () -> (condition.get() ? Translation.SETTING_ENABLED : Translation.SETTING_DISABLED).format();
    }
}
