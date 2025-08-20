package ru.liko.dronedetector.client;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class ClientConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;
    private static final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

    // Аудио
    public static final ForgeConfigSpec.BooleanValue ONLY_NEAREST_BEEP;
    public static final ForgeConfigSpec.IntValue BEEP_MIN_PERIOD;
    public static final ForgeConfigSpec.IntValue BEEP_MAX_PERIOD;
    public static final ForgeConfigSpec.DoubleValue BEEP_VOLUME;
    public static final ForgeConfigSpec.DoubleValue BEEP_PITCH_MIN;
    public static final ForgeConfigSpec.DoubleValue BEEP_PITCH_MAX;

    // HUD
    public static final ForgeConfigSpec.BooleanValue HUD_ENABLED;
    public static final ForgeConfigSpec.IntValue HUD_LIST_COUNT;
    public static final ForgeConfigSpec.BooleanValue HUD_ANCHOR_RIGHT;
    public static final ForgeConfigSpec.IntValue HUD_X;
    public static final ForgeConfigSpec.IntValue HUD_Y;

    static {
        b.push("audio");
        ONLY_NEAREST_BEEP = b.define("onlyNearestBeep", true);
        BEEP_MIN_PERIOD   = b.defineInRange("beepMinPeriod", 6, 1, 200);
        BEEP_MAX_PERIOD   = b.defineInRange("beepMaxPeriod", 40, 2, 400);
        BEEP_VOLUME       = b.defineInRange("beepVolume", 0.8, 0.0, 2.0);
        BEEP_PITCH_MIN    = b.defineInRange("beepPitchMinFar", 0.7, 0.5, 2.0);
        BEEP_PITCH_MAX    = b.defineInRange("beepPitchMaxNear", 1.8, 0.5, 3.0);
        b.pop();

        b.push("hud");
        HUD_ENABLED      = b.define("enabled", true);
        HUD_LIST_COUNT   = b.defineInRange("listCount", 5, 1, 12);
        HUD_ANCHOR_RIGHT = b.define("anchorRight", true);
        HUD_X            = b.defineInRange("x", 8, 0, 10000);
        HUD_Y            = b.defineInRange("y", 8, 0, 10000);
        b.pop();

        CLIENT_SPEC = b.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
    }

    private ClientConfig() {}
}
