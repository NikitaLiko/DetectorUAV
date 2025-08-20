package ru.liko.dronedetector.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import ru.liko.dronedetector.common.ServerValues;

import java.util.function.Supplier;

public record S2CServerSettings(double range) {
    public static void encode(S2CServerSettings msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.range);
    }
    public static S2CServerSettings decode(FriendlyByteBuf buf) {
        return new S2CServerSettings(buf.readDouble());
    }
    public static void handle(S2CServerSettings msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context c = ctx.get();
        c.enqueueWork(() -> ServerValues.setRange(msg.range()));
        c.setPacketHandled(true);
    }
}
