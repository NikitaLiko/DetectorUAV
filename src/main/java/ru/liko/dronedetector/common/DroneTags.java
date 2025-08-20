package ru.liko.dronedetector.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import ru.liko.dronedetector.DDConstants;

public final class DroneTags {
    public static final TagKey<EntityType<?>> DRONE_TARGETS =
            TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(DDConstants.MODID, "drone_targets"));

    private DroneTags() {}
}
