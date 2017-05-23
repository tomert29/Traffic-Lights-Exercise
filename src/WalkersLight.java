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
    Event64 eventReciver = new Event64();
    WalkersLight.ExternalState externalState;
    StreetLight streetLight;
    JPanel panel;

    public WalkersLight(StreetLight streetLight, JPanel panel) {
        this.streetLight = streetLight;
        this.panel = panel;
    }

    public void run() {
        externalState = ExternalState.RegularMode;
        //noinspection InfiniteLoopStatement
        while (true) {
            switch (externalState) {
                case ShabatMode:
                    setLight(LightMode.Blank);
                    while (eventReciver.waitEvent() != WalkersLightEvent.RegularMode) ;
                    break;
                case RegularMode:
                    runRegularMode(Collections.singletonList(WalkersLightEvent.ShabatMode));
                    externalState = ExternalState.ShabatMode;
                    break;
            }
        }

    }

    WalkersLightEvent runRegularMode(List<WalkersLightEvent> exitEvents) {
        setLight(LightMode.Red);
        RegularModeState currentState = RegularModeState.Red;
        WalkersLightEvent event = null;
        Timer timer = new Timer();
        do {
            switch (currentState) {
                case Red:
                    event = (WalkersLightEvent) eventReciver.waitEvent();
                    if (event == WalkersLightEvent.TurnGreen) {
                        currentState = RegularModeState.Temp;
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendEvent(WalkersLightEvent.TempTimeout);
                            }
                        }, 1000);
                    }
                    break;
                case Temp:
                    event = (WalkersLightEvent) eventReciver.waitEvent();
                    switch (event) {
                        case TempTimeout:
                            setLight(LightMode.Green);
                            currentState = RegularModeState.Green;
                            break;
                        case TurnRed:
                            currentState = RegularModeState.Red;
                    }
                    break;
                case Green:
                    event = (WalkersLightEvent) eventReciver.waitEvent();
                    if (event == WalkersLightEvent.TurnRed) {
                        currentState = RegularModeState.Red;
                        setLight(LightMode.Red);
                    }
                    break;
            }
        } while (!exitEvents.contains(event));
        return event;
    }

    public void sendEvent(WalkersLightEvent event) {
        eventReciver.sendEvent(event);
    }

    public void setLight(LightMode mode) {
        switch (mode) {
            case Red:
                streetLight.colorLight[0] = Color.RED;
                streetLight.colorLight[1] = Color.GRAY;
                break;
            case Green:
                streetLight.colorLight[0] = Color.GRAY;
                streetLight.colorLight[1] = Color.GREEN;
                break;
            case Blank:
                streetLight.colorLight[0] = Color.GRAY;
                streetLight.colorLight[1] = Color.GRAY;
                break;
        }
        panel.repaint();
    }

    enum RegularModeState {Green, Red, Temp}

    enum ExternalState {RegularMode, ShabatMode}

    enum LightMode {Red, Green, Blank}
}
