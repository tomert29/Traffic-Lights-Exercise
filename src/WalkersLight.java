import java.awt.Color;
import java.util.*;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author �����
 */


class WalkersLight extends Thread {
    Event64 externelEventListener = new Event64(), eventListener = new Event64();
    WalkersLight.ExternalState externalState;
    StreetLight streetLight;
    JPanel panel;

    public WalkersLight(StreetLight streetLight, JPanel panel) {
        this.streetLight = streetLight;
        this.panel = panel;
        start();
    }

    public void run() {
        externalState = ExternalState.RegularMode;
        while (true) {
            switch (externalState) {
                case ShabatMode:
                    setLight(1, Color.GRAY);
                    setLight(2, Color.GRAY);
                    while (externelEventListener.waitEvent() != WalkersLightEvent.RegularMode) ;
                    break;
                case RegularMode:
                    regularMode(Arrays.asList(WalkersLightEvent.ShabatMode));
                    externalState = ExternalState.ShabatMode;
                    break;
            }
        }

    }

    WalkersLightEvent regularMode(List<WalkersLightEvent> exitEvents) {
        RegulerModeState currentState = RegulerModeState.Red;
        WalkersLightEvent event = null;
        Timer timer = new Timer();
        do {
            switch (currentState) {
                case Red:
                    event = (WalkersLightEvent) eventListener.waitEvent();
                    if (event == WalkersLightEvent.TurnGreen) {
                        currentState = RegulerModeState.Temp;
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendEvent(WalkersLightEvent.TempTimeout);
                            }
                        }, 1000);
                    }
                    break;
                case Temp:
                    event = (WalkersLightEvent) eventListener.waitEvent();
                    switch (event) {
                        case TempTimeout:
                            setLight(1, Color.LIGHT_GRAY);
                            setLight(2, Color.GREEN);
                            currentState = RegulerModeState.Green;
                            break;
                        case TurnRed:
                            currentState = RegulerModeState.Red;
                    }
                    break;
                case Green:
                    event = (WalkersLightEvent) eventListener.waitEvent();
                    if (event == WalkersLightEvent.TurnRed) {
                        currentState = RegulerModeState.Red;
                        setLight(1, Color.RED);
                        setLight(2, Color.LIGHT_GRAY);
                    }
                    break;
            }
        } while (!exitEvents.contains(event));
        return event;
    }

    public void sendEvent(WalkersLightEvent event) {
        eventListener.sendEvent(event);
    }

    public void setLight(int place, Color color) {
        streetLight.colorLight[place - 1] = color;
        panel.repaint();
    }

    enum RegulerModeState {Green, Red, Temp}

    enum ExternalState {RegularMode, ShabatMode}
}
