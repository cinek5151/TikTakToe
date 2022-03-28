package com.kodilla.tictactoe;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TicTacToe extends Application {

    GridPane grid = new GridPane();

    public Parent createContent() {
        TicTacToeController controller = new TicTacToeController(grid);
        grid.setPrefSize(600, 600);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setOnMousePressed(event -> {
                    Tile source = (Tile) event.getSource();
                    controller.runAGame(source);
                });
                grid.add(tile, i, j);
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle("TicTacToe");
        primaryStage.show();
    }
}