package client;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {

    private SendData sendData;
    private static final String SOUND = "bgmusic.mp3";
    private MediaPlayer mediaPlayer;
    private Media media;
    private static int id;
    private Boolean gameOver;
    private VBox root;
    private Stage stage;
    private Menu menu;
    private GeneralsPane generalsPane = null;
    private Receiver receiver;


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        root = new VBox(10);

        gameOver = false;

        media = new Media(new File(SOUND).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.dispose();
            });
        });

        primaryStage.setTitle("Generals.io");
        Image image = new Image("client/icon.png");
        primaryStage.getIcons().add(image);
        menu = new Menu(this);

        primaryStage.setOnCloseRequest(event -> {
                    mediaPlayer.stop();

                    //ha elindult a jatek, de nincs jatekvege, akkor endRequest-et kuld
                    //es leallitja a fogado szalat, kulonben csak siman kilep mivel meg nem kezdodott el a jatek
                    //igy nincs receiver szal sem.
                    if (generalsPane != null && gameOver == false) {
                        sendEndRequest();
                    }
                    System.exit(0);
                }
        );
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.setScene(new Scene(menu, 830, 550, Color.WHITE));
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }

    void setId(int value) {
        id = value;
    }

    int getId() {
        return id;
    }

    synchronized void setGameOver(Boolean b) {
        gameOver = b;
    }

    void setGeneralsPane(GeneralsPane g) {
        generalsPane = g;
    }

    void setSendData(SendData sendData) {
        this.sendData = sendData;
    }

    void sendMessage(Object evetType, Object array) {
        sendData.sendData(evetType, array);
    }

    void sendEndRequest() {
        sendData.sendEndRequest();
    }

    void changePane(Pane newPane) {
        stage.setScene(new Scene(newPane, 830, 550));
    }

    void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    GeneralsPane getGeneralsPane() {
        return generalsPane;
    }

    MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    Receiver getReceiver() {
        return receiver;
    }
}
