package ru.liko.dronedetector.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import ru.liko.dronedetector.DDConstants;
import ru.liko.dronedetector.content.blockentity.DroneDetectorBlockEntity;

public final class ModBlocks {
    private ModBlocks() {}

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DDConstants.MODID);

    public static final DeferredRegister<net.minecraft.world.item.Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DDConstants.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DDConstants.MODID);

    // --- ВАЖНО: даём проекту символ, которого не хватало ---
    // Зарегистрируй здесь свои блоки и подставь их в список valid blocks.
    // Пока регистрируем BE без привязанных блоков, чтобы сборка прошла; позже подставь свои:
    //    .of(DroneDetectorBlockEntity::new, YOUR_BLOCK.get())
    public static final RegistryObject<BlockEntityType<DroneDetectorBlockEntity>> DRONE_DETECTOR_BE =
            BLOCK_ENTITIES.register("drone_detector",
                    () -> BlockEntityType.Builder.of(DroneDetectorBlockEntity::new /* factory */,
                                    new Block[]{} /* TODO: сюда добавить твой блок(и) */)
                            .build(null));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }
}
