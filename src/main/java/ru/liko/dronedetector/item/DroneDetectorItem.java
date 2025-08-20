package ru.liko.dronedetector.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class DroneDetectorItem extends Item {
    private static final String ACTIVE_TAG = "Active";

    public DroneDetectorItem(Properties props) {
        super(props);
    }

    /** Читает состояние "включён" из NBT. */
    public static boolean isActive(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(ACTIVE_TAG);
    }

    /** Запись состояния в NBT. */
    public static void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, active);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            boolean newState = !isActive(stack);
            setActive(stack, newState);
            player.displayClientMessage(
                    Component.translatable(newState ? "item.drone_detector.state_on" : "item.drone_detector.state_off"),
                    true
            );
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        boolean on = isActive(stack);
        tooltip.add(Component.translatable(on ? "item.drone_detector.tooltip_on" : "item.drone_detector.tooltip_off"));
    }
}
