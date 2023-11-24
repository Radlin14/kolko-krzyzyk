package com.example.gui.model;

public class Main {
    public static void main(String[] args) throws Exception {
        Board board = new Board(3, 3, 3);
        View view = new View();
        Model model = new Model(board);
        Controller controller = new Controller(model, view);
        controller.gameLoop();

    }
}