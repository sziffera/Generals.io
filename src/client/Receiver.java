package client;


import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Receiver extends Thread {

    private ObjectInputStream objectInputStream;
    private Integer[][] soldiers;
    private Integer[][] fieldType;
    private Integer[][] players;
    private Integer[] selected;
    private Main main;
    private GeneralsPane generalsPane;
    private Integer winner;
    private Boolean gameOver;

    Receiver(Main main, GeneralsPane generalsPane, ObjectInputStream o) {

        this.objectInputStream = o;
        this.generalsPane = generalsPane;
        this.main = main;
    }


    @Override
    public void run() {
        System.out.println("receiver started");

        try {
        while (!interrupted()) {

                gameOver = (Boolean) objectInputStream.readObject();
                main.setGameOver(gameOver);

                if (gameOver) {
                    main.sendEndRequest();
                    this.interrupt();
                    winner = (Integer) objectInputStream.readObject();
                } else {
                    selected = (Integer[]) objectInputStream.readObject();
                    players = (Integer[][]) objectInputStream.readObject();
                    soldiers = (Integer[][]) objectInputStream.readObject();
                    fieldType = (Integer[][]) objectInputStream.readObject();
                }
                Platform.runLater(() -> {

                    if (gameOver) {
                        main.getGeneralsPane().getGameOverSound().play();
                        main.changePane(new GameOverPane(winner, main));
                    } else
                        generalsPane.repaint(soldiers, players, fieldType, selected[0], selected[1]);

                });


        }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }

        System.out.println("RECEIVER ENDED");
    }

}
