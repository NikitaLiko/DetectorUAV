package ru.liko.dronedetector;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.liko.dronedetector.client.ClientConfig;
import ru.liko.dronedetector.common.ServerConfig;
import ru.liko.dronedetector.net.DDNetwork;
import ru.liko.dronedetector.registry.ModItems;
import ru.liko.dronedetector.registry.ModSounds;
import software.bernie.geckolib.GeckoLib; // импорт GeckoLib

@Mod(DDConstants.MODID)
public final class DroneDetector {

    public DroneDetector() {
        GeckoLib.initialize(); // инициализация GeckoLib один раз при старте мода

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modBus);
        ModSounds.register(modBus);

        ServerConfig.register();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientConfig::register);

        DDNetwork.register();
    }
}
