[ReleaseTag]() is automatically replaced with the release tag, for example mc1.21.11-1.0.0.
[MCVersion]() is automatically replaced with the Minecraft version, for example 1.21.11.
[ModVersion]() is automatically replaced with the mod version, for example 1.0.0.

Everything above the line is ignored. Everything below the line is used as the release notes for GitHub and Modrinth.

----------

Mind Your Bruises [ModVersion]() for Minecraft [MCVersion]() adds client-side damage-colored entity hurt overlays.

## Highlights

- Colors the vanilla entity hurt flash based on damage type.
- Supports fire, frost, plant, arcane, shock, starvation, ender, and fallback color groups.
- Adds optional in-game configuration through Mod Menu and YetAnotherConfigLib.
- Keeps working without optional config-screen dependencies by falling back to manual JSON configuration.

## Compatibility

- Minecraft [MCVersion]()
- Fabric Loader 0.19.3 or newer
- Client-side only
