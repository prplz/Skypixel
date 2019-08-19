package io.prplz.skypixel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.prplz.skypixel.property.Property;
import io.prplz.skypixel.property.PropertyAdapterFactory;

public class Settings {

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new PropertyAdapterFactory())
            .setPrettyPrinting()
            .create();

    public Property<Boolean> enchantmentGuiEnabled = new Property<>(true);
    public Property<Boolean> anvilUsesEnabled = new Property<>(true);
    public Property<Boolean> cancelItemDamage = new Property<>(true);
    public Property<Boolean> cancelInventoryDrag = new Property<>(true);
}
