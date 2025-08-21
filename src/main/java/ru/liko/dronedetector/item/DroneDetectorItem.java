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
    /** Ключ NBT для состояния "включено/выключено" */
    private static final String ACTIVE_TAG = "active";

    /** Имена анимаций должны совпадать с ключами в animations/*.animation.json */
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation PING = RawAnimation.begin().thenPlay("ping");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DroneDetectorItem(Properties props) {
        super(props.stacksTo(1));
        // Разрешаем синхронизацию триггеров анимации для предмета
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    /* =======================
       Состояние "включено"
       ======================= */

    /** Включена ли "рация" у данного стака */
    public static boolean isActive(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getBoolean(ACTIVE_TAG);
    }

    /** Установить состояние */
    public static void setActive(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, value);
    }

    /** Переключить состояние и вернуть новое значение */
    public static boolean toggle(ItemStack stack) {
        boolean now = !isActive(stack);
        setActive(stack, now);
        return now;
    }

    /* =======================
       GeckoLib: контроллеры
       ======================= */

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>(this, "main", 0, state -> state.setAndContinue(IDLE))
                        .triggerableAnim("ping", PING)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    /* =======================
       Рендерер предмета (BEWLR)
       ======================= */

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new ru.liko.dronedetector.client.render.item.DroneDetectorItemRenderer();
                }
                return renderer;
            }
        });
    }

    /* =======================
       ЛКМ/ПКМ поведение
       ======================= */

    /** ПКМ по воздуху: переключаем флаг и коротко проигрываем "ping" */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            boolean on = toggle(stack); // меняем NBT-флаг "active"

            // Тригерим короткую анимацию (имя контроллера и триггера должно совпадать с registerControllers)
            long id = GeoItem.getOrAssignId(stack, serverLevel);
            this.triggerAnim(player, id, "main", "ping");
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
