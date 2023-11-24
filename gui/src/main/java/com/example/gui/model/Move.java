package com.example.gui.model;

public class Move {
    public int getX() {
        return x;
    }
    public Move() {}
    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    private int x;
    private int y;

    public void setMove(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Move getMoveFromString(String string) throws MoveException {
        if (string == null || string.length() < 2) {
            throw new MoveException("Input string is too short or null");
        }

        char firstChar = string.toUpperCase().charAt(0);
        int y = firstChar - 'A';

        if (y < 0 || y >= 26) {
            throw new MoveException("First character must be a letter");
        }

        int x;
        try {
            x = Integer.parseInt(string.substring(1)) - 1;
        } catch (NumberFormatException e) {
            throw new MoveException("Wrong string format. Should be a capital letter followed by a number");
        }
        return new Move(x, y);
    }


}
