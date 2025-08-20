package ru.liko.dronedetector.client;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import ru.liko.dronedetector.common.DroneTags;
import ru.liko.dronedetector.common.ServerValues;

import java.util.*;
import java.util.function.Predicate;

public final class DroneTracker {

    public static final DroneTracker INSTANCE = new DroneTracker();

    public static final class Target {
        public final UUID id;
        public final Entity entity;
        public final String name;
        public final double distance;
        public final float bearingDeg;

        Target(Entity e, Player player) {
            this.id = e.getUUID();
            this.entity = e;
            this.name = e.getDisplayName().getString();
            this.distance = e.distanceTo(player);
            var vec = e.position().subtract(player.position());
            float yawTo = (float)(Mth.atan2(vec.z, vec.x) * (180f/Math.PI)) - 90f;
            this.bearingDeg = Mth.wrapDegrees(yawTo - player.getYRot());
        }
    }

    private final List<Target> snapshot = new ArrayList<>();
    private UUID nearestId = null;
    private int beepCooldownTicks = 0;

    private DroneTracker() {}

    public List<Target> getSnapshotLimited(int limit) {
        if (snapshot.size() <= limit) return Collections.unmodifiableList(snapshot);
        return Collections.unmodifiableList(snapshot.subList(0, limit));
    }

    public Optional<Target> getNearest() {
        return snapshot.isEmpty() ? Optional.empty() : Optional.of(snapshot.get(0));
    }

    public void tick(Minecraft mc) {
        var player = mc.player;
        var level  = mc.level;
        if (player == null || level == null) return;

        double range = ServerValues.getRange();
        boolean onlyNearestBeep = ClientConfig.ONLY_NEAREST_BEEP.get();

        AABB box = player.getBoundingBox().inflate(range);
        Predicate<Entity> filter = this::isDroneCandidate;
        List<Entity> found = level.getEntitiesOfClass(Entity.class, box, e -> e != player && filter.test(e));

        snapshot.clear();
        for (Entity e : found) snapshot.add(new Target(e, player));
        snapshot.sort(Comparator.comparingDouble(t -> t.distance));

        if (onlyNearestBeep && !snapshot.isEmpty()) {
            var nearest = snapshot.get(0);
            if (!nearest.id.equals(nearestId)) {
                nearestId = nearest.id;
                beepCooldownTicks = 0;
            }
            handleBeep(level, player, nearest, range);
        } else {
            nearestId = null;
        }
    }

    private void handleBeep(Level level, Player player, Target nearest, double range) {
        if (beepCooldownTicks > 0) { beepCooldownTicks--; return; }

        double d = nearest.distance;
        double minD = 5.0;
        double maxD = Math.max(range, 16.0);
        double t = Mth.clamp((d - minD) / (maxD - minD), 0.0, 1.0); // 0 близко, 1 далеко

        int   minPeriod = ClientConfig.BEEP_MIN_PERIOD.get();
        int   maxPeriod = ClientConfig.BEEP_MAX_PERIOD.get();
        int   period    = (int) Mth.lerp(t, minPeriod, maxPeriod);

        float minPitch = ClientConfig.BEEP_PITCH_MAX.get().floatValue(); // ближе -> выше
        float maxPitch = ClientConfig.BEEP_PITCH_MIN.get().floatValue(); // дальше -> ниже
        float pitch    = (float) Mth.lerp(t, minPitch, maxPitch);

        float volume = ClientConfig.BEEP_VOLUME.get().floatValue();

        level.playLocalSound(
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.NOTE_BLOCK_HAT.value(), // Holder<SoundEvent> -> SoundEvent
                SoundSource.PLAYERS,
                volume, pitch, false
        );

        beepCooldownTicks = Math.max(1, period);
    }

    private boolean isDroneCandidate(Entity e) {
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(e.getType());
        if (key == null) return false;

        if (e.getType().is(DroneTags.DRONE_TARGETS)) return true;

        String ns = key.getNamespace();
        String path = key.getPath();
        return (ns.equals("sbw") || ns.equals("superbwarfare")) && path.contains("drone");
    }
}
