package ru.nefedovam;

import ru.nefedovam.entity.Toy;
import ru.nefedovam.entity.ToyGenerator;
import ru.nefedovam.entity.ToyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToyStock {
    private final HashMap<ToyType, List<Toy>> stock = new HashMap<>();

    public ToyStock() {
        for (ToyType toyType : ToyType.values()) {
            stock.put(toyType, new ArrayList<>());
            for (int i = 0; i < 100; i++) {
                stock.get(toyType).add(ToyGenerator.createRandomToy(toyType));
            }
        }
    }

    public List<Toy> getAllToys() {
        List<Toy> result = new ArrayList<>();
        stock.values().forEach(result::addAll);
        return result;
    }

    public List<Toy> getAllToys(ToyType toyType) {
        return stock.get(toyType);
    }

    public List<Toy> takeToys(ToyType toyType, int count) throws IllegalArgumentException {
        List<Toy> toysWithType = stock.get(toyType);

        if (toysWithType.size() < count) {
            throw new IllegalArgumentException("can't take " + count + " of toys total size is " + toysWithType.size());
        }

        return toysWithType.subList(0, count);
    }

    public void onWonToy(Toy toy) {
        stock.get(toy.toyType).remove(toy);
    }
}
