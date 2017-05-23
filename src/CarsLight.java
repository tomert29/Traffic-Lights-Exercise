import java.awt.Color;
import java.lang.reflect.Array;
import java.util.*;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author �����
 */


public class CarsLight extends Thread {
    StreetLight streetLight;
    JPanel panel;
    List<WalkersLight> WalkersLights;
    Event64 eventReceiver = new Event64();
    private boolean carsShouldStop = true;


    //region

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


    //region
    public void run() {
        ExternalState state = ExternalState.ShabatMode;
        while (true) {
            switch (state) {
                case ShabatMode:
                    startShabatMode(Arrays.asList(CarsEvent.RegularMode));
                    state = ExternalState.RegularMode;
                    break;
                case RegularMode:
                    startRegulerMode(Arrays.asList(CarsEvent.ShabatMode));
                    break;
            }

        }


    }

    private CarsEvent startRegulerMode(List<CarsEvent> exitEvents) {
        CarsEvent event;
        Timer timer = new Timer();
        MidState state = MidState.Red;
        do {
            switch (state) {
                case Green:
                    //TODO
                    break;
                case Red:
                    //TODO
                    break;
            }
            event = (CarsEvent) eventReceiver.waitEvent();
        } while (!exitEvents.contains(event));
        return event;
    }

    private CarsEvent startShabatMode(List<CarsEvent> exitEvents) {
        CarsEvent event;
        Timer timer = new Timer();
        ShabatState state = ShabatState.BlinkOn;
        for (WalkersLight WL : WalkersLights) {
            WL.setLight(1, Color.GRAY);
            WL.setLight(2, Color.GRAY);
        }
        setLight(1, Color.GRAY);
        setLight(2, Color.ORANGE);
        setLight(3, Color.GRAY);
        do {
            switch (state) {
                case BlinkOn:
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            sendEvent(CarsEvent.Timeout1);
                        }
                    }, 1000);
                    event = (CarsEvent) eventReceiver.waitEvent();
                    setLight(2, Color.GRAY);
                    state = ShabatState.BlinkOff;
                    break;
                case BlinkOff:
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            sendEvent(CarsEvent.Timeout1);
                        }
                    }, 1000);
                    event = (CarsEvent) eventReceiver.waitEvent();
                    setLight(2, Color.ORANGE);
                    state = ShabatState.BlinkOn;
                    break;
            }
            event = (CarsEvent) eventReceiver.waitEvent();
        } while (!exitEvents.contains(event));
        return event;
    }

    private CarsEvent startRedState(List<CarsEvent> exitEvents) {
        CarsEvent event = (CarsEvent) eventReceiver.waitEvent();
        //TODO
        return event;
    }

    public void sendEvent(CarsEvent event) {
        eventReceiver.sendEvent(event);
    }

    public void setLight(int place, Color color) {
        streetLight.colorLight[place - 1] = color;
        panel.repaint();
    }


    public boolean shouldCarsStop() {
        return carsShouldStop;
    }

    //region Statecharts' enums
    private enum ExternalState {
        RegularMode, ShabatMode
    }

    private enum MidState {Red, Green}

    private enum ShabatState {BlinkOn, BlinkOff}

    private enum InnerState {WalkersLightsRed, RedOrange, Green, Red, Orange}
    //endregion

}
