import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author �����
 */

enum CarsEvent {
    ShabatMode, RegulerMode, TurnGreen, TurnRed
}

public class CarsLight extends Thread {
    StreetLight streetLight;
    JPanel panel;
    List<WalkersLight> WalkersLights;
    Event64 externalEventReciver = new Event64(),
            internalEventReciver = new Event64();
    private boolean stop = true;

    public CarsLight(StreetLight streetLight, JPanel panel, int key, List<WalkersLight> WalkersLights) {
        this.streetLight = streetLight;
        this.panel = panel;
        this.WalkersLights = WalkersLights;
//		new CarsMaker(panel,this,key);
        start();
    }

    public CarsLight(StreetLight streetLight, JPanel panel, int key) {
        this(streetLight, panel, key, new ArrayList<WalkersLight>());
    }

    public void run() {
        try {
            while (true) {
                sleep(1000);
                setLight(2, Color.YELLOW);
                sleep(1000);
                setLight(1, Color.LIGHT_GRAY);
                setLight(2, Color.LIGHT_GRAY);
                setLight(3, Color.GREEN);
                stop = false;
                sleep(3000);
                stop = true;
                setLight(1, Color.LIGHT_GRAY);
                setLight(2, Color.YELLOW);
                setLight(3, Color.LIGHT_GRAY);
                sleep(1000);
                setLight(1, Color.RED);
                setLight(2, Color.LIGHT_GRAY);
                setLight(3, Color.LIGHT_GRAY);
            }
        } catch (InterruptedException e) {
        }

    }

    public void sendEvent(CarsEvent event) {
        internalEventReciver.sendEvent(event);
        internalEventReciver.sendEvent(event);
    }

    public void setLight(int place, Color color) {
        streetLight.colorLight[place - 1] = color;
        panel.repaint();
    }

    public boolean isStop() {
        return stop;
    }
}
