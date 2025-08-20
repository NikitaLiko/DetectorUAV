package ru.liko.dronedetector.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.liko.dronedetector.DDConstants;

public final class DDNetwork {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DDConstants.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(
                id++,
                S2CServerSettings.class,
                S2CServerSettings::encode,
                S2CServerSettings::decode,
                S2CServerSettings::handle
        );
    }

    private DDNetwork() {}
}
