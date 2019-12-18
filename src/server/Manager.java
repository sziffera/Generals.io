package server;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

class Manager {

    private Integer rows, columns;
    private Integer[][] soldiers;
    private Integer[][] fieldTypes;
    private Integer[][] players;
    private Integer winner = null;
    private Integer[] player1Selected, player2Selected, player3Selected, player4Selected;
    private Boolean gameOver = false;
    private int clientNumber = 2;

    private static final int MOUNTAIN = -1;
    private static final int FIELD = 0;
    private static final int SOLDIER_FIELD = 7;
    private static final int OWNED_TOWN = 5;
    private static final int FREE_TOWN = 8;
    private static final int CAPITAL_CITY = 6;
    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;
    private static final int PLAYER_3 = 3;
    private static final int PLAYER_4 = 4;

    private SoldierIncreaser1 soldierIncreaser1;
    private SoldierIncreaser2 soldierIncreaser2;
    private Listener2 listener2;


    Manager() {

        this.rows = 27;
        this.columns = 41;
        fieldTypes = new Integer[rows][columns];
        soldiers = new Integer[rows][columns];
        players = new Integer[rows][columns];

        Random random = new Random();

        player1Selected = new Integer[2];
        player2Selected = new Integer[2];
        player3Selected = new Integer[2];
        player4Selected = new Integer[4];

        setUpMatrix();

        for (int i = 0; i < random.nextInt(20) + 60; i++) {
            int[] temp = freePosition();
            fieldTypes[temp[0]][temp[1]] = MOUNTAIN;
        }

        for (int i = 0; i < random.nextInt(5) + 2; i++) {
            int[] temp = freePosition();
            fieldTypes[temp[0]][temp[1]] = FREE_TOWN;
            soldiers[temp[0]][temp[1]] = random.nextInt(20) + 40;
        }


        for (Client c : Client.clients) {
            try {
                int[] temp = freePosition();
                players[temp[0]][temp[1]] = c.getClientId();
                fieldTypes[temp[0]][temp[1]] = CAPITAL_CITY;
                soldiers[temp[0]][temp[1]] = 1;

                if (c.getClientId() == PLAYER_1) {
                    player1Selected[0] = temp[1];
                    player1Selected[1] = temp[0];
                } else if (c.getClientId() == PLAYER_2) {
                    player2Selected[0] = temp[1];
                    player2Selected[1] = temp[0];
                }

                c.getObjectOutputStream().writeObject(true);
                c.getObjectOutputStream().writeObject(c.getClientId());
                c.getObjectOutputStream().writeObject(temp[1]);
                c.getObjectOutputStream().writeObject(temp[0]);
                c.getObjectOutputStream().writeObject(rows);
                c.getObjectOutputStream().writeObject(columns);
                c.getObjectOutputStream().writeObject(players);
                c.getObjectOutputStream().writeObject(soldiers);
                c.getObjectOutputStream().writeObject(fieldTypes);
                c.getObjectOutputStream().flush();
                c.getObjectOutputStream().reset();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        listener2 = new Listener2(this);
        listener2.start();
        soldierIncreaser1 = new SoldierIncreaser1(this);
        soldierIncreaser2 = new SoldierIncreaser2(this);
        soldierIncreaser1.start();
        soldierIncreaser2.start();
    }

    private void setUpMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                fieldTypes[i][j] = FIELD;
                soldiers[i][j] = 0;
                players[i][j] = 0;
            }
        }
    }

    private synchronized void send() {

        for (Client c : Client.clients) {
            try {

                if (gameOver == false) {
                    c.getObjectOutputStream().writeObject(gameOver);
                    c.getObjectOutputStream().reset();

                    switch (c.getClientId()) {
                        case PLAYER_1:
                            c.getObjectOutputStream().writeObject(player1Selected);
                            break;
                        case PLAYER_2:
                            c.getObjectOutputStream().writeObject(player2Selected);
                            break;
                        case PLAYER_3:
                            c.getObjectOutputStream().writeObject(player3Selected);
                            break;
                        case PLAYER_4:
                            c.getObjectOutputStream().writeObject(player4Selected);
                            break;
                    }

                    c.getObjectOutputStream().writeObject(players);
                    c.getObjectOutputStream().reset();
                    c.getObjectOutputStream().writeObject(soldiers);
                    c.getObjectOutputStream().reset();
                    c.getObjectOutputStream().writeObject(fieldTypes);
                    c.getObjectOutputStream().reset();
                    c.getObjectOutputStream().flush();
                    c.getObjectOutputStream().reset();
                } else {
                    c.getObjectOutputStream().writeObject(gameOver);
                    c.getObjectOutputStream().writeObject(winner);
                    c.getObjectOutputStream().flush();
                    c.getObjectOutputStream().reset();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(gameOver){
            try {
                Server.serverSocket.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    synchronized void sendDataToNewClient(int id) {

        clientNumber++;

        int[] temp = freePosition();
        players[temp[0]][temp[1]] = id;
        fieldTypes[temp[0]][temp[1]] = CAPITAL_CITY;
        soldiers[temp[0]][temp[1]] = 1;
        setSelected(id,temp[1],temp[0]);
        for (Client c : Client.clients) {
            try {
                if (c.getClientId() == id) {
                    c.getObjectOutputStream().writeObject(true);
                    c.getObjectOutputStream().writeObject(c.getClientId());
                    c.getObjectOutputStream().writeObject(temp[1]);
                    c.getObjectOutputStream().writeObject(temp[0]);
                    c.getObjectOutputStream().writeObject(rows);
                    c.getObjectOutputStream().writeObject(columns);
                    c.getObjectOutputStream().writeObject(players);
                    c.getObjectOutputStream().writeObject(soldiers);
                    c.getObjectOutputStream().writeObject(fieldTypes);
                    c.getObjectOutputStream().flush();
                    c.getObjectOutputStream().reset();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized void increaseSoldiers(int posX, int posY) {

        soldiers[posX][posY]++;
        //kliens frissites
        send();

    }

    synchronized void validateClick(int id, int posX, int posY) {

        //forditva az osszes koordinata!
        if (players[posY][posX] == id && soldiers[posY][posX] > 1) {
            setSelected(id, posX, posY);
            send();
        }

    }

    synchronized int[] freePosition() {

        int[] pos = new int[2];
        Random random = new Random(rows);

        int error = 0;
        while (true) {
            int r = random.nextInt(rows);
            int c = random.nextInt(columns);
            if (fieldTypes[r][c] == FIELD) {
                pos[0] = r;
                pos[1] = c;
                break;
            }
            error++;
            if (error >= rows * columns * 100) {
                System.out.println("Telitve");
                break;
            }
        }
        return pos;
    }

    private void setSelected(int id, int posX, int posY) {

        switch (id) {
            case PLAYER_1:
                player1Selected[0] = posX;
                player1Selected[1] = posY;
                break;
            case PLAYER_2:
                player2Selected[0] = posX;
                player2Selected[1] = posY;
                break;
            case PLAYER_3:
                player3Selected[0] = posX;
                player3Selected[1] = posY;
                break;
            case PLAYER_4:
                player4Selected[0] = posX;
                player4Selected[1] = posY;
                break;
        }

    }

    synchronized void validateStep(int id, int x, int y, int oldX, int oldY) {

        //forditva az osszes koordinata!

        if (x >= 0 && y >= 0 && x < columns && y < rows) {

            if (players[y][x] == id || fieldTypes[y][x] == FIELD) {

                if (soldiers[oldY][oldX] > 1) {

                    soldiers[y][x] += soldiers[oldY][oldX] - 1;
                    soldiers[oldY][oldX] = 1;
                    players[y][x] = id;
                    if (fieldTypes[y][x] == FIELD) {
                        fieldTypes[y][x] = SOLDIER_FIELD;
                    }
                    setSelected(id, x, y);
                }
            }
            if (players[y][x] != id && players[y][x] != 0 && fieldTypes[y][x] != MOUNTAIN) {

                if (soldiers[oldY][oldX] > 1) {

                    if (soldiers[oldY][oldX] - 1 > soldiers[y][x]) {

                        players[y][x] = id;
                        int temp = soldiers[y][x];
                        soldiers[y][x] = soldiers[oldY][oldX] - 1 - temp;
                        soldiers[oldY][oldX] = 1;

                        //jatek vege
                        if (fieldTypes[y][x] == CAPITAL_CITY) {
                            gameOver = true;
                            winner = id;
                            //kapcsolat fogado es a katona novelo szalak leallitasa
                            listener2.interrupt();

                            soldierIncreaser1.interrupt();
                            soldierIncreaser2.interrupt();
                        }
                        setSelected(id, x, y);

                    } else if (soldiers[oldY][oldX] - 1 == soldiers[y][x]) {
                        //megtartja a teruletet
                        soldiers[oldY][oldX] = 1;
                        soldiers[y][x] = 0;

                    }
                }
            }
            if (fieldTypes[y][x] == FREE_TOWN) {

                if (soldiers[oldY][oldX] > 1) {

                    if (soldiers[oldY][oldX] - 1 > soldiers[y][x]) {

                        players[y][x] = id;
                        int temp = soldiers[y][x];
                        soldiers[y][x] = soldiers[oldY][oldX] - 1 - temp;
                        soldiers[oldY][oldX] = 1;
                        fieldTypes[y][x] = OWNED_TOWN;
                        setSelected(id, x, y);
                    }
                }

            }
            send();
        }

    }

    synchronized void closeConnection(int id) {


        clientNumber--;
        if(clientNumber < 1){
            listener2.interrupt();
            soldierIncreaser1.interrupt();
            soldierIncreaser2.interrupt();
            gameOver = true;
            try {
                Server.serverSocket.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }

        for (Client c : Client.clients) {
            if (c.getClientId() == id) {
                try {
                    c.getSocket().close();
                    c.getObjectOutputStream().close();
                    c.getObjectInputStream().close();
                    c.interrupt();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Iterator<Client> iterator = Client.clients.iterator(); iterator.hasNext(); ) {
            Client temp = iterator.next();
            if (temp.getClientId() == id) {
                iterator.remove();
            }
        }

        //leveszi a tablarol a jatekost
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (players[i][j] == id) {
                    players[i][j] = 0;
                    fieldTypes[i][j] = FIELD;
                    soldiers[i][j] = 0;
                }
            }
        }



        System.out.println("Client " + id + " disconnected");


    }

    synchronized Integer[][] getFieldTypes() {
        return fieldTypes;
    }

    Integer getRows() {
        return rows;
    }

    Integer getColumns() {
        return columns;
    }

    public int getSoldierField() {
        return SOLDIER_FIELD;
    }

    public int getOwnedTown() {
        return OWNED_TOWN;
    }

    synchronized Boolean getGameOver(){
        return gameOver;
    }
    public int getCapitalCity() {
        return CAPITAL_CITY;
    }
}
