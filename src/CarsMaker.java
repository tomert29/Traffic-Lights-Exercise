import javax.swing.JPanel;

/*
 * Created on Tevet 5770 
 */

/**
 * @author �����
 */

public class CarsMaker extends Thread {
    JPanel myPanel;
    int key;
    private CarsLight myRamzor;

    public CarsMaker(JPanel myPanel, CarsLight myRamzor, int key) {
        this.myPanel = myPanel;
        this.myRamzor = myRamzor;
        this.key = key;
        setDaemon(true);
        start();
    }

    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                sleep(300);
                if (!myRamzor.shouldCarsStop()) {
                    new CarMoving(myPanel, myRamzor, key);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
