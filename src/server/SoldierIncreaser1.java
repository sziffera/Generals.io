package server;

/**
 * Katonak szamat noveli a varosokban/fovarosokban
 */

public class SoldierIncreaser1 extends Thread {

    private Manager manager;

    SoldierIncreaser1(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {

        try {
            while (!interrupted()) {

                sleep(1000);

                for (int i = 0; i < manager.getRows(); i++) {
                    for (int j = 0; j < manager.getColumns(); j++) {
                        if (manager.getFieldTypes()[i][j] == manager.getOwnedTown() || manager.getFieldTypes()[i][j] == manager.getCapitalCity()) {
                            manager.increaseSoldiers(i, j);
                        }
                    }
                }

            }
        } catch (InterruptedException e) {
            System.out.println("SoldierIncreaser1 ended");
        }
    }
}
