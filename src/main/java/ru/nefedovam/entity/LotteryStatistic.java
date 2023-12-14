package ru.nefedovam.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class LotteryStatistic {
    private final String date;
    private final List<WonStatistic> wonStatisticList;

    public LotteryStatistic(List<WonStatistic> wonStatisticList) {
        this.date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
        this.wonStatisticList = wonStatisticList;
    }

    public List<WonStatistic> getWonStatisticList() {
        return wonStatisticList;
    }

    public String getDate() {
        return date;
    }
}
