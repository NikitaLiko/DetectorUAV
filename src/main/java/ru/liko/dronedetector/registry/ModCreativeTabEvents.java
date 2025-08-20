package ru.liko.dronedetector.registry;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.liko.dronedetector.DDConstants;
@Mod.EventBusSubscriber(modid = DDConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ModCreativeTabEvents {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            e.accept(ModItems.DRONE_DETECTOR.get());
        }
        if (e.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            e.accept(ModBlocks.DRONE_DETECTOR_BLOCK.get());
        }
    }
}
