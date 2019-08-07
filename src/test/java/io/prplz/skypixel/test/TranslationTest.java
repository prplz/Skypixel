package io.prplz.skypixel.test;

import io.prplz.skypixel.Translation;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class TranslationTest {

    @Test
    public void testKeys() throws IOException {
        URL resource = Translation.class.getClassLoader().getResource("assets/skypixel/lang/en_US.lang");
        String[] lines = IOUtils.toString(resource).split("\\n");
        for (Translation translation : Translation.values()) {
            assertTrue(
                    translation.key + " exists in en_US.lang",
                    Arrays.stream(lines).anyMatch(line -> line.startsWith(translation.key + "="))
            );
        }
    }
}
