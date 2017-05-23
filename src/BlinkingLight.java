import java.awt.Color;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author �����
 */
class BlinkingLight extends Thread {
    private final long Delay = 700;
    StreetLight streetLight;
    JPanel panel;
    public BlinkingLight(StreetLight streetLight, JPanel panel) {
        this.streetLight = streetLight;
        this.panel = panel;
    }

    public void run() {
        State state = State.Blank;
        //noinspection InfiniteLoopStatement
        while (true) {
            switch (state) {
                case Blank:
                    try {
                        sleep(Delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setLight(1, Color.ORANGE);
                    state = State.Orange;
                    break;
                case Orange:
                    try {
                        sleep(Delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setLight(1, Color.GRAY);
                    state = State.Blank;
                    break;
            }
        }

    }

    public void setLight(int place, Color color) {
        streetLight.colorLight[place - 1] = color;
        panel.repaint();
    }

    enum State {Orange, Blank}
}
