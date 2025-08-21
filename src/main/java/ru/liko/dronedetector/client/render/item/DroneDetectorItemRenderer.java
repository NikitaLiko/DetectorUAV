package ru.liko.dronedetector.client.render.item;

import ru.liko.dronedetector.client.model.DroneDetectorModel;
import ru.liko.dronedetector.item.DroneDetectorItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DroneDetectorItemRenderer extends GeoItemRenderer<DroneDetectorItem> {
    public DroneDetectorItemRenderer() {
        super(new DroneDetectorModel());
    }
}
