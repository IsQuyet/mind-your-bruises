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
	public static final int WITHER_HURT_ROW = 8;
	public static final int SHOCK_HURT_ROW = 9;
	public static final int STARVATION_HURT_ROW = 11;
	public static final int ENDER_HURT_ROW = 12;
	public static final int SUFFOCATION_HURT_ROW = 13;

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

		if (containsAny(damageTypePath, "lightning", "shock")) {
			return SHOCK_HURT_ROW;
		}

		if (containsAny(damageTypePath, "starve", "starvation")) {
			return STARVATION_HURT_ROW;
		}

		if (containsAny(damageTypePath, "ender_pearl")) {
			return ENDER_HURT_ROW;
		}

		if (containsAny(damageTypePath, "in_wall", "suffocation", "suffocate")) {
			return SUFFOCATION_HURT_ROW;
		}

		if (containsAny(damageTypePath, "explosion", "fireworks")) {
			return BLAST_HURT_ROW;
		}

		if (containsAny(damageTypePath, "out_of_world", "outside_border", "void", "generic_kill")) {
			return VOID_HURT_ROW;
		}

		if (containsAny(damageTypePath, "wither")) {
			return WITHER_HURT_ROW;
		}

		if (containsAny(damageTypePath, "magic", "dragon_breath", "sonic_boom", "thorns")) {
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

}
