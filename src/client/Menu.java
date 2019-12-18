package client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


class Menu extends BorderPane {


    private Button button;
    private Main main;

    Menu(Main main) {

        this.main = main;


        setBackground(new Background(new BackgroundImage(new Image("client/bg.png"), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        VBox vBox = new VBox(20);
        vBox.setFillWidth(true);
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.setPadding(new Insets(170));
        CheckBox mute = new CheckBox("mute");
        mute.setTextFill(Color.WHITE);
        mute.setOnAction(event -> {
            if(!mute.isSelected()){
                main.getMediaPlayer().play();
            }
            else{
                main.getMediaPlayer().pause();
            }
        });
        button = new Button("JOIN GAME");
        Label label = new Label("Destroy the other player's capital city!");
        label.setTextFill(Color.WHITE);

        vBox.getChildren().addAll(button,label,mute);

        button.setPrefWidth(150);
        button.setWrapText(true);
        button.setTextFill(Color.DARKRED);
        button.setPadding(new Insets(7));
        setCenter(vBox);
        button.setOnAction(event -> {
            button.setText("Waiting for other players");
            Thread thread = new Thread(new SetUpData(main, button));
            thread.setDaemon(true);
            thread.start();
        });

    }

}
