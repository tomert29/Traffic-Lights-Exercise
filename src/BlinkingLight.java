import java.awt.Color;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author �����
 */
class BlinkingLight extends Thread {
    StreetLight streetLight;
    JPanel panel;

    public BlinkingLight(StreetLight streetLight, JPanel panel) {
        this.streetLight = streetLight;
        this.panel = panel;
        start();
    }

    public void run() {
        try {
            while (true) {
                sleep(1000);
                setLight(1, Color.GRAY);
                sleep(1000);
                setLight(1, Color.YELLOW);
            }
        } catch (InterruptedException e) {
        }

    }

    public void setLight(int place, Color color) {
        streetLight.colorLight[place - 1] = color;
        panel.repaint();
    }
}
