package ru.liko.dronedetector.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.liko.dronedetector.DDConstants;

@Mod.EventBusSubscriber(modid = DDConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModCreativeTabEvents {

    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            var entries  = event.getEntries();

            // якорь — деревянная лопата
            ItemStack anchor   = new ItemStack(Items.WOODEN_SHOVEL);
            // твой предмет
            ItemStack detector = new ItemStack(ModItems.DRONE_DETECTOR.get());

            // ставим перед деревянной лопатой
            entries.putBefore(anchor, detector, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    private ModCreativeTabEvents() {}
}
