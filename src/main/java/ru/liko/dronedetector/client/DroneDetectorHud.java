package ru.liko.dronedetector.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public final class DroneDetectorHud {

    public static final IGuiOverlay OVERLAY = (gui, gfx, pt, sw, sh) -> {
        if (!ClientConfig.HUD_ENABLED.get()) return;
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        var targets = DroneTracker.INSTANCE.getSnapshotLimited(ClientConfig.HUD_LIST_COUNT.get());
        if (targets.isEmpty()) return;

        Font font = mc.font;

        int lineH = 10;
        int pad = 4;
        int maxW = 0;

        for (var t : targets) {
            String text = formatLine(t.name, (int) Math.round(t.distance), t.bearingDeg);
            maxW = Math.max(maxW, font.width(text));
        }

        int width = maxW + pad * 2;
        int height = targets.size() * lineH + pad * 2;

        int x = ClientConfig.HUD_X.get();
        int y = ClientConfig.HUD_Y.get();
        if (ClientConfig.HUD_ANCHOR_RIGHT.get()) x = sw - width - ClientConfig.HUD_X.get();

        int bg = 0x88000000; // полупрозрачный чёрный
        int br = 0x66FFFFFF; // рамка

        gfx.fill(x, y, x + width, y + height, bg);
        gfx.fill(x, y, x + width, y + 1, br);
        gfx.fill(x, y + height - 1, x + width, y + height, br);
        gfx.fill(x, y, x + 1, y + height, br);
        gfx.fill(x + width - 1, y, x + width, y + height, br);

        int cy = y + pad + 1;
        for (var t : targets) {
            String text = formatLine(t.name, (int) Math.round(t.distance), t.bearingDeg);
            int color = colorByDistance(t.distance);
            gfx.drawString(font, text, x + pad, cy, color, true);
            cy += lineH;
        }
    };

    private static String formatLine(String name, int meters, float bearingDeg) {
        String arrow = bearingToArrow(bearingDeg);
        return arrow + " " + name + " [" + meters + "m]";
    }

    private static String bearingToArrow(float deg) {
        float a = (deg + 360f) % 360f;
        if (a < 22.5 || a >= 337.5) return "↑";
        if (a < 67.5)  return "↗";
        if (a < 112.5) return "→";
        if (a < 157.5) return "↘";
        if (a < 202.5) return "↓";
        if (a < 247.5) return "↙";
        if (a < 292.5) return "←";
        return "↖";
    }

    private static int colorByDistance(double d) {
        if (d < 15) return 0xFFFF5555;    // красный (очень близко)
        if (d < 35) return 0xFFFFFF55;    // жёлтый (середина)
        return 0xFF55FF55;                // зелёный (далеко)
    }

    private DroneDetectorHud() {}
}
