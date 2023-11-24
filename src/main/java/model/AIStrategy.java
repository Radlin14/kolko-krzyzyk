package model;

public interface AIStrategy {

    public Move getMove(Board board) throws NoPossibleMoveToMakeException;

}


