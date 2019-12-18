package client;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;

import java.io.File;

import static javafx.scene.input.KeyCode.RIGHT;

public class GeneralsPane extends BorderPane {

    private Canvas canvas;
    private int posY, posX, tempX, tempY;
    private int rows;
    private int columns;
    private GraphicsContext gc;
    private static final int MOUNTAIN = -1;
    private static final int FIELD = 0;
    private static final int SOLDIER_FIELD = 7;
    private static final int OWNED_TOWN = 5;
    private static final int FREE_TOWN = 8;
    private static final int CAPITAL_CITY = 6;
    private static final int BLOCK_SIZE = 20;
    private Boolean gameOver = false;
    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;
    private static final int PLAYER_3 = 3;
    private static final int PLAYER_4 = 4;
    private static final String STEP_FILE = "step.wav";
    private static final String GAME_OVER_FILE = "gameover.wav";
    private MediaPlayer stepSound, gameOverSound;
    private Media media, media2;
    private Image mountain, capitalCity, town;
    Main main;


    public GeneralsPane(Main main, int rows, int columns, Integer[][] soldiers, Integer[][] players, Integer[][] fieldTypes, int x, int y) {

        this.main = main;

        this.rows = rows;
        this.columns = columns;
        this.posY = y;
        this.posX = x;

        media = new Media(new File(STEP_FILE).toURI().toString());
        stepSound = new MediaPlayer(media);
        media2 = new Media((new File(GAME_OVER_FILE).toURI().toString()));
        gameOverSound = new MediaPlayer(media2);


        mountain = new Image("client/mountain.png");
        capitalCity = new Image("client/capital.png");
        town = new Image("client/town.png");

        canvas = new Canvas(columns * BLOCK_SIZE, rows * BLOCK_SIZE);
        canvas.setFocusTraversable(true);
        canvas.setOnKeyReleased(this::keyPressed);
        canvas.setOnMouseClicked(this::mouseEvent);
        gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font(10.5));
        gc.setFontSmoothingType(FontSmoothingType.LCD);

        setCenter(canvas);
        repaint(soldiers, players, fieldTypes, x, y);

    }

    synchronized void repaint(Integer[][] soldiers, Integer[][] players, Integer[][] fieldTypes, int x, int y) {


        if (x != posX || y != posY) {
            stepSound.play();
            stepSound.seek(stepSound.getStartTime());
        }
        posX = x;
        posY = y;

        gc.setFill(Color.FORESTGREEN);
        gc.fillRect(0, 0, BLOCK_SIZE * columns, BLOCK_SIZE * rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                int tempX = BLOCK_SIZE * j;
                int tempY = BLOCK_SIZE * i;

                if (fieldTypes[i][j] == MOUNTAIN) {

                    gc.drawImage(mountain, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                }

                if (players[i][j] == main.getId()) {
                    if (fieldTypes[i][j] == CAPITAL_CITY) {
                        gc.setFill(Color.RED);
                        gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                        gc.drawImage(capitalCity, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                    }
                    if (fieldTypes[i][j] == SOLDIER_FIELD) {
                        gc.setFill(Color.DARKRED);
                        gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                    }
                    if (fieldTypes[i][j] == OWNED_TOWN) {
                        gc.setFill(Color.DARKRED);
                        gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                        gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                    }

                } else {

                    switch (players[i][j]){

                        case PLAYER_1:
                            if (fieldTypes[i][j] == CAPITAL_CITY) {
                                gc.setFill(Color.DARKGREEN);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(capitalCity, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);

                            }
                            if (fieldTypes[i][j] == SOLDIER_FIELD) {
                                gc.setFill(Color.MEDIUMSEAGREEN);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                            }
                            if (fieldTypes[i][j] == OWNED_TOWN) {
                                gc.setFill(Color.MEDIUMSEAGREEN);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);

                            }
                            break;

                        case PLAYER_2:
                            if (fieldTypes[i][j] == CAPITAL_CITY) {
                                gc.setFill(Color.DARKBLUE);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(capitalCity, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);

                            }
                            if (fieldTypes[i][j] == SOLDIER_FIELD) {
                                gc.setFill(Color.DEEPSKYBLUE);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                            }
                            if (fieldTypes[i][j] == OWNED_TOWN) {
                                gc.setFill(Color.DEEPSKYBLUE);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);

                            }
                            break;
                        case PLAYER_3:
                            if (fieldTypes[i][j] == CAPITAL_CITY) {
                                gc.setFill(Color.MEDIUMPURPLE);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(capitalCity, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);

                            }
                            if (fieldTypes[i][j] == SOLDIER_FIELD) {
                                gc.setFill(Color.PURPLE);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                            }
                            if (fieldTypes[i][j] == OWNED_TOWN) {
                                gc.setFill(Color.PURPLE);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);

                            }
                            break;
                        case PLAYER_4:
                            if (fieldTypes[i][j] == CAPITAL_CITY) {
                                gc.setFill(Color.DARKORANGE);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(capitalCity, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);

                            }
                            if (fieldTypes[i][j] == SOLDIER_FIELD) {
                                gc.setFill(Color.DARKGOLDENROD);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                            }
                            if (fieldTypes[i][j] == OWNED_TOWN) {
                                gc.setFill(Color.DARKGOLDENROD);
                                gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                                gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);

                            }
                            break;
                    }


                }

                if (fieldTypes[i][j] == FREE_TOWN) {
                    gc.setFill(Color.DARKGREEN);
                    gc.fillRect(tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                    gc.drawImage(town, tempX, tempY, BLOCK_SIZE, BLOCK_SIZE);
                }

                if (soldiers[i][j] > 0) {

                    gc.setFill(Color.WHITE);
                    gc.setTextAlign(TextAlignment.CENTER);

                    gc.fillText(soldiers[i][j].toString(), tempX + BLOCK_SIZE / 2, tempY + BLOCK_SIZE - 4, BLOCK_SIZE);

                }
                gc.strokeRect(posX * BLOCK_SIZE, posY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    private synchronized void keyPressed(KeyEvent ev) {

        tempX = posX;
        tempY = posY;
        if (ev.getCode() == KeyCode.UP || ev.getCode() == KeyCode.DOWN || ev.getCode() == KeyCode.LEFT || ev.getCode() == RIGHT) {

            switch (ev.getCode()) {
                case RIGHT:
                    tempX++;
                    break;

                case LEFT:
                    tempX--;
                    break;

                case UP:
                    tempY--;
                    break;

                case DOWN:
                    tempY++;
                    break;
            }

            String eventType = "key";

            Integer[] sendKey = new Integer[4];

            sendKey[0] = tempX;
            sendKey[1] = tempY;
            sendKey[2] = posX;
            sendKey[3] = posY;

            main.sendMessage(eventType, sendKey);
        }
    }


    private synchronized void mouseEvent(MouseEvent event) {

        Integer[] sendClick = new Integer[2];

        sendClick[0] = (int) event.getX() / BLOCK_SIZE;
        sendClick[1] = (int) event.getY() / BLOCK_SIZE;
        main.sendMessage("mouse", sendClick);

    }

    MediaPlayer getGameOverSound() {
        return gameOverSound;
    }

}
