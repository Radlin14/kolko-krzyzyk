package model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RankingFileManager {
    static public void updateRanking(String userName, GameResult result, String fileName) throws Exception {
        Map<String, String> ranking = null;
        try {
            ranking = getRanking(fileName);
        } catch (Exception e) {
            ranking = new HashMap<>();
        }
        String userValue;
        if(ranking.containsKey(userName))
            userValue = ranking.get(userName);
        else
            userValue = "0\n0\n0";
        Statistics userStatistics = new Statistics().stringToStatistics(userValue);
        userStatistics.updateWithGameResult(result);
        ranking.put(userName, userStatistics.toSave());
        File usersFile = new File(fileName);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile));
            oos.writeObject(ranking);
            oos.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    static public String getRankingString(String filename) throws Exception {
        Map<String, String> ranking = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object readMap = ois.readObject();
            if (readMap instanceof HashMap) {
                ranking.putAll((HashMap) readMap);
            }
        }

        catch (Exception e) {
            return "";
        }
        StringBuilder rankingString = new StringBuilder();
        for (Map.Entry<String, String> entry : ranking.entrySet()) {
            rankingString.append(entry.getKey());
            rankingString.append(Statistics.stringToStatistics(entry.getValue()).toString());
        }
        return rankingString.toString();

    }

    static private Map<String, String> getRanking(String filename) {
        Map<String, String> ranking = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object readMap = ois.readObject();
            if (readMap instanceof HashMap) {
                ranking.putAll((HashMap) readMap);
            }
        }

        catch (Exception e) {
            return new HashMap<>();
        }
        return ranking;
    }
}
class Statistics {
    private int win = 0;
    private int draw = 0;
    private int lose = 0;

    public Statistics(int win, int draw, int lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
    }
    public Statistics() {
        this.win = 0;
        this.draw = 0;
        this.lose = 0;
    }
    public void updateWithGameResult(GameResult result) {
        switch (result) {

            case DRAW -> {
                draw++;
            }
            case WIN -> {
                win++;
            }
            case LOSE -> {
                lose++;
            }
        }
    }

    public static Statistics stringToStatistics(String string)throws Exception {
        String[] parts = string.split("\n");
        try {
            int win = Integer.parseInt(parts[0]);
            int draw = Integer.parseInt(parts[1]);
            int lose = Integer.parseInt(parts[2]);
            return new Statistics(win, draw, lose);
        } catch (Exception e) {
            throw new Exception();
        }
    }
    public String toSave() {
        return String.format("%d\n%d\n%d", win, draw, lose);
    }

    public String toString() {
        return String.format("\nwin: %d\ndraw: %d\nlose: %d\n", win, draw, lose);
    }
}

