package ru.liko.dronedetector.client.model;

import ru.liko.dronedetector.DDConstants;
import ru.liko.dronedetector.item.DroneDetectorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DroneDetectorModel extends GeoModel<DroneDetectorItem> {
    private static final ResourceLocation MODEL =
            new ResourceLocation(DDConstants.MODID, "geo/drone_detector.geo.json");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DDConstants.MODID, "textures/item/drone_detector.png");
    private static final ResourceLocation ANIM =
            new ResourceLocation(DDConstants.MODID, "animations/drone_detector.animation.json");

    @Override
    public ResourceLocation getModelResource(DroneDetectorItem animatable) { return MODEL; }

    @Override
    public ResourceLocation getTextureResource(DroneDetectorItem animatable) { return TEXTURE; }

    @Override
    public ResourceLocation getAnimationResource(DroneDetectorItem animatable) { return ANIM; }
}
