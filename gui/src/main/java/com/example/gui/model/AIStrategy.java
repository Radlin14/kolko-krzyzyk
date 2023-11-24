package com.example.gui.model;

public interface AIStrategy {

    public Move getMove(Board board) throws NoPossibleMoveToMakeException;

}


