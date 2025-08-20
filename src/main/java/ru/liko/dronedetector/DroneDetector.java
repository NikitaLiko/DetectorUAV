package ru.liko.dronedetector;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import ru.liko.dronedetector.client.ClientConfig;
import ru.liko.dronedetector.common.ServerConfig;
import ru.liko.dronedetector.net.DDNetwork;

@Mod(DDConstants.MODID) // уже ОК, т.к. берёт из DDConstants
public final class DroneDetector {
    public DroneDetector() {
        // SERVER config (геймплейные параметры)
        ServerConfig.register();

        // CLIENT config (только визуал/звук)
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientConfig.register());

        // Сетевой канал для синка серверных значений клиенту
        DDNetwork.register();
    }
}
