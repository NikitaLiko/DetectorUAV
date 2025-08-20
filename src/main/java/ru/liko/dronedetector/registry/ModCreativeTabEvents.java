package ru.liko.dronedetector.registry;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.world.item.CreativeModeTabs;
import ru.liko.dronedetector.DDConstants;

@Mod.EventBusSubscriber(modid = DDConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModCreativeTabEvents {
    private ModCreativeTabEvents() {}

    @SubscribeEvent
    public static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        // Пример добавления предмета в стандартную вкладку (раскомментируй, когда будет предмет):
        // if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
        //     event.accept(ModItems.DRONE_DETECTOR.get());
        // }
        // Сейчас метод пустой — лишь фиксит импорт/сигнатуру под Forge 47 (1.20.1).
    }
}
