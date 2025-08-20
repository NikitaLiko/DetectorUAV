package ru.liko.dronedetector.common;

public final class ServerValues {
    private static volatile double range = -1.0;

    public static void setRange(double r) { range = r; }
    public static void reset() { range = -1.0; }

    /** Если сервер ещё не прислал значения, используем локальный SERVER-конфиг (SP/локальный мир). */
    public static double getRange() {
        return range > 0 ? range : ServerConfig.RANGE.get();
    }

    private ServerValues() {}
}
