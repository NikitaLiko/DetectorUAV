package ru.liko.dronedetector.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class DroneDetectorConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;
    static {
        Pair<Client, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = pair.getLeft();
        CLIENT_SPEC = pair.getRight();
    }

    public static class Client {

        public final ForgeConfigSpec.DoubleValue radius;
        public final ForgeConfigSpec.DoubleValue verticalRadius;
        public final ForgeConfigSpec.BooleanValue requireLineOfSight;

        public final ForgeConfigSpec.IntValue baseBeepIntervalTicks;
        public final ForgeConfigSpec.IntValue minBeepIntervalTicks;
        public final ForgeConfigSpec.IntValue cooldownAfterBeep;
        public final ForgeConfigSpec.DoubleValue minPitch;
        public final ForgeConfigSpec.DoubleValue maxPitch;
        public final ForgeConfigSpec.DoubleValue volume;

        public final ForgeConfigSpec.BooleanValue detectOnlyFromNamespace;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> matchEntityIds;

        public final ForgeConfigSpec.IntValue blockPulseTicks;

        public Client(ForgeConfigSpec.Builder b) {
            b.push("detection");
            radius = b.defineInRange("radius", 128.0, 4.0, 1024.0);
            verticalRadius = b.defineInRange("verticalRadius", 64.0, 2.0, 512.0);
            requireLineOfSight = b.define("requireLineOfSight", false);
            detectOnlyFromNamespace = b.define("detectOnlyFromNamespace", true);
            matchEntityIds = b.defineList("matchEntityIds", List.of("drone","uav","quad","fpv"), o -> o instanceof String);
            b.pop();

            b.push("beep");
            baseBeepIntervalTicks = b.defineInRange("baseBeepIntervalTicks", 24, 2, 200);
            minBeepIntervalTicks = b.defineInRange("minBeepIntervalTicks", 6, 1, 200);
            cooldownAfterBeep = b.defineInRange("cooldownAfterBeep", 2, 0, 40);
            minPitch = b.defineInRange("minPitch", 0.7, 0.1, 3.0);
            maxPitch = b.defineInRange("maxPitch", 1.8, 0.1, 3.0);
            volume = b.defineInRange("volume", 0.8, 0.0, 3.0);
            b.pop();

            b.push("block");
            blockPulseTicks = b.defineInRange("blockPulseTicks", 40, 5, 400);
            b.pop();
        }
    }
}