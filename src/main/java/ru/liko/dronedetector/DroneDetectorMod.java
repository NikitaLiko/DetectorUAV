package ru.liko.dronedetector;

import ru.liko.dronedetector.config.DroneDetectorConfig;
import ru.liko.dronedetector.registry.ModBlocks;
import ru.liko.dronedetector.registry.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DroneDetectorMod.MOD_ID)
public class DroneDetectorMod {
    public static final String MOD_ID = "drone_detector";

    public DroneDetectorMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.REGISTER.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.ITEMS.register(modBus);
        ModBlocks.BLOCK_ENTITIES.register(modBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DroneDetectorConfig.CLIENT_SPEC);
    }

}