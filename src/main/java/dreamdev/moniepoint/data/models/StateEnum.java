package dreamdev.moniepoint.data.models;

import java.util.Arrays;

public enum StateEnum {
    ABIA("Abia"),
    ADAMAWA("Adamawa"),
    AKWA_IBOM("Akwa Ibom"),
    ANAMBRA("Anambra"),
    BAUCHI("Bauchi"),
    BAYELSA("Bayelsa"),
    BENUE("Benue"),
    BORNO("Borno"),
    CROSS_RIVER("Cross River"),
    DELTA("Delta"),
    EBONYI("Ebonyi"),
    EDO("Edo"),
    EKITI("Ekiti"),
    ENUGU("Enugu"),
    FCT_ABUJA("FCT Abuja"),
    GOMBE("Gombe"),
    IMO("Imo"),
    JIGAWA("Jigawa"),
    KADUNA("Kaduna"),
    KANO("Kano"),
    KATSINA("Katsina"),
    KEBBI("Kebbi"),
    KOGI("Kogi"),
    KWARA("Kwara"),
    LAGOS("Lagos"),
    NASARAWA("Nasarawa"),
    NIGER("Niger"),
    OGUN("Ogun"),
    ONDO("Ondo"),
    OSUN("Osun"),
    OYO("Oyo"),
    PLATEAU("Plateau"),
    RIVERS("Rivers"),
    SOKOTO("Sokoto"),
    TARABA("Taraba"),
    YOBE("Yobe"),
    ZAMFARA("Zamfara");

    private final String displayName;

    StateEnum(String displayName) {
        this.displayName = displayName;
    }

    public static boolean isValidState(String stateName) {
        for (StateEnum state : StateEnum.values()) {
            if (state.name().equalsIgnoreCase(stateName)) {
                return true;
            }
        }
        return false;
    }
}