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
    }

    public void run() {
        State state = State.Gray;
        //noinspection InfiniteLoopStatement
        while (true) {
            switch (state) {
                case Gray:
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setLight(1, Color.ORANGE);
                    break;
                case Orange:
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setLight(1, Color.GRAY);
                    break;
            }
        }

    }

    public void setLight(int place, Color color) {
        streetLight.colorLight[place - 1] = color;
        panel.repaint();
    }

    enum State {Orange, Gray}
}
