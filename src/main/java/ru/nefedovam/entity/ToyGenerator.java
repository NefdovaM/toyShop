package ru.nefedovam.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ToyGenerator {

    private static int id = 1;
    private static final Random rnd = new Random();

    public static Toy createRandomToy(ToyType tp) {
        String name = toyNames.get(rnd.nextInt(toyNames.size()));
        return new Toy(
                ++id,
                name,
                tp
        );
    }

    private static List<String> toyNames = new ArrayList<>() {{
        for (int i = 0; i < 100; i++) {
            add("Toy " + i);
        }
    }};
}
