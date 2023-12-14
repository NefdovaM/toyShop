package ru.nefedovam;

import ru.nefedovam.entity.LotteryStatistic;
import ru.nefedovam.entity.Toy;
import ru.nefedovam.entity.ToyType;
import ru.nefedovam.entity.WonStatistic;

import java.util.*;
import java.util.stream.Collectors;

public class ToyShop {
    private final LotteryStatisticRepository statisticRepository = new LotteryStatisticRepository();
    private final ToyStock stock = new ToyStock();
    private static final int MENU_EXIT = 0;
    private static final int MENU_PRINT_ALL = 1;
    private static final int MENU_SHOW_PRIORITY = 2;
    private static final int MENU_EDIT_PRIORITY = 3;
    private static final int MENU_CHECK_LOTTERY = 4;
    private static final int MENU_START_LOTTERY = 5;
    private static final int MENU_GET_STATISTIC = 6;

    private final HashMap<ToyType, Integer> priorityMap = new HashMap<>() {{
        put(ToyType.BALL, 10);
        put(ToyType.DOLL, 15);
        put(ToyType.CONSTRUCTOR, 1);
        put(ToyType.BEAR, 6);
    }};

    public void startShop() {
        int userInput;
        do {
            System.out.println("Выберите пункт меню: ");
            System.out.println(MENU_EXIT + ") Выйти");
            System.out.println(MENU_PRINT_ALL + ") Показать все игрушки");
            System.out.println(MENU_SHOW_PRIORITY + ") Показать шансы выпадения");
            System.out.println(MENU_EDIT_PRIORITY + ") Редактировать шансы выпадения");
            System.out.println(MENU_CHECK_LOTTERY + ") Проверить возможность проведения лотереи");
            System.out.println(MENU_START_LOTTERY + ") Начать лотерею");
            System.out.println(MENU_GET_STATISTIC + ") Вывести статистику лотереи");
            Scanner sc = new Scanner(System.in);
            userInput = sc.nextInt();
            switch (userInput) {
                case 1 -> {
                    for (Toy toy : stock.getAllToys()) {
                        System.out.println(toy);
                    }
                }

                case MENU_SHOW_PRIORITY -> priorityMap.forEach((type, priority) -> {
                    System.out.println("Шанс выпадения " + type + " составляет " + priority + "%");
                });
                case MENU_EDIT_PRIORITY -> {
                    for (ToyType toyType : ToyType.values()) {
                        System.out.println("Введите шанс выпадения для - " + toyType);
                        int priority = sc.nextInt();
                        priorityMap.put(toyType, priority);
                    }
                    System.out.println("Шансы выпадения обновлены");
                }
                case MENU_CHECK_LOTTERY -> {
                    boolean hasError = false;
                    for (ToyType toyType : ToyType.values()) {
                        if (stock.getAllToys(toyType).size() < priorityMap.get(toyType)) {
                            hasError = true;
                            System.out.println("Лотерея невозможна, не хватает игрушек типа - " + toyType);
                        }
                    }
                    if (!hasError) {
                        System.out.println("Лотерея возможна!");
                    } else {
                        System.out.println("Закажите у поставщиков недостающие игрушки для начала лотереи! (перезапусти программу)");
                    }
                }
                case MENU_START_LOTTERY -> {
                    System.out.println("Введите кол-во игроков ");
                    int playersCount = sc.nextInt();
                    startLottery(playersCount);
                }
                case MENU_GET_STATISTIC -> {
                    printStatistic();
                }
            }
        } while (userInput != MENU_EXIT);


    }

    private void printStatistic() {
        List<LotteryStatistic> lotteryStatisticList = statisticRepository.getAllStatistic();
        if (lotteryStatisticList.isEmpty()) {
            System.out.println("Пока не было ни одной лотереи");
            return;
        }
        System.out.println("Было проведено " + lotteryStatisticList.size() + " лотерей!");
        lotteryStatisticList.forEach((lotteryStatistic -> {
            System.out.println("Статистика лотереи от " + lotteryStatistic.getDate());

            Optional<Map.Entry<ToyType, List<Toy>>> biggestToy = lotteryStatistic.getWonStatisticList()
                    .stream()
                    .map(WonStatistic::getWonToy)
                    .collect(Collectors.groupingBy(Toy::getToyType)).entrySet().stream()
                    .max(Comparator.comparingInt(entry -> entry.getValue().size()));
            if (biggestToy.isPresent()) {
                int size = biggestToy.get().getValue().size();
                ToyType toyType = biggestToy.get().getKey();
                System.out.println("Самое большое кол-во выигранных игрушек " + size + " типа " + toyType);
            }

        }));
    }

    private void startLottery(int players) {
        List<Toy> toysForPlay = new ArrayList<>();
        for (ToyType toyType : ToyType.values()) {
            int priority = priorityMap.get(toyType);
            try {
                List<Toy> toys = stock.takeToys(toyType, priority);
                toysForPlay.addAll(toys);
            } catch (IllegalArgumentException e) {
                System.out.println("К сожалению, лотерея невозможна, у нас не хватает игрушек типа" + toyType);
                return;
            }
        }
        Collections.shuffle(toysForPlay);
        Random rnd = new Random();

        List<WonStatistic> winningResult = new ArrayList<>();
        for (int playerNumber = 0; playerNumber < players; playerNumber++) {
            int rndIndex = rnd.nextInt(toysForPlay.size());
            Toy toy = toysForPlay.get(rndIndex);
            stock.onWonToy(toy);
            winningResult.add(new WonStatistic(toy, playerNumber));
            toysForPlay.remove(rndIndex);
            System.out.println("Игрок " + playerNumber + " выиграл " + toy);
        }
        LotteryStatistic statistic = new LotteryStatistic(winningResult);
        statisticRepository.insertStatistic(statistic);
    }

}
