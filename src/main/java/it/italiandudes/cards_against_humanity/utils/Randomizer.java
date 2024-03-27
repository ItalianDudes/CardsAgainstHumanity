package it.italiandudes.cards_against_humanity.utils;

import java.util.Random;

public final class Randomizer {

    // Static
    public static final Random RANDOMIZER = new Random();

    // Methods
    public static int randomFromZeroTo(final int max) { // 0 Included to Max excluded
        return RANDOMIZER.nextInt(max);
    }
}
