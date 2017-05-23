import java.awt.Color;
import java.util.*;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770
 * Modified by Ezra Steinmetz on May 2017
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

    /**
     * runs the statechart associated with the Cars' Light object
     */
    public void run() {
        ExternalState state = ExternalState.ShabatMode;
        while (true) {
            switch (state) {
                case ShabatMode:
                    runShabatMode(Arrays.asList(CarsEvent.RegularMode));
                    state = ExternalState.RegularMode;
                    break;
                case RegularMode:
                    startRegularMode(Arrays.asList(CarsEvent.ShabatMode));
                    break;
            }

        }


    }


    /**
     * the function runs the sub-statechart of the state 'regular mode'
     *
     * @param exitEvents the events on which the sub-statechart should exit
     * @return the last event received which caused the sub-statechart to exit
     */
    private CarsEvent startRegularMode(List<CarsEvent> exitEvents) {
        CarsEvent event = null;
        RegularSubState state = RegularSubState.Red;
        List<CarsEvent> temp;
        do {
            switch (state) {
                case Green:
                    temp = new ArrayList<>(exitEvents);
                    temp.add(CarsEvent.TurnRed);
                    event = runRedState(temp);
                    if (event == CarsEvent.TurnRed) {
                        state = RegularSubState.Red;
                    }
                    break;
                case Red:
                    temp = new ArrayList<>(exitEvents);
                    temp.add(CarsEvent.TurnGreen);
                    event = runGreenState(temp);
                    if (event == CarsEvent.TurnGreen) {
                        state = RegularSubState.Green;
                    }
                    break;
            }
        } while (!exitEvents.contains(event));
        return event;
    }

    /**
     * the function runs the sub-statechart of the state 'shabat mode'
     *
     * @param exitEvents the events on which the sub-statechart should exit
     * @return the last event received which caused the sub-statechart to exit
     */
    private CarsEvent runShabatMode(List<CarsEvent> exitEvents) {
        CarsEvent event = null;
        Timer timer = new Timer();
        ShabatSubState state = ShabatSubState.BlinkOn;
        for (WalkersLight WL : WalkersLights) {
            WL.setLight(1, Color.GRAY);
            WL.setLight(2, Color.GRAY);
        }
        setLight(LightMode.Orange);
        do {
            switch (state) {
                case BlinkOn:
                    event = (CarsEvent) eventReceiver.waitEvent();
                    if (event == CarsEvent.BlinkOnTimeout) {
                        setLight(LightMode.Blank);
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendEvent(CarsEvent.BlinkOffTimeout);
                            }
                        }, 1000);
                        state = ShabatSubState.BlinkOff;
                    }

                    break;
                case BlinkOff:
                    event = (CarsEvent) eventReceiver.waitEvent();
                    if (event == CarsEvent.BlinkOffTimeout) {
                        setLight(LightMode.Orange);
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendEvent(CarsEvent.BlinkOnTimeout);
                            }
                        }, 1000);
                        state = ShabatSubState.BlinkOn;
                    }
                    break;
            }
        } while (!exitEvents.contains(event));
        return event;
    }


    /**
     * the function runs the sub-statechart of the state 'Red' (which is a sub-state of 'regular mode'
     *
     * @param exitEvents the events on which the sub-statechart should exit
     * @return the last event received (which caused the sub-statechart to exit)
     */
    private CarsEvent runRedState(List<CarsEvent> exitEvents) {
        CarsEvent event = null;
        Timer timer = new Timer();

        setLight(LightMode.Orange);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendEvent(CarsEvent.RedStTimeout);
            }
        }, 1000);
        RedSubState subState = RedSubState.Orange;
        do {
            switch (subState) {
                case Orange:
                    event = (CarsEvent) eventReceiver.waitEvent();
                    if (event == CarsEvent.RedStTimeout) {
                        setLight(LightMode.Red);
                        for (WalkersLight WL : WalkersLights) {
                            WL.sendEvent(WalkersLightEvent.TurnGreen);
                        }
                        subState = RedSubState.Red;
                    }
                    break;
                case Red:
                    event = (CarsEvent) eventReceiver.waitEvent();
                    break;
            }
        } while (!exitEvents.contains(event));
        return event;
    }

    private CarsEvent runGreenState(List<CarsEvent> exitEvents) {
        CarsEvent event = null;
        Timer timer = new Timer();
        for (WalkersLight WL : WalkersLights) {
            WL.sendEvent(WalkersLightEvent.TurnRed);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendEvent(CarsEvent.WLRTimeout);
            }
        }, 1000);

        GreenSubState subState = GreenSubState.WalkersLightsRed;
        do {
            switch (subState) {
                case WalkersLightsRed:
                    event = (CarsEvent) eventReceiver.waitEvent();
                    if (event == CarsEvent.WLRTimeout) {
                        setLight(LightMode.RedOrange);
                        subState = GreenSubState.RedOrange;
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendEvent(CarsEvent.ROTimeout);
                            }
                        }, 1000);
                    }
                    break;
                case RedOrange:
                    event = (CarsEvent) eventReceiver.waitEvent();
                    if (event == CarsEvent.ROTimeout) {
                        setLight(LightMode.Green);
                        subState = GreenSubState.Green;
                    }
                    break;
                case Green:
                    event = (CarsEvent) eventReceiver.waitEvent();
                    break;
            }
        } while (!exitEvents.contains(event));
        return event;
    }

    /**
     * sends event to the current instance's statechart
     * @param event - the event to send to the statechart
     */
    public void sendEvent(CarsEvent event) {
        eventReceiver.sendEvent(event);
    }

    ///sets the cars' light according to the given mode
    public void setLight(LightMode mode) {
        switch (mode) {
            case Red:
                streetLight.colorLight[0] = Color.RED;
                streetLight.colorLight[1] = Color.GRAY;
                streetLight.colorLight[2] = Color.GRAY;
                carsShouldStop = true;
                break;
            case Orange:
                streetLight.colorLight[0] = Color.GRAY;
                streetLight.colorLight[1] = Color.ORANGE;
                streetLight.colorLight[2] = Color.GRAY;
                carsShouldStop = true;
            case RedOrange:
                streetLight.colorLight[0] = Color.RED;
                streetLight.colorLight[1] = Color.ORANGE;
                streetLight.colorLight[2] = Color.GRAY;
                carsShouldStop = true;
                break;
            case Green:
                streetLight.colorLight[0] = Color.GRAY;
                streetLight.colorLight[1] = Color.GRAY;
                streetLight.colorLight[2] = Color.GREEN;
                carsShouldStop = false;
                break;
            case Blank:
                streetLight.colorLight[0] = Color.GRAY;
                streetLight.colorLight[1] = Color.GRAY;
                streetLight.colorLight[2] = Color.GRAY;
                carsShouldStop = false;
                break;
        }
        panel.repaint();
    }


    public boolean shouldCarsStop() {
        return carsShouldStop;
    }

    //region Statecharts' enums

    /**
     * enums that represents states inside the statechart, grouped by sub-states
     */
    private enum ExternalState {
        RegularMode, ShabatMode
    }

    private enum RegularSubState {Red, Green}

    private enum ShabatSubState {BlinkOn, BlinkOff}

    private enum RedSubState {Red, Orange}

    private enum GreenSubState {WalkersLightsRed, RedOrange, Green}
    //endregion

    /**
     * represents the cars' light mode
     */
    private enum LightMode {
        Green, Red, RedOrange, Orange, Blank}

}
