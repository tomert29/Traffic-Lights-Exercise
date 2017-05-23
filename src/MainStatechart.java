import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by User on 22-May-17.
 */
public class MainStatechart extends Thread {

    CarsLight YeminAvotLight, KanfeiNesharimLight, KNSideLight, FarbsteinLight;
    List<WalkersLight> independentLights;
    Event64 eventReciver;

    public MainStatechart(CarsLight yeminAvotLight, CarsLight kanfeiNesharimLight, CarsLight farbsteinLight, List<WalkersLight> independentLights) {
        YeminAvotLight = yeminAvotLight;
        KanfeiNesharimLight = kanfeiNesharimLight;
        FarbsteinLight = farbsteinLight;
        this.independentLights = independentLights;
        YeminAvotLight.run();
        KanfeiNesharimLight.run();
        FarbsteinLight.run();
        for (WalkersLight WL : independentLights) {
            WL.run();
        }
    }

    public void run() {
        ExternalState state = ExternalState.ShabatMode;
        MainSCEvent event = null;
        while (true) {
            switch (state) {
                case RegularMode:
                    event = runRegularMode(Arrays.asList(MainSCEvent.ShabatButtonPress));
                    if (event == MainSCEvent.ShabatButtonPress) {
                        YeminAvotLight.sendEvent(CarsEvent.ShabatMode);
                        KanfeiNesharimLight.sendEvent(CarsEvent.ShabatMode);
                        FarbsteinLight.sendEvent(CarsEvent.ShabatMode);
                        for (WalkersLight WL : independentLights) {
                            WL.sendEvent(WalkersLightEvent.ShabatMode);
                        }
                        state = ExternalState.ShabatMode;
                    }
                    break;
                case ShabatMode:
                    event = (MainSCEvent) eventReciver.waitEvent();
                    if (event == MainSCEvent.ShabatButtonPress) {
                        YeminAvotLight.sendEvent(CarsEvent.RegularMode);
                        KanfeiNesharimLight.sendEvent(CarsEvent.RegularMode);
                        FarbsteinLight.sendEvent(CarsEvent.RegularMode);
                        for (WalkersLight WL : independentLights) {
                            WL.sendEvent(WalkersLightEvent.RegularMode);
                        }
                        state = ExternalState.RegularMode;
                    }
                    break;
            }
        }
    }

    public void sendEvent(MainSCEvent event) {
        eventReciver.sendEvent(event);
    }

    private MainSCEvent runRegularMode(List<MainSCEvent> exitEvents) {
        Timer timer = new Timer();
        MainSCEvent event = null;
        RegularSubState state = RegularSubState.KanfeiNesharim;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendEvent(MainSCEvent.KNTimeout);
            }
        }, 30000);
        do {
            switch (state) {
                case KanfeiNesharim:
                    event = (MainSCEvent) eventReciver.waitEvent();
                    if (event == MainSCEvent.KNTimeout || event == MainSCEvent.KNPress) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendEvent(MainSCEvent.YATimeout);
                            }
                        }, 30000);
                        YeminAvotLight.sendEvent(CarsEvent.TurnGreen);
                        KanfeiNesharimLight.sendEvent(CarsEvent.TurnRed);
                        KNSideLight.sendEvent(CarsEvent.TurnRed);
                        for (WalkersLight WL : independentLights) {
                            WL.sendEvent(WalkersLightEvent.TurnRed);
                        }
                        state = RegularSubState.YeminAvot;
                    }
                    break;
                case YeminAvot:
                    event = (MainSCEvent) eventReciver.waitEvent();
                    if (event == MainSCEvent.YATimeout || event == MainSCEvent.YAPress) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendEvent(MainSCEvent.FTimeout);
                            }
                        }, 30000);
                        YeminAvotLight.sendEvent(CarsEvent.TurnRed);
                        KNSideLight.sendEvent(CarsEvent.TurnGreen);
                        FarbsteinLight.sendEvent(CarsEvent.TurnGreen);
                        state = RegularSubState.Farbstein;
                    }
                    break;
                case Farbstein:
                    event = (MainSCEvent) eventReciver.waitEvent();
                    if (event == MainSCEvent.FPress || event == MainSCEvent.FTimeout) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendEvent(MainSCEvent.KNTimeout);
                            }
                        }, 30000);
                        FarbsteinLight.sendEvent(CarsEvent.TurnRed);
                        KanfeiNesharimLight.sendEvent(CarsEvent.TurnGreen);
                        for (WalkersLight WL : independentLights) {
                            WL.sendEvent(WalkersLightEvent.TurnGreen);
                        }
                        state = RegularSubState.KanfeiNesharim;
                    }
                    break;
            }
        } while (!exitEvents.contains(event));
        return event;
    }


    /**
     * states of the regular sub statechart, the states are called after the street which has a cars' green light
     * when the state is active
     */
    private enum RegularSubState {
        KanfeiNesharim, YeminAvot, Farbstein
    }

    private enum ExternalState {RegularMode, ShabatMode}
}
