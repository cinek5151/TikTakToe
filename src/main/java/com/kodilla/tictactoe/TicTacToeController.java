package com.kodilla.tictactoe;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TicTacToeController implements Serializable {

    GridPane grid;
    File savedMap = new File("ranking.list");
    Map<String, Integer> map = new LinkedHashMap<>();
    Integer numberOfRound = 0;

    public TicTacToeController(GridPane grid) {
        System.out.println("Creating controller");
        loadMap();
        numberOfRound = map.getOrDefault("round", numberOfRound);
        numberOfRound++;
        map.put("round", numberOfRound);
        this.grid = grid;
    }

    public List<Tile> findEmptyTiles() {
        List<Tile> emptyTiles = grid.getChildren().stream()
                .map(node -> ((Tile) node))
                .filter(tile -> tile.getText().getText().equals(""))
                .collect(Collectors.toList());
        return emptyTiles;
    }

    public List<Tile> findTiles(String mark) {
        List<Tile> tiles = grid.getChildren().stream()
                .map(node -> ((Tile) node))
                .filter(tile -> tile.getText().getText().equals(mark))
                .collect(Collectors.toList());
        return tiles;
    }

    public void computerMove() {
        List<Tile> emptyTiles = findEmptyTiles();

        Optional<Tile> anyTile = emptyTiles.stream()
                .findAny();

        if (anyTile.isPresent()) {
            Tile tile1 = anyTile.get();
            tile1.getText().setText("O");
        }
    }

    public boolean ifFieldWasUsedBefore(Tile tile) {
        boolean result = tile.getText().getText().equals("");
        return result;
    }

    public boolean checkRows(String mark) {
        List<Tile> tiles = findTiles(mark);

        int row0 = 0;
        int row1 = 0;
        int row2 = 0;
        for (Tile tile : tiles) {
            if (GridPane.getRowIndex(tile) == 0) {
                row0++;
            } else if (GridPane.getRowIndex(tile) == 1) {
                row1++;
            } else {
                row2++;
            }
        }
        return row0 == 3 || row1 == 3 || row2 == 3;
    }

    public boolean checkColumns(String mark) {
        List<Tile> tiles = findTiles(mark);
        int column0 = 0;
        int column1 = 0;
        int column2 = 0;

        for (Tile tile : tiles) {
            if (GridPane.getColumnIndex(tile) == 0) {
                column0++;
            } else if (GridPane.getColumnIndex(tile) == 1) {
                column1++;
            } else {
                column2++;
            }
        }
        return column0 == 3 || column1 == 3 || column2 == 3;
    }

    public boolean checkDiagonals(String mark) {
        List<Tile> tiles = findTiles(mark);
        int diagonal1 = 0;
        int diagonal2 = 0;

        for (Tile tile : tiles) {
            if (GridPane.getRowIndex(tile) == 0 && GridPane.getColumnIndex(tile) == 0) {
                diagonal1++;
            }
            if (GridPane.getRowIndex(tile) == 1 && GridPane.getColumnIndex(tile) == 1) {
                diagonal1++;
                diagonal2++;
            }
            if (GridPane.getRowIndex(tile) == 2 && GridPane.getColumnIndex(tile) == 2) {
                diagonal1++;
            }
            if (GridPane.getRowIndex(tile) == 2 && GridPane.getColumnIndex(tile) == 0) {
                diagonal2++;
            }
            if (GridPane.getRowIndex(tile) == 0 && GridPane.getColumnIndex(tile) == 2) {
                diagonal2++;
            }
        }
        return diagonal1 == 3 || diagonal2 == 3;
    }

    public boolean isWinningCombination(String mark) {
        return checkRows(mark) || checkColumns(mark) || checkDiagonals(mark);
    }

    public boolean isDraw() {
        List<Tile> emptyTiles = findEmptyTiles();
        return (emptyTiles.size() == 0);
    }



    public boolean verifyResult() {
        if (isWinningCombination("X") || isWinningCombination("O") || isDraw()) {
            addResults();
            saveMap();
            return endOfGame();
        }
        return true;
    }

    public void runAGame(Tile tile) {
        if (ifFieldWasUsedBefore(tile)) {
            tile.getText().setText("X");
            boolean nextMove = verifyResult();
            if (nextMove) {
                computerMove();
                verifyResult();
            }
        }
    }

    public void addResults() {
        if (isWinningCombination("X")) {
            putScoreInMap("X");
        } else if (isWinningCombination("O")) {
            putScoreInMap("O");
        } else if (isDraw()) {
            putScoreInMap("Draw");
        }
    }

    private void putScoreInMap(String result) {
        result = "Round " + numberOfRound + " " + result;
        if (map.containsKey(result)) {
            Integer score = map.get(result);
            score++;
            map.put(result, score);
        } else {
            map.put(result, 1);
        }
        System.out.println("saved " + map);
    }

    public void saveMap() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedMap));
            oos.writeObject(map);
            oos.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadMap() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedMap));
            Object readList = ois.readObject();
            map = (Map<String, Integer>) readList;
            ois.close();
            System.out.println(map);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String showRanking() {
        String result;
        String gameResult = "";
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (!entry.getKey().equals("round")) {
                result = entry.getKey() + " = " + entry.getValue();
                gameResult = gameResult + result + "\n";
            }
        }
        return gameResult;
    }

    public void clearTheBoard() {
        List<Tile> allTiles = grid.getChildren().stream()
                .map(node -> ((Tile) node))
                .collect(Collectors.toList());

        for (Tile tile : allTiles) {
            tile.getText().setText("");
        }
    }

    public boolean endOfGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game over");
        String message = "";
        alert.setHeaderText("Thank you for playing Tic Tac Toe");
        if (isWinningCombination("X")) {
            message = "The winner is X, \nDo you want to play again?";
        } else if (isWinningCombination("O")) {
            message = "The winner is O, \nDo you want to play again?";
        } else if (isDraw()) {
            message = "Draw, \nDo you want to play again?";
        }
        alert.setContentText(message);

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        ButtonType buttonRanking = new ButtonType("Show Ranking");

        alert.getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
        alert.getButtonTypes().addAll(buttonYes, buttonNo, buttonRanking);
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == (buttonYes)) {
            clearTheBoard();
            return false;

        } else if (response.isPresent() && response.get() == (buttonNo)) {
            Platform.exit();

        } else if (response.isPresent() && response.get() == (buttonRanking)) {
            Alert rankingInformation = new Alert(Alert.AlertType.INFORMATION);
            rankingInformation.setTitle("Ranking");
            loadMap();
            rankingInformation.setHeaderText(showRanking());
            rankingInformation.showAndWait();
        }
        return true;
    }
}