package ru.liko.dronedetector.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.liko.dronedetector.DDConstants;

public final class ModSounds {
    private ModSounds() {}

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DDConstants.MODID);

    // Наш кастомный звук "drone_detector:detector_beep"
    public static final RegistryObject<SoundEvent> DETECTOR_BEEP =
            SOUND_EVENTS.register("detector_beep",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(DDConstants.MODID, "detector_beep")));

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
