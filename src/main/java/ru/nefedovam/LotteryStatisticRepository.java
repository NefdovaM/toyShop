package ru.nefedovam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class LotteryStatisticRepository {

    private static final Gson GSON = new Gson();
    private static final String FILE_NAME = "lotteryStatistic.json";
    private final File statisticFile;
    private final Type listType = new TypeToken<ArrayList<ru.nefedovam.entity.LotteryStatistic>>() {
    }.getType();

    public LotteryStatisticRepository() {
        statisticFile = new File(FILE_NAME);
        if (statisticFile.exists()) {
            statisticFile.delete();
        }
    }

    public List<ru.nefedovam.entity.LotteryStatistic> getAllStatistic() {
        try {
            String json = new String(Files.readAllBytes(statisticFile.toPath()));
            return GSON.fromJson(json, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void rewriteStatistic(List<ru.nefedovam.entity.LotteryStatistic> data) {
        try {
            String json = GSON.toJson(data);
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
            writer.write(json);
            writer.flush();
        } catch (IOException ignored) {

        }
    }

    public void insertStatistic(ru.nefedovam.entity.LotteryStatistic lotteryStatistic) {
        List<ru.nefedovam.entity.LotteryStatistic> allStatistic = getAllStatistic();
        allStatistic.add(lotteryStatistic);
        rewriteStatistic(allStatistic);
    }

}
