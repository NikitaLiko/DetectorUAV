# Drone Detector (SBW companion)

- Forge 1.20.1 (47.2.x), Java 17
- GeckoLib 4.4.6
- Item + Redstone block detector
- Config: `config/drone_detector-client.toml` (auto-created on first run)

## Build
1) Install JDK 17.
2) Run: `./gradlew genIntellijRuns` (or `genEclipseRuns`), then `./gradlew build`.
3) Put the built JAR from `build/libs` into your `mods/` folder alongside SuperbWarfare and GeckoLib.

> If you prefer, you can drop `src/` into a fresh Forge MDK 1.20.1 and use its gradle wrapper.