package com.example.gui.model;

public class Model {
    private Board board;
    private String loggedUser = "";
    private PlayerType noughtPlayerType = PlayerType.HUMAN_PLAYER;
    private PlayerType crossPlayerType = PlayerType.HUMAN_PLAYER;
    private int turn = 0;
    private FieldValue whoseTurn = FieldValue.NOUGHT;
    private GameState state = GameState.MENU;
    private boolean isLogged = false;

    public boolean getIsLogged() {
        return isLogged;
    }
    public String getLoggedUser() {
        return loggedUser;
    }
    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }
    public void validateUser(String userName, String password) {
        if(UserFileManager.validateUser(userName, password, "users.list")) {
            isLogged = true;
            loggedUser = userName;
        }
    }
    public void createUser(String userName, String password) throws Exception {
        UserFileManager.insertUser(userName, password, "users.list");
    }
    public void setState(GameState state) {
        this.state = state;
    }
    public GameState getState() {
        return state;
    }
    public void makeMove(int x, int y) throws Exception {
        if(!board.doesFieldExists(x, y)) {
            throw new Exception("This field do not exist");
        }
        if(!board.isFieldFree(x, y)) {
            throw new Exception("Field with this coordinates is already occupied");
        }
        FieldValue value;
        if(whoseTurn == FieldValue.NOUGHT)
            value = FieldValue.NOUGHT;
        else
            value = FieldValue.CROSS;
        board.setFieldValueWithStateUpdate(x, y, value);
        turn++;
        whoseTurn = whoseTurn.toggle();
    }
    public String getRanking() throws Exception {
        return RankingFileManager.getRankingString("ranking.list");
    }

    public void updateRankingHumanPlayerAgainstAI() throws Exception {
        if(isLogged) {
            if (noughtPlayerType == PlayerType.HUMAN_PLAYER && crossPlayerType != PlayerType.HUMAN_PLAYER) {
                if (board.getState() == BoardState.NOUGHT_WIN) {
                    RankingFileManager.updateRanking(loggedUser, GameResult.WIN, "ranking.list");
                    return;
                }
                if (board.getState() == BoardState.CROSS_WIN) {
                    RankingFileManager.updateRanking(loggedUser, GameResult.LOSE, "ranking.list");
                    return;
                }
                if (board.getState() == BoardState.DRAW) {
                    RankingFileManager.updateRanking(loggedUser, GameResult.DRAW, "ranking.list");
                    return;
                }
            }
            if (noughtPlayerType != PlayerType.HUMAN_PLAYER && crossPlayerType == PlayerType.HUMAN_PLAYER) {
                if (board.getState() == BoardState.NOUGHT_WIN) {
                    RankingFileManager.updateRanking(loggedUser, GameResult.LOSE, "ranking.list");
                    return;
                }
                if (board.getState() == BoardState.CROSS_WIN) {
                    RankingFileManager.updateRanking(loggedUser, GameResult.WIN, "ranking.list");
                    return;
                }
                if (board.getState() == BoardState.DRAW) {
                    RankingFileManager.updateRanking(loggedUser, GameResult.DRAW, "ranking.list");
                    return;
                }
            }
        }
    }
    public Board getBoard() {
        return this.board;
    }

    public void setNoughtPlayerType(PlayerType noughtPlayerType) {
        this.noughtPlayerType = noughtPlayerType;
    }

    public void setCrossPlayerType(PlayerType crossPlayerType) {
        this.crossPlayerType = crossPlayerType;
    }

    public void makeAIMove(PlayerType aiType) throws Exception {
        switch (aiType) {

            case EASY_AI -> {
                makeMinMaxAIMove(3);
            }
            case MEDIUM_AI -> {
                makeMinMaxAIMove(4);
            }
            case HARD_AI -> {
                makeMinMaxAIMove(3);
            }
            case RANDOM_AI -> {
                makeRandomAIMove();
            }
            case HUMAN_PLAYER -> {
                throw new Exception("cannot invoke function for human player");
            }
        }
    }
    private void makeMinMaxAIMove(int difficulty) {
        Move move = new Move();
        MinMaxAI minMax = new MinMaxAI(difficulty, whoseTurn);
        try {
            move = minMax.getMove(board);
            makeMove(move.getX(), move.getY());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void makeRandomAIMove() throws NoPossibleMoveToMakeException {
        RandomAI randomAI = new RandomAI();
        Move move = randomAI.getMove(board);
        try {
            makeMove(move.getX(), move.getY());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBoard(Board board) {
        this.board = board;
        turn = 0;
        whoseTurn = FieldValue.NOUGHT;
    }
    public PlayerType getPlayerTypeWhichHasTurn() {
        if(whoseTurn == FieldValue.NOUGHT)
            return noughtPlayerType;
        return crossPlayerType;
    }
    public void updatePlayersType(PlayerType type, FieldValue whoUpdate) {
        if(whoUpdate == FieldValue.CROSS)
            crossPlayerType = type;
        if(whoUpdate == FieldValue.NOUGHT)
            noughtPlayerType = type;

    }
    public int getTurn() {
        return turn;
    }
    public FieldValue getPlayerTurn() {
        return whoseTurn;
    }

    public Model(Board board, PlayerType noughtPlayerType, PlayerType crossPlayerType, FieldValue whoseTurn, int turn, GameState state) {
        this.board = board;
        this.noughtPlayerType = noughtPlayerType;
        this.crossPlayerType = crossPlayerType;
        this.whoseTurn = whoseTurn;
        this.turn = turn;
        this.state = state;
    }

    public PlayerType getNoughtPlayerType() {
        return noughtPlayerType;
    }
    public PlayerType getCrossPlayerType() {
        return crossPlayerType;
    }

    public Model(Board board) {
        this.board = board;
    }

    public Board getBoardCopy() {
        return board.copy();
    }

    public void resetGame() {
        board.fillAllFieldsWithFieldValue(FieldValue.FREE);
        turn = 0;
        whoseTurn = FieldValue.NOUGHT;
    }



}


