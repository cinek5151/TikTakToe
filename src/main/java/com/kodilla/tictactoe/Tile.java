package com.kodilla.tictactoe;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

class Tile extends StackPane {

    private Text text = new Text();

    public Tile() {
        Rectangle border = new Rectangle(300, 300, Color.GOLD);
        border.setStroke(Color.DARKMAGENTA);
        setAlignment(Pos.CENTER);
        getChildren().addAll(border, text);
        text.setFont(Font.font(150));
    }

    public Text getText() {
        return text;
    }
}
