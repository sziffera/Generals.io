package client;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameOverPane extends BorderPane {

    GameOverPane(int winner, Main main) {

        main.getReceiver().interrupt();
        main.getMediaPlayer().stop();
        Canvas canvas = new Canvas(830, 550);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.FORESTGREEN);
        gc.fillRect(0, 0, 830, 550);
        gc.setFill(Color.GOLDENROD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER!", 830 / 2, 100);

        if (main.getId() == winner) {
            gc.fillText("YOU WON!", 830 / 2, 400);
            gc.drawImage(new Image("client/won.png"),830 / 2 - 100,170);
        } else {
            gc.fillText("YOU LOST", 830 / 2, 200);
            gc.fillText("Player " +  winner + " won",830/2,230);

        }
        setCenter(canvas);

    }
}
