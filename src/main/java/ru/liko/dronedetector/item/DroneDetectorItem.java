package ru.liko.dronedetector.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class DroneDetectorItem extends Item implements GeoItem {
    private static final String ACTIVE_TAG = "active";

    // Имена должны совпадать с именами клипов в animations/*.animation.json
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation PING = RawAnimation.begin().thenPlay("ping");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DroneDetectorItem(Properties props) {
        super(props);
        // Включаем синхронизацию анимаций для предмета
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    // Для твоего DroneTracker
    public static boolean isActive(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getBoolean(ACTIVE_TAG);
    }
    public static void setActive(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, value);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0, state -> state.setAndContinue(IDLE))
                .triggerableAnim("ping", PING));
    }

    // Регистрация рендера предмета через IClientItemExtensions (Forge 1.19.3–1.20.6)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() { // см. Forge Docs про BEWLR
                if (renderer == null) {
                    renderer = new ru.liko.dronedetector.client.render.item.DroneDetectorItemRenderer();
                }
                return renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // ПКМ: переключаем состояние + проигрываем короткую анимацию "ping"
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            boolean newState = !isActive(stack);
            setActive(stack, newState);
            long id = GeoItem.getOrAssignId(stack, serverLevel);
            this.triggerAnim(player, id, "main", "ping");
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
