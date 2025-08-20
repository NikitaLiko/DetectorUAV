package ru.liko.dronedetector.registry;

import ru.liko.dronedetector.content.block.DroneDetectorBlock;
import ru.liko.dronedetector.content.blockentity.DroneDetectorBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.liko.dronedetector.DDConstants;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DDConstants.MODID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DDConstants.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DDConstants.MODID);

    public static final RegistryObject<Block> DRONE_DETECTOR_BLOCK = BLOCKS.register("drone_detector_block", () ->
            new DroneDetectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0f, 6.0f).lightLevel(s -> s.getValue(BlockStateProperties.POWERED) ? 7 : 0))
    );

    public static final RegistryObject<Item> DRONE_DETECTOR_BLOCK_ITEM = ITEMS.register("drone_detector_block", () ->
            new BlockItem(DRONE_DETECTOR_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<BlockEntityType<DroneDetectorBlockEntity>> DRONE_DETECTOR_BE =
            BLOCK_ENTITIES.register("drone_detector_be", () ->
                    BlockEntityType.Builder.of(DroneDetectorBlockEntity::new, DRONE_DETECTOR_BLOCK.get()).build(null)
            );
}