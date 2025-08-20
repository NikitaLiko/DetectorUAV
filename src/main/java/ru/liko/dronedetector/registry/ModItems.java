package ru.liko.dronedetector.registry;

import ru.liko.dronedetector.DroneDetectorMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> REGISTER =
            DeferredRegister.create(ForgeRegistries.ITEMS, DroneDetectorMod.MOD_ID);

    public static final RegistryObject<Item> DRONE_DETECTOR = REGISTER.register(
            "data/drone_detector",
            () -> new DroneDetectorItem(new Item.Properties().stacksTo(1))
    );

    // Простой предмет без GeckoLib
    public static class DroneDetectorItem extends Item {
        public DroneDetectorItem(Properties props) { super(props); }

        @Override
        public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> use(
                net.minecraft.world.level.Level level,
                net.minecraft.world.entity.player.Player player,
                net.minecraft.world.InteractionHand hand) {

            var stack = player.getItemInHand(hand);
            if (!level.isClientSide) return net.minecraft.world.InteractionResultHolder.pass(stack);

            var tag = stack.getOrCreateTag();
            tag.putBoolean("active", !tag.getBoolean("active")); // просто переключаем флаг
            // Никаких сообщений в actionbar здесь!
            return net.minecraft.world.InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }


        @Override
        public boolean isFoil(ItemStack stack) {
            return stack.hasTag() && stack.getTag().getBoolean("active"); // блестит, когда включён
        }
    }
}
