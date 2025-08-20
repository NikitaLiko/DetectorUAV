package ru.liko.dronedetector.registry;

import ru.liko.dronedetector.DroneDetectorMod;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DroneDetectorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
