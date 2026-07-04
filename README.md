# Mind Your Bruises

[English](README.md) | [简体中文](README.zh-CN.md)

Mind Your Bruises is a client-side Minecraft Fabric mod for 1.21.11 that recolors the vanilla entity hurt overlay based on the damage type that caused it.

The mod keeps vanilla red as the fallback color, then uses different overlay rows for broad damage categories such as fire and explosion, frost, toxic, arcane, water, void, wither, shock, starvation, and ender pearl damage.

## Features

- Recolors the vanilla entity hurt overlay on the client.
- Uses different colors for broad damage categories such as fire and explosion, frost, toxic, arcane, water, void, wither, shock, starvation, and ender pearl damage.
- Keeps vanilla red as the fallback color for unmatched damage types.
- Supports JSON configuration for colors and damage type overrides.
- Can use status effect hints so poison effect damage can appear toxic even though vanilla reports it as magic damage.
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
  "useStatusEffectHints": true,
  "fireColor": "#ff7014",
  "frostColor": "#4bd2ff",
  "toxicColor": "#50dc3c",
  "fallbackColor": "#ff0000",
  "arcaneColor": "#b950ff",
  "waterColor": "#3c78ff",
  "voidColor": "#2d005a",
  "witherColor": "#d6d6c8",
  "shockColor": "#d8f6ff",
  "starvationColor": "#9a7a32",
  "enderColor": "#2bd6b3",
  "damageTypeOverrides": {
    "minecraft:lava": "fire",
    "minecraft:freeze": "frost",
    "minecraft:wither": "wither",
    "minecraft:wither_skull": "wither",
    "minecraft:lightning_bolt": "shock",
    "minecraft:starve": "starvation",
    "minecraft:ender_pearl": "ender"
  }
}
```

Manual edits require a game restart.

Configuration fields:

- `enabled`: Turns the mod's recolored hurt overlay on or off. When set to `false`, the overlay falls back to vanilla red.
- `useStatusEffectHints`: Allows the mod to use the entity's active status effects when the damage type is too broad. For example, vanilla poison effect ticks use `minecraft:magic`, so this option lets poisoned magic damage use the toxic color.
- `fireColor`, `frostColor`, `toxicColor`, `fallbackColor`, `arcaneColor`, `waterColor`, `voidColor`, `witherColor`, `shockColor`, `starvationColor`, and `enderColor`: Hex RGB colors in `#rrggbb` format. Missing `#` is accepted and normalized on save.
- `damageTypeOverrides`: Maps a damage type id to a color group. Valid color groups are `fire`, `frost`, `toxic`, `fallback`, `arcane`, `water`, `void`, `wither`, `shock`, `starvation`, and `ender`. Explosion damage uses the `fire` group by default.

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
