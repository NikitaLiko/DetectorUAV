package ru.liko.dronedetector.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class ServerConfig {
    public static final ForgeConfigSpec SERVER_SPEC;
    private static final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue RANGE;

    static {
        b.push("detector");
        RANGE = b.comment("Радиус поиска дронов, метры (задаёт сервер)")
                .defineInRange("range", 64.0, 8.0, 256.0);
        b.pop();
        SERVER_SPEC = b.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }

    private ServerConfig() {}
}
