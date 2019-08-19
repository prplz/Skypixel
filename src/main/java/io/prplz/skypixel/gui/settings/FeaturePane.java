package io.prplz.skypixel.gui.settings;

import io.prplz.skypixel.gui.GuiItem;
import io.prplz.skypixel.gui.Label;
import io.prplz.skypixel.gui.Pane;

import java.util.Arrays;

public class FeaturePane extends Pane {

    public FeaturePane(GuiItem... elements) {
        this.elements = Arrays.asList(elements);
    }

    @Override
    public void setSize(int width, int height) {
        contentHeight = 3;
        for (GuiItem element : elements) {
            element.setPosition(3, contentHeight);
            if (element instanceof Label) {
                element.setWidth(width - 6);
            }
            contentHeight += element.getHeight() + 8;
        }
        super.setSize(width, height);
    }
}
