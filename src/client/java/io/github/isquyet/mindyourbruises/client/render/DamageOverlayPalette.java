package io.github.isquyet.mindyourbruises.client.render;

import io.github.isquyet.mindyourbruises.client.MindYourBruisesConfig;
import net.minecraft.resources.Identifier;

import java.util.Locale;

public final class DamageOverlayPalette {
	public static final int VANILLA_HURT_OVERLAY_ALPHA = 178;

	public static final int FIRE_HURT_ROW = 0;
	public static final int FROST_HURT_ROW = 1;
	public static final int TOXIC_HURT_ROW = 2;
	public static final int VANILLA_HURT_ROW = 3;
	public static final int ARCANE_HURT_ROW = 4;
	public static final int BLAST_HURT_ROW = 5;
	public static final int WATER_HURT_ROW = 6;
	public static final int VOID_HURT_ROW = 7;

	private static final int[] HURT_ROW_COLORS = {
		argb(255, 112, 20),
		argb(75, 210, 255),
		argb(80, 220, 60),
		argb(255, 0, 0),
		argb(185, 80, 255),
		argb(255, 213, 64),
		argb(60, 120, 255),
		argb(45, 0, 90)
	};

	private DamageOverlayPalette() {
	}

	public static int colorForOverlayRow(int overlayRow) {
		return MindYourBruisesConfig.get().colorForOverlayRow(overlayRow);
	}

	public static int selectOverlayRow(Identifier damageTypeId) {
		MindYourBruisesConfig config = MindYourBruisesConfig.get();
		if (!config.enabled()) {
			return VANILLA_HURT_ROW;
		}

		Integer configuredOverlayRow = config.overlayRowForDamageType(damageTypeId);
		if (configuredOverlayRow != null) {
			return configuredOverlayRow;
		}

		if (damageTypeId == null) {
			return VANILLA_HURT_ROW;
		}

		String damageTypePath = damageTypeId.getPath().toLowerCase(Locale.ROOT);

		if (containsAny(damageTypePath, "freeze", "frozen")) {
			return FROST_HURT_ROW;
		}

		if (containsAny(damageTypePath, "in_fire", "on_fire", "lava", "hot_floor", "fireball", "burn")) {
			return FIRE_HURT_ROW;
		}

		if (containsAny(damageTypePath, "drown")) {
			return WATER_HURT_ROW;
		}

		if (containsAny(damageTypePath, "explosion", "fireworks")) {
			return BLAST_HURT_ROW;
		}

		if (containsAny(damageTypePath, "out_of_world", "outside_border", "void", "generic_kill")) {
			return VOID_HURT_ROW;
		}

		if (containsAny(damageTypePath, "magic", "wither", "dragon_breath", "sonic_boom", "thorns")) {
			return ARCANE_HURT_ROW;
		}

		if (containsAny(damageTypePath, "cactus", "sweet_berry", "sting", "poison")) {
			return TOXIC_HURT_ROW;
		}

		return VANILLA_HURT_ROW;
	}

	private static boolean containsAny(String value, String... needles) {
		for (String needle : needles) {
			if (value.contains(needle)) {
				return true;
			}
		}

		return false;
	}

	private static int argb(int red, int green, int blue) {
		return (VANILLA_HURT_OVERLAY_ALPHA << 24) | (red << 16) | (green << 8) | blue;
	}
}
