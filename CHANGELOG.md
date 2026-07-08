[ReleaseTag]() is automatically replaced with the release tag, for example mc1.21.11-1.0.0.
[MCVersion]() is automatically replaced with the Minecraft version, for example 1.21.11.
[ModVersion]() is automatically replaced with the mod version, for example 1.0.0.

Everything above the line is ignored. Everything below the line is used as the release notes for GitHub and Modrinth.

----------

## Features

- Improved built-in damage type matching for more consistent color group selection.
- Removed the built-in ender pearl damage mapping for Minecraft versions that do not expose an ender pearl damage type.

## Fixes

- Fixed empty custom color values falling back safely to their defaults.
