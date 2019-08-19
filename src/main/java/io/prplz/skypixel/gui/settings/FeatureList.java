package io.prplz.skypixel.gui.settings;

import io.prplz.skypixel.gui.Pane;

import java.util.List;

public class FeatureList extends Pane {

    private final SettingsGui parent;
    private final List<Feature> features;
    private int selectedIndex = -1;
    private int itemHeight = 28;

    public FeatureList(SettingsGui parent, List<Feature> features) {
        this.parent = parent;
        this.features = features;
        contentHeight = features.size() * itemHeight;
    }

    @Override
    public void drawContents(int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < features.size(); i++) {
            if (selectedIndex == i) {
                drawRect(0, i * itemHeight, width, i * itemHeight + itemHeight, 0x60808080);
            }
            drawString(mc.fontRendererObj, features.get(i).getName(), 3, i * itemHeight + 5, 0xFFFFFF);
            drawString(mc.fontRendererObj, features.get(i).getStatus(), 3, i * itemHeight + 15, 0xFFFFFF);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseY < features.size() * itemHeight) {
            selectedIndex = mouseY / itemHeight;
            parent.setSelectedFeature(features.get(selectedIndex));
        }
    }
}
