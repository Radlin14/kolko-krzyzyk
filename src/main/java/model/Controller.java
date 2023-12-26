package model;

public class Controller {
    private View view;
    private Model model;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }
    public void gameLoop() {
        while(true) {
            if(model.getState() == GameState.MENU) {
                if(model.getIsLogged()) {
                    view.displayMessage(String.format("Logged as %s\n", model.getLoggedUser()));
                }
                view.displayMenu();
                String input = view.getUserInput("type menu option between 1 and 8");
                switch (input) {
                    case "1":
                        setupClassicBoard();
                        setupPlayers();
                        model.setState(GameState.GAME);
                        break;
                    case "2":
                        setupBoard();
                        setupPlayers();
                        model.setState(GameState.GAME);
                        break;
                    case "3":
                        Model model;
                        try {
                            model = GameFileManager.getModelFromFile("autosave.list");
                        } catch (ModelFileManagerException e) {
                            view.displayMessage(e.getMessage());
                            break;
                        }
                        this.model = model;

                        break;
                    case "4":
                        logInLoop();
                        break;
                    case "5":
                        createUser();
                        break;
                    case "6":
                        displayRanking();
                        break;
                    case "7":
                        return;

                    case "8":
                        logOut();
                        break;
                    default:
                        view.displayUnrecognizedCommand(input);

                }
            }

            if(model.getState() == GameState.GAME) {
                if (model.getBoardCopy().getState() != BoardState.PENDING) {
                    try {
                        model.updateRankingHumanPlayerAgainstAI();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    view.displayGameResult(model.getBoardCopy().getState());
                    view.displayBoard(model.getBoardCopy());
                    setupEndGameMenu();

                    continue;
                }


                Move move = new Move();
                view.displayBoard(model.getBoardCopy());
                view.displayWhoseTurn(model.getPlayerTurn());

                switch (model.getPlayerTypeWhichHasTurn()) {
                    case HUMAN_PLAYER -> {
                        try {
                            move = getHumanPlayerMove();
                        } catch (MoveException e) {
                            view.displayMessage(e.getMessage());
                            continue;
                        }
                    }
                    case RANDOM_AI -> {
                        try {
                            move = getRandomAIMove();
                        } catch (NoPossibleMoveToMakeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case EASY_AI -> {
                        move = getMinMaxAIMove(3);
                    }
                    case MEDIUM_AI -> {
                        move = getMinMaxAIMove(5);
                    }
                    case HARD_AI -> {
                        move = getMinMaxAIMove(7);
                    }
                }
                try {
                    model.makeMove(move.getX(), move.getY());
                } catch (Exception e) {
                    view.displayMessage(e.getMessage());
                }
                try {
                    GameFileManager.saveModel(model, "autosave.list");
                } catch (ModelFileManagerException e) {
                    view.displayMessage(e.getMessage());
                }
            }
        }
    }
    private void displayRanking() {
        try {
            view.displayMessage(model.getRanking());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createUser() {
        while (true) {
            view.displayMessage("type user name");
            String userName = view.getUserInput("user name");
            view.displayMessage("type password");

            String password = view.getUserInput("password");
            try {
                model.createUser(userName, password);
                view.displayMessage(String.format("Successfully created user %s", userName));
                return;
            } catch (Exception e) {
                 view.displayMessage("user name is already in use");
            }
            while(true) {
                view.displayMenuIncorrectData();
                String input = view.getUserInput("Choose option");
                if(input.equals("1")) {
                    break;
                }
                if(input.equals("2")) {
                    model.setState(GameState.MENU);
                    return;
                }
                view.displayUnrecognizedCommand(input);
            }
        }
    }
    private void logOut() {
        model.setIsLogged(false);
    }
    private void logInLoop() {
        while(true) {
            view.displayMessage("type user name");
            String userName = view.getUserInput("user name");
            view.displayMessage("type password");

            String password = view.getUserInput("password");
            model.validateUser(userName, password);
            if(model.getIsLogged()) {
                view.displayMessage(String.format("You log in as %s", model.getLoggedUser()));
                break;
            }
            while(true) {
                view.displayMessage("password or username is incorrect");
                view.displayMenuIncorrectData();
                String input = view.getUserInput("Choose option");
                if("1".equals(input)) {
                    break;
                }
                if(input.equals("2")) {
                    model.setState(GameState.MENU);
                    return;
                }
                view.displayUnrecognizedCommand(input);
            }

        }
    }
    private void setupBoard() {
        int width = getUserInputNumberInBoundries("Type board width", Board.MIN_BOARD_SIZE, Board.MAX_BOARD_SIZE);
        int height = getUserInputNumberInBoundries("Type board height", Board.MIN_BOARD_SIZE, Board.MAX_BOARD_SIZE);
        int marksToWin = getUserInputNumberInBoundries("Type number of marks to win", Board.MIN_BOARD_SIZE, Math.max(width, height));
        Board board = new Board(width, height, marksToWin);
        model.setBoard(board);
    }
    private int getUserInputNumberInBoundries(String inputMessage, int start, int end) {
        int result;
        while(true) {
            String input = view.getUserInput(inputMessage);
            try {
                result = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                view.displayUnrecognizedCommand(input);
                continue;
            }
            if(result >= start && result <= end)
                break;
        }
        return result;
    }


    private void setupPlayers() {
        setupPlayerType(FieldValue.NOUGHT);
        setupPlayerType(FieldValue.CROSS);
    }
    private void setupClassicBoard() {
        Board board = new Board(3, 3, 3);
        model.setBoard(board);
    }
    private void setupEndGameMenu() {
        while (true) {
            view.displayEndGameMenu();
            String input = view.getUserInput("choose whats next");
            if(input.equals("1")) {
                model.resetGame();
                model.setState(GameState.GAME);
                break;
            }
            if(input.equals("2")) {
                model.setState(GameState.MENU);
                break;
            }
        }
    }

    private void setupPlayerType(FieldValue playerType) {
        while(true) {
            view.displayPlayerTypeMenu();
            String input = view.getUserInput("choose player type");
            if(input.equals("1")) {
                model.updatePlayersType(PlayerType.HUMAN_PLAYER, playerType);
                break;
            }
            if(input.equals("2")) {
                model.updatePlayersType(PlayerType.RANDOM_AI, playerType);
                break;
            }
            if(input.equals("3")) {
                model.updatePlayersType(PlayerType.EASY_AI, playerType);
                break;
            }
            if(input.equals("4")) {
                model.updatePlayersType(PlayerType.MEDIUM_AI, playerType);
                break;
            }
            if(input.equals("5")) {
                model.updatePlayersType(PlayerType.HARD_AI, playerType);
                break;
            }
            view.displayUnrecognizedCommand(input);
        }
    }
    private Move getMinMaxAIMove(int difficulty) {
        Move move = new Move();
        MinMaxAI minMax = new MinMaxAI(difficulty, model.getPlayerTurn());
        try {
            move = minMax.getMove(model.getBoardCopy());
        } catch (NoPossibleMoveToMakeException e) {
            view.displayMessage(e.getMessage());
        }
        return move;
    }
    private Move getRandomAIMove() throws NoPossibleMoveToMakeException {
        RandomAI randomAI = new RandomAI();
        return randomAI.getMove(model.getBoardCopy());
    }
    private Move getHumanPlayerMove() throws MoveException {
        String userMove = view.getUserMove();
        return Move.getMoveFromString(userMove);
    }
}
