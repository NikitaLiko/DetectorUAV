package ru.liko.dronedetector.client;

import ru.liko.dronedetector.config.DroneDetectorConfig;
import ru.liko.dronedetector.registry.ModItems;
import ru.liko.dronedetector.util.DroneFinder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {
    private static int tickCounter = 0;
    private static int cooldown = 0;
    private static int lastBarTick = -999;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        var mc = Minecraft.getInstance();
        if (mc.isPaused() || mc.player == null || mc.level == null) return;

        tickCounter++;
        if (cooldown > 0) cooldown--;

        // --- предмет должен быть ИМЕННО В РУКЕ ---
        ItemStack held = ItemStack.EMPTY;
        ItemStack mh = mc.player.getMainHandItem();
        ItemStack oh = mc.player.getOffhandItem();
        if (mh.is(ModItems.DRONE_DETECTOR.get())) held = mh;
        else if (oh.is(ModItems.DRONE_DETECTOR.get())) held = oh;

        // если не держим предмет — ничего не рисуем и не пищим
        if (held.isEmpty()) return;

        boolean active = held.hasTag() && held.getTag().getBoolean("active");
        // если в руке, но выключен — ничего не рисуем/не пищим
        if (!active) return;

        final int BAR_UPDATE_EVERY = 5; // ~4 раза в секунду
        if (tickCounter - lastBarTick < BAR_UPDATE_EVERY) return;

        double radius  = DroneDetectorConfig.CLIENT.radius.get();
        double vRadius = DroneDetectorConfig.CLIENT.verticalRadius.get();

        var nearest = DroneFinder.findNearestDrone(
                mc.level, mc.player.position(), radius, vRadius,
                DroneDetectorConfig.CLIENT.requireLineOfSight.get()
        );

        if (nearest == null) {
            // активен, но цели в зоне нет — показываем пустую шкалу ("нет цели")
            showProximityBar(mc, 0.0, -1.0);
            lastBarTick = tickCounter;
            return;
        }

        double dist = Math.sqrt(nearest.distanceToSqr(mc.player.position()));
        double closeness = Mth.clamp(1.0 - (dist / Math.max(1.0, radius)), 0.0, 1.0); // 0..1
        showProximityBar(mc, closeness, dist);
        lastBarTick = tickCounter;

        // Писк: ближе -> чаще/выше
        int base = DroneDetectorConfig.CLIENT.baseBeepIntervalTicks.get();
        int minI = DroneDetectorConfig.CLIENT.minBeepIntervalTicks.get();
        int interval = Math.max(minI, (int)(base * (1.0 - closeness)));
        if (tickCounter % interval == 0 && cooldown == 0) {
            float minPitch = DroneDetectorConfig.CLIENT.minPitch.get().floatValue();
            float maxPitch = DroneDetectorConfig.CLIENT.maxPitch.get().floatValue();
            float pitch = (float) Mth.clampedLerp(minPitch, maxPitch, (float) closeness);
            mc.player.playSound(SoundEvents.NOTE_BLOCK_PLING.value(),
                    DroneDetectorConfig.CLIENT.volume.get().floatValue(), pitch);
            cooldown = DroneDetectorConfig.CLIENT.cooldownAfterBeep.get();
        }
    }

    /** Рисует цветную шкалу близости в actionbar. */
    private static void showProximityBar(Minecraft mc, double closeness, double distMeters) {
        if (mc.player == null) return;

        final int SEGMENTS = 20;
        int filled = Mth.clamp((int)Math.round(closeness * SEGMENTS), 0, SEGMENTS);

        MutableComponent msg = Component.literal("").withStyle(ChatFormatting.WHITE);
        msg.append(Component.literal("["));
        for (int i = 0; i < SEGMENTS; i++) {
            if (i < filled) {
                double pos = (i + 1) / (double) SEGMENTS;
                ChatFormatting color = (pos < 0.40) ? ChatFormatting.GREEN
                        : (pos < 0.75) ? ChatFormatting.YELLOW
                        : ChatFormatting.RED;
                msg.append(Component.literal("|").withStyle(color));
            } else {
                msg.append(Component.literal("|").withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        msg.append(Component.literal("] "));

        if (distMeters < 0) {
            msg.append(Component.literal("нет цели").withStyle(ChatFormatting.GRAY));
        } else {
            int m = Math.max(1, (int)Math.round(distMeters));
            msg.append(Component.literal(m + "м").withStyle(ChatFormatting.GRAY));
        }

        mc.player.displayClientMessage(msg, true);
    }
}
