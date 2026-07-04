# Mind Your Bruises

[English](README.md) | [简体中文](README.zh-CN.md)

Mind Your Bruises is a client-side Minecraft Fabric mod for 1.21.11 that recolors the vanilla entity hurt overlay based on the damage type that caused it.

The mod keeps vanilla red as the fallback color, then uses different overlay rows for broad damage categories such as fire and explosion, frost and drowning, plant, arcane/wither/void-like damage, shock, starvation, and ender pearl damage.

## Features

- Recolors the vanilla entity hurt overlay on the client.
- Uses different colors for broad damage categories such as fire and explosion, frost and drowning, plant, arcane/wither/void-like damage, shock, starvation, and ender pearl damage.
- Keeps vanilla red as the fallback color for unmatched damage types.
- Supports JSON configuration for colors and damage type overrides.
- Supports custom damage types from mods and datapacks through `damageTypeOverrides`.

## Configuration

Mind Your Bruises creates this config file after the game starts:

```text
.minecraft/config/mind-your-bruises.json
```

In a development environment, the file is created under:

```text
run/config/mind-your-bruises.json
```

The config uses one JSON file directly under `config/`.

Example config:

```json
{
  "enabled": true,
  "fireColor": "#ff7014",
  "frostColor": "#4bd2ff",
  "plantColor": "#50dc3c",
  "fallbackColor": "#ff0000",
  "arcaneColor": "#b950ff",
  "shockColor": "#d8f6ff",
  "starvationColor": "#9a7a32",
  "enderColor": "#2bd6b3",
  "damageTypeOverrides": {
    "minecraft:lava": "fire",
    "minecraft:freeze": "frost",
    "minecraft:wither": "arcane",
    "minecraft:wither_skull": "arcane",
    "minecraft:lightning_bolt": "shock",
    "minecraft:starve": "starvation",
    "minecraft:ender_pearl": "ender"
  }
}
```

Manual edits require a game restart.

Configuration fields:

- `enabled`: Turns the mod's recolored hurt overlay on or off. When set to `false`, the overlay falls back to vanilla red.
- `fireColor`, `frostColor`, `plantColor`, `fallbackColor`, `arcaneColor`, `shockColor`, `starvationColor`, and `enderColor`: Hex RGB colors in `#rrggbb` format. Missing `#` is accepted and normalized on save.
- `damageTypeOverrides`: Maps a damage type id to a color group. Valid color groups are `fire`, `frost`, `plant`, `fallback`, `arcane`, `shock`, `starvation`, and `ender`. Explosion damage uses the `fire` group by default. Drowning damage uses the `frost` group by default. Out-of-world, outside-border, and generic-kill damage use the `arcane` group by default.

Default color groups:

| Color group | Default color | Built-in damage types |
| --- | --- | --- |
| `fire` | `#ff7014` | Fire, lava, hot floor, fireballs, explosions, fireworks |
| `frost` | `#4bd2ff` | Freezing and drowning damage |
| `plant` | `#50dc3c` | Cactus and sweet berry bush damage |
| `arcane` | `#b950ff` | Magic, indirect magic, dragon breath, sonic boom, thorns, wither, wither skull, out of world, outside border, generic kill |
| `shock` | `#d8f6ff` | Lightning damage |
| `starvation` | `#9a7a32` | Starvation damage |
| `ender` | `#2bd6b3` | Ender pearl damage |
| `fallback` | `#ff0000` | Unmatched damage types, vanilla physical damage, bee stings, suffocation, cramming |

For example, this makes a custom thorny plant damage type use the plant overlay color:

```json
"damageTypeOverrides": {
  "examplemod:thorny_vine": "plant"
}
```

If a damage type is not listed in `damageTypeOverrides`, Mind Your Bruises falls back to its built-in broad matching rules, then finally to `fallbackColor`.

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.19.3 or newer
- Java 21 or newer

## Compatibility

Mind Your Bruises is client-side only. It changes how hurt entities are rendered on your client and does not change damage rules, health, invulnerability frames, or server behavior.

## Building

```powershell
.\gradlew.bat build
```

The built jar will be generated under `build/libs/`.
