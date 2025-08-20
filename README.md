# DetectorUAV (Forge 1.20.1)

A lightweight Forge 1.20.1 mod that adds a **UAV detector item** with a minimal **bottom-right HUD**, showing a compact bar for the closest target and a short list of nearby drones with **their current names** and **approximate distance**. The detector **beeps only for the nearest UAV**, supports a **custom beep sound**, and uses a **server-controlled scan radius**. When switched **ON**, it works even **from inventory**.

---

## ✨ Features
- **Item-based detector** (toggle **ON/OFF** via right-click; state stored in NBT).
- **Minimal HUD (bottom-right)**: compact proximity bar + short list (up to 5) with name and ≈distance (keeps UI clean).
- **Beep for nearest only**; pitch/interval scale with distance.
- **Works from inventory**: if the detector item is ON anywhere in inventory, tracking/HUD/beeps are active.
- **Superb Warfare aware**: reads the **display name** each tick, so when SW renames a linked drone, the HUD shows the updated name automatically.
- **Server-controlled radius** (authoritative) synced to clients on login and config reload.
- **Client-only customization**: volume, pitch range, beep period, list size.
- **Custom sound** (`drone_detector:detector_beep`) with subtitles.
- **Extensible targets** via an **entity type tag**.

---

## 📦 Requirements
- **Minecraft:** 1.20.1
- **Forge:** 47.2.x
- *(Optional)* **Superb Warfare** — to have UAVs to detect.

---

## 🔧 Installation
1. Put the mod **on both client and server** for multiplayer.
2. Launch once to generate configs.
3. *(Optional)* Extend the target entity tag via a datapack (see below).

---

## 🕹️ Usage
- Obtain the **UAV Detector** item.
- **Right-click** to toggle **ON/OFF**.
- Keep it in hand **or anywhere in inventory** (if **ON**) to activate HUD + beeps.
- Approach drones:
    - The **bar fills** as you get closer.
    - The **list** shows **current names** (incl. Superb Warfare renamed drones) and **≈distance**.

---

## ⚙️ Configuration

**Server** *(authoritative gameplay settings)*  
`serverconfig/drone_detector-server.toml`
```toml
[detector]
# Scan radius in meters (synced to clients)
range = 64.0
