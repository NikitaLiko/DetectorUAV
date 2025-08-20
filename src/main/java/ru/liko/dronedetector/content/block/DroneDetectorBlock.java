package ru.liko.dronedetector.content.block;

import ru.liko.dronedetector.content.blockentity.DroneDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class DroneDetectorBlock extends Block implements EntityBlock {

    public DroneDetectorBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(BlockStateProperties.POWERED);
    }

    @Override
    public boolean isSignalSource(BlockState state) { return true; }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.core.Direction dir) {
        return state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DroneDetectorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, p, st, be) -> {
            if (be instanceof DroneDetectorBlockEntity dbe) dbe.serverTick();
        };
    }
}