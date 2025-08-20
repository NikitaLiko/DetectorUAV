package ru.liko.dronedetector.registry;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.liko.dronedetector.DDConstants;

@Mod.EventBusSubscriber(modid = DDConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModCreativeTabEvents {
    private ModCreativeTabEvents() {}

    @SubscribeEvent
    public static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.DRONE_DETECTOR);
        }
    }
}
