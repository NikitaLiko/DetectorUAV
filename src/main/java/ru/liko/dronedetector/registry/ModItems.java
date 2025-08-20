package ru.liko.dronedetector.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.liko.dronedetector.DDConstants;
import ru.liko.dronedetector.item.DroneDetectorItem;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DDConstants.MODID);

    public static final RegistryObject<Item> DRONE_DETECTOR =
            ITEMS.register("drone_detector", () -> new DroneDetectorItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
