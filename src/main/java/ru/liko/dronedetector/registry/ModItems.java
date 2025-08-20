package ru.liko.dronedetector.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.minecraftforge.eventbus.api.IEventBus;

import ru.liko.dronedetector.DDConstants;

public final class ModItems {
    private ModItems() {}

    // ВАЖНО: здесь тоже заменили только MODID
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DDConstants.MODID);

    // ОСТАЛЬНЫЕ ТВОИ РЕГИСТРАЦИИ (RegistryObject<Item>) ОСТАВЬ КАК БЫЛО.
    // Например:
    // public static final RegistryObject<Item> DRONE_DETECTOR = ITEMS.register("drone_detector", ...);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
