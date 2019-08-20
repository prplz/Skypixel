package io.prplz.skypixel.utils;

public class RomanNumerals {

    private static final String[] numerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI"};

    public static String get(int i) {
        if (i > 0 && i <= numerals.length) {
            return numerals[i - 1];
        } else {
            return Integer.toString(i);
        }
    }
}
