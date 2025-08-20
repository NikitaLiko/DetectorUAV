package ru.liko.dronedetector.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import ru.liko.dronedetector.net.DDNetwork;
import ru.liko.dronedetector.net.S2CServerSettings;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ServerEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp) {
            DDNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp),
                    new S2CServerSettings(ServerConfig.RANGE.get()));
        }
    }

    @SubscribeEvent
    public static void onServerConfigReload(ModConfigEvent.Reloading e) {
        if (e.getConfig().getType() != ModConfig.Type.SERVER) return;
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        var pkt = new S2CServerSettings(ServerConfig.RANGE.get());
        for (ServerPlayer sp : server.getPlayerList().getPlayers()) {
            DDNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), pkt);
        }
    }

    private ServerEvents() {}
}
