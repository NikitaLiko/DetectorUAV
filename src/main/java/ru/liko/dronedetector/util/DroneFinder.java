package ru.liko.dronedetector.util;

import ru.liko.dronedetector.config.DroneDetectorConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DroneFinder {

    public static Entity findNearestDrone(Level level, Vec3 center, double radius, double vRadius, boolean requireLoS) {
        AABB box = new AABB(center.x - radius, center.y - vRadius, center.z - radius,
                            center.x + radius, center.y + vRadius, center.z + radius);

        List<Entity> list = level.getEntitiesOfClass(Entity.class, box, DroneFinder::isDroneLike);
        Entity nearest = null;
        double best = Double.MAX_VALUE;

        for (Entity e : list) {
            double d2 = e.distanceToSqr(center);
            if (d2 < best) {
                if (!requireLoS || hasLineOfSight(level, center, e.position())) {
                    best = d2; nearest = e;
                }
            }
        }
        return nearest;
    }

    private static boolean hasLineOfSight(Level level, Vec3 from, Vec3 to) {
        var res = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        return res == null || res.getType() == net.minecraft.world.phys.HitResult.Type.MISS;
    }

    private static boolean isDroneLike(Entity e) {
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(e.getType());
        if (key != null) {
            String ns = key.getNamespace();
            String path = key.getPath().toLowerCase(java.util.Locale.ROOT);
            if (DroneDetectorConfig.CLIENT.detectOnlyFromNamespace.get() && !ns.equals("superbwarfare")) {
                // skip
            } else {
                if (path.contains("drone") || path.contains("uav")) return true;
                for (String pat : DroneDetectorConfig.CLIENT.matchEntityIds.get()) {
                    if (path.contains(pat.toLowerCase(java.util.Locale.ROOT))) return true;
                }
            }
        }
        String cn = e.getClass().getSimpleName().toLowerCase(java.util.Locale.ROOT);
        return cn.contains("drone");
    }
}