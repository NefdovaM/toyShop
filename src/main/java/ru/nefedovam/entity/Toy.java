package ru.nefedovam.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Toy {
    public final int id;
    public final String name;
    public final ToyType toyType;

    public Toy(int id, String name, ToyType toyType) {
        this.id = id;
        this.name = name;
        this.toyType = toyType;
    }

    @Override
    public String toString() {
        return "Toy: " +
                "id - " + id +
                ", name - '" + name + '\'' +
                ", toyType - " + toyType;
    }

    public ToyType getToyType() {
        return toyType;
    }
}
