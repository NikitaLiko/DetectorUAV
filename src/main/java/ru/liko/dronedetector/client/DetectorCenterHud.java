package ru.liko.dronedetector.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import ru.liko.dronedetector.common.ServerValues;

public final class DetectorCenterHud {

    public static final IGuiOverlay OVERLAY = (gui, gfx, pt, sw, sh) -> {
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        // Показываем панель ТОЛЬКО если детектор в руке и включён
        if (!DroneTracker.hasActiveDetector(mc.player)) return;

        var nearestOpt = DroneTracker.INSTANCE.getNearest();
        double range = Math.max(8.0, ServerValues.getRange());

        // Геометрия панели
        int barW = 180;
        int barH = 12;
        int pad  = 6;
        int panelW = barW + pad * 2;
        int panelH = barH + 22; // место под текст
        int x = (sw - panelW) / 2;
        int y = (sh - panelH) / 2;

        int bg = 0x88000000; // фон панели
        int br = 0x66FFFFFF; // рамка

        // Панель
        gfx.fill(x, y, x + panelW, y + panelH, bg);
        gfx.fill(x, y, x + panelW, y + 1, br);
        gfx.fill(x, y + panelH - 1, x + panelW, y + panelH, br);
        gfx.fill(x, y, x + 1, y + panelH, br);
        gfx.fill(x + panelW - 1, y, x + panelW, y + panelH, br);

        // Текст
        Font font = mc.font;
        String title;
        double fillFrac = 0.0;
        int barColor = 0xFF55FF55; // зелёный по умолчанию

        if (nearestOpt.isPresent()) {
            var t = nearestOpt.get();
            int dist = (int) Math.round(Math.min(t.distance, range));
            title = "БПЛА: " + dist + "м";
            fillFrac = 1.0 - Math.min(1.0, t.distance / range); // ближе -> больше заполнение
            barColor = colorByDistance(t.distance, range);
        } else {
            title = "БПЛА не обнаружены";
            fillFrac = 0.0;
        }

        int titleW = font.width(title);
        gfx.drawString(font, title, x + (panelW - titleW) / 2, y + 6, 0xFFFFFF, false);

        // Полоса
        int barX = x + pad;
        int barY = y + panelH - pad - barH;
        int barBg = 0xFF202020;
        gfx.fill(barX, barY, barX + barW, barY + barH, barBg);

        int fillW = (int) Math.round(barW * fillFrac);
        if (fillW > 0) {
            gfx.fill(barX, barY, barX + fillW, barY + barH, barColor);
        }

        // Засечки (0%, 25%, 50%, 75%, 100%)
        int tickColor = 0x66FFFFFF;
        for (int i = 0; i <= 4; i++) {
            int tx = barX + (barW * i) / 4;
            gfx.fill(tx, barY - 2, tx + 1, barY + barH + 2, tickColor);
        }
    };

    private static int colorByDistance(double d, double range) {
        double p = d / Math.max(1.0, range); // 0..1
        if (p <= 0.25) return 0xFFFF5555;    // красный (очень близко)
        if (p <= 0.6)  return 0xFFFFFF55;    // жёлтый
        return 0xFF55FF55;                   // зелёный
    }

    private DetectorCenterHud() {}
}
