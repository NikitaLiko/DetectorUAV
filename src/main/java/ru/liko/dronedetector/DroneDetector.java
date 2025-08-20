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

@Mod(DDConstants.MODID)
public final class DroneDetector {
    public DroneDetector() {
        // Регистрация реестров
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modBus); // только предметы

        // Конфиги
        ServerConfig.register();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientConfig::register);

        // Сетевой канал (для синка серверных значений вроде range)
        DDNetwork.register();
    }
}
