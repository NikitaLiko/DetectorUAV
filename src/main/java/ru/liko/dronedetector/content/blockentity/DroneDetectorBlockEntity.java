package ru.liko.dronedetector.content.blockentity;

import ru.liko.dronedetector.config.DroneDetectorConfig;
import ru.liko.dronedetector.registry.ModBlocks;
import ru.liko.dronedetector.util.DroneFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class DroneDetectorBlockEntity extends BlockEntity {

    private int pulseTicks = 0;

    public DroneDetectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DRONE_DETECTOR_BE.get(), pos, state);
    }

    public void serverTick() {
        if (this.level == null) return;
        boolean powered = getBlockState().getValue(BlockStateProperties.POWERED);

        if (this.level.getGameTime() % 10 == 0) {
            var nearest = DroneFinder.findNearestDrone(this.level, Vec3.atCenterOf(this.worldPosition),
                    DroneDetectorConfig.CLIENT.radius.get(),
                    DroneDetectorConfig.CLIENT.verticalRadius.get(),
                    DroneDetectorConfig.CLIENT.requireLineOfSight.get());
            if (nearest != null) {
                pulseTicks = Math.max(pulseTicks, DroneDetectorConfig.CLIENT.blockPulseTicks.get());
            }
        }

        if (pulseTicks > 0) {
            if (!powered) setPowered(true);
            pulseTicks--;
        } else if (powered) {
            setPowered(false);
        }
    }

    private void setPowered(boolean on) {
        if (this.level == null) return;
        BlockState newState = getBlockState().setValue(BlockStateProperties.POWERED, on);
        this.level.setBlock(this.worldPosition, newState, 3);
        this.level.updateNeighborsAt(this.worldPosition, newState.getBlock());
    }
}