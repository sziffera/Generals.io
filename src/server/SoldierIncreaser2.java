package server;

public class SoldierIncreaser2 extends Thread {

    private Manager manager;

    SoldierIncreaser2(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {

        try {
        while (!interrupted()) {

            sleep(30000);
            for (int i = 0; i < manager.getRows(); i++) {
                for (int j = 0; j < manager.getColumns(); j++) {
                    if (manager.getFieldTypes()[i][j] == manager.getSoldierField()) {
                        manager.increaseSoldiers(i, j);
                    }
                }
            }
        }
        } catch (InterruptedException e) {
            System.out.println("SoldierIncreaser2 ended");
        }

    }
}
