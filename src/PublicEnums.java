
enum CarsEvent {
    ShabatMode, RegularMode, TurnGreen, TurnRed, BlinkOnTimeout, RedStTimeout, WLRTimeout, BlinkOffTimeout, ROTimeout
}

enum WalkersLightEvent {
    TurnRed, TurnGreen, ShabatMode, RegularMode, TempTimeout
}

/**
 * events for the main statechart
 */
enum MainSCEvent {
    ShabatButtonPress,
    //timeout for transition inside the statechart
    YATimeout, KNTimeout, FTimeout,
    //Button press
    YAPress, KNPress, FPress
}