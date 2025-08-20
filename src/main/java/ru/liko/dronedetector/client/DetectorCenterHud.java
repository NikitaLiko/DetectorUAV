package ru.liko.dronedetector.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import ru.liko.dronedetector.common.ServerValues;

import java.util.List;

public final class DetectorCenterHud {

    // Панель в правом нижнем углу: маленькая шкала по ближайшей цели + краткий список имен/дистанций
    public static final IGuiOverlay OVERLAY = (gui, gfx, pt, sw, sh) -> {
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        // Показываем HUD ТОЛЬКО когда детектор в руке и включен
        if (!DroneTracker.hasActiveDetector(mc.player)) return;

        var targets = DroneTracker.INSTANCE.getSnapshotLimited(5); // короткий список (5 строк)
        double range = Math.max(8.0, ServerValues.getRange());
        Font font = mc.font;

        // --- Геометрия (минималистичный вид) ---
        int margin = 8;          // отступ от краев экрана
        int lineH  = 10;         // высота строки списка
        int pad    = 5;          // внутренние отступы панели
        int barW   = 120;        // ширина шкалы
        int barH   = 6;          // высота шкалы
        int listN  = Math.min(5, targets.size());

        // Заголовок и шкала занимают фикс. высоту; + список строк
        int panelW = Math.max(160, barW + pad * 2);
        int panelH = 18 + pad * 2 + (listN > 0 ? (listN * lineH + 4) : 0) + barH + 6;

        // Правый-нижний угол
        int x = sw - panelW - margin;
        int y = sh - panelH - margin;

        // Фон и рамка
        int bg = 0x66000000;  // более прозрачный, чтобы не мешал интерфейсу
        int br = 0x55FFFFFF;
        gfx.fill(x, y, x + panelW, y + panelH, bg);
        gfx.fill(x, y, x + panelW, y + 1, br);
        gfx.fill(x, y + panelH - 1, x + panelW, y + panelH, br);
        gfx.fill(x, y, x + 1, y + panelH, br);
        gfx.fill(x + panelW - 1, y, x + panelW, y + panelH, br);

        // --- Блок ближайшей цели: заголовок + шкала ---
        int cy = y + pad;

        // Текст заголовка (ближайший или "не обнаружены")
        String title;
        double fillFrac = 0.0;
        int barColor = 0xFF55FF55; // зелёный по умолчанию

        var nearestOpt = DroneTracker.INSTANCE.getNearest();
        if (nearestOpt.isPresent()) {
            var t = nearestOpt.get();
            int approx = approxMeters(t.distance); // округление до 5 м
            title = "Ближайший БПЛА: ≈" + approx + "м";
            fillFrac = 1.0 - Math.min(1.0, t.distance / range); // ближе -> больше заполнение
            barColor = colorByDistance(t.distance, range);
        } else {
            title = "БПЛА не обнаружены";
        }

        int titleW = font.width(title);
        gfx.drawString(font, title, x + (panelW - titleW)/2, cy, 0xFFFFFF, false);
        cy += 14;

        // Шкала заполнения
        int barX = x + pad;
        int barY = cy;
        cy += barH + 6;

        int barBg = 0xFF202020;
        gfx.fill(barX, barY, barX + barW, barY + barH, barBg);

        int fillW = (int) Math.round(barW * fillFrac);
        if (fillW > 0) {
            gfx.fill(barX, barY, barX + fillW, barY + barH, barColor);
        }

        // --- Список ближайших (имя + ≈дистанция) ---
        if (!targets.isEmpty()) {
            List<DroneTracker.Target> show = targets.subList(0, listN);
            for (var t : show) {
                String line = clipName(t.name, 18) + "  ·  ≈" + approxMeters(t.distance) + "м";
                int color = listColorByDistance(t.distance, range);
                gfx.drawString(font, line, x + pad, cy, color, false);
                cy += lineH;
            }
        }
    };

    private static int approxMeters(double d) {
        // округляем до шага 5м для «примерного» отображения
        return (int) Math.round(d / 5.0) * 5;
    }

    private static String clipName(String s, int max) {
        if (s == null) return "";
        return (s.length() <= max) ? s : (s.substring(0, Math.max(0, max - 1)) + "…");
    }

    private static int colorByDistance(double d, double range) {
        double p = d / Math.max(1.0, range);
        if (p <= 0.25) return 0xFFFF5555; // близко — красный
        if (p <= 0.6)  return 0xFFFFFF55; // средне — жёлтый
        return 0xFF55FF55;               // далеко — зелёный
    }

    private static int listColorByDistance(double d, double range) {
        // менее насыщенные цвета для списка
        double p = d / Math.max(1.0, range);
        if (p <= 0.25) return 0xFFDD7777;
        if (p <= 0.6)  return 0xFFDDDD77;
        return 0xFF88DD88;
    }

    private DetectorCenterHud() {}
}
