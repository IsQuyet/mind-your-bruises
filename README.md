# Mind Your Bruises

[English](README.md) | [简体中文](README.zh-CN.md)

Mind Your Bruises is a client-side Minecraft Fabric mod for 1.21.11 that recolors the vanilla entity hurt overlay based on the damage type that caused it.

The mod keeps vanilla red as the fallback color, then uses different overlay rows for broad damage categories such as fire, frost, toxic, arcane, blast, water, and void damage.

## Features

- Recolors the vanilla entity hurt overlay on the client.
- Uses different colors for broad damage categories such as fire, frost, toxic, arcane, blast, water, and void damage.
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
  "toxicColor": "#50dc3c",
  "fallbackColor": "#ff0000",
  "arcaneColor": "#b950ff",
  "blastColor": "#ffd540",
  "waterColor": "#3c78ff",
  "voidColor": "#2d005a",
  "damageTypeOverrides": {
    "minecraft:lava": "fire",
    "minecraft:freeze": "frost"
  }
}
```

Manual edits require a game restart.

Configuration fields:

- `enabled`: Turns the mod's recolored hurt overlay on or off. When set to `false`, the overlay falls back to vanilla red.
- `fireColor`, `frostColor`, `toxicColor`, `fallbackColor`, `arcaneColor`, `blastColor`, `waterColor`, and `voidColor`: Hex RGB colors in `#rrggbb` format. Missing `#` is accepted and normalized on save.
- `damageTypeOverrides`: Maps a damage type id to a color group. Valid color groups are `fire`, `frost`, `toxic`, `fallback`, `arcane`, `blast`, `water`, and `void`.

For example, this makes a custom acid damage type use the toxic overlay color:

```json
"damageTypeOverrides": {
  "examplemod:acid": "toxic"
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
