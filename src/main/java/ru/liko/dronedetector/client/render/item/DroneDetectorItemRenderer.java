package ru.liko.dronedetector.client.render.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ru.liko.dronedetector.DDConstants;
import ru.liko.dronedetector.client.model.DroneDetectorModel;
import ru.liko.dronedetector.item.DroneDetectorItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DroneDetectorItemRenderer extends GeoItemRenderer<DroneDetectorItem> {
    private static final ResourceLocation TEX_OFF =
            new ResourceLocation(DDConstants.MODID, "textures/item/drone_detector.png");
    private static final ResourceLocation TEX_ON =
            new ResourceLocation(DDConstants.MODID, "textures/item/drone_detector_on.png");

    public DroneDetectorItemRenderer() {
        super(new DroneDetectorModel());
    }

    @Override
    public ResourceLocation getTextureLocation(DroneDetectorItem animatable) {
        ItemStack stack = this.getCurrentItemStack(); // GL4: текущий рендеримый стак
        return (stack != null && DroneDetectorItem.isActive(stack)) ? TEX_ON : TEX_OFF;
    }
}
