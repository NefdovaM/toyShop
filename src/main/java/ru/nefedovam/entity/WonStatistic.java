package ru.nefedovam.entity;

public class WonStatistic {
    private final Toy wonToy;
    private final int userNumber;

    public WonStatistic(Toy toy, int userNumber) {
        this.wonToy = toy;
        this.userNumber = userNumber;
    }

    public Toy getWonToy() {
        return wonToy;
    }

    public int getUserNumber() {
        return userNumber;
    }
}
