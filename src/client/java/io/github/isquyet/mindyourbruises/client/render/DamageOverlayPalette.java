package io.github.isquyet.mindyourbruises.client.render;

import io.github.isquyet.mindyourbruises.client.MindYourBruisesConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class DamageOverlayPalette {
	public static final String FIRE_GROUP_NAME = "fire";
	public static final String FROST_GROUP_NAME = "frost";
	public static final String PLANT_GROUP_NAME = "plant";
	public static final String FALLBACK_GROUP_NAME = "fallback";
	public static final String ARCANE_GROUP_NAME = "arcane";
	public static final String SHOCK_GROUP_NAME = "shock";
	public static final String STARVATION_GROUP_NAME = "starvation";
	public static final String ENDER_GROUP_NAME = "ender";

	public static final int VANILLA_HURT_OVERLAY_ALPHA = 178;

	// Row 3 is vanilla red. Row 10 is left untouched because vanilla uses it for white overlay effects.
	public static final int FIRE_HURT_ROW = 0;
	public static final int FROST_HURT_ROW = 1;
	public static final int PLANT_HURT_ROW = 2;
	public static final int VANILLA_HURT_ROW = 3;
	public static final int ARCANE_HURT_ROW = 4;
	public static final int SHOCK_HURT_ROW = 9;
	public static final int STARVATION_HURT_ROW = 11;
	public static final int ENDER_HURT_ROW = 12;
	private static final List<DamageOverlayRule> BUILT_IN_RULES = List.of(
		new DamageOverlayRule(FROST_HURT_ROW, "freeze", "frozen"),
		new DamageOverlayRule(FIRE_HURT_ROW, "in_fire", "on_fire", "lava", "hot_floor", "fireball", "burn"),
		new DamageOverlayRule(FROST_HURT_ROW, "drown"),
		new DamageOverlayRule(SHOCK_HURT_ROW, "lightning", "shock"),
		new DamageOverlayRule(STARVATION_HURT_ROW, "starve", "starvation"),
		new DamageOverlayRule(FIRE_HURT_ROW, "explosion", "fireworks"),
		new DamageOverlayRule(ARCANE_HURT_ROW, "out_of_world", "outside_border", "void", "generic_kill"),
		new DamageOverlayRule(ARCANE_HURT_ROW, "wither"),
		new DamageOverlayRule(ARCANE_HURT_ROW, "magic", "dragon_breath", "sonic_boom", "thorns"),
		new DamageOverlayRule(PLANT_HURT_ROW, "cactus", "sweet_berry")
	);
	private static final Map<String, String> BUILT_IN_DAMAGE_TYPE_OVERRIDES = createBuiltInDamageTypeOverrides();

	private DamageOverlayPalette() {
	}

	public static int colorForOverlayRow(int overlayRow) {
		return MindYourBruisesConfig.get().colorForOverlayRow(overlayRow);
	}

	public static Map<String, String> builtInDamageTypeOverrides() {
		return BUILT_IN_DAMAGE_TYPE_OVERRIDES;
	}

	public static Integer overlayRowForName(String overlayRowName) {
		if (overlayRowName == null) {
			return null;
		}

		return switch (overlayRowName.trim().toLowerCase(Locale.ROOT)) {
			case FIRE_GROUP_NAME -> FIRE_HURT_ROW;
			case FROST_GROUP_NAME -> FROST_HURT_ROW;
			case PLANT_GROUP_NAME -> PLANT_HURT_ROW;
			case FALLBACK_GROUP_NAME, "default", "vanilla" -> VANILLA_HURT_ROW;
			case ARCANE_GROUP_NAME -> ARCANE_HURT_ROW;
			case SHOCK_GROUP_NAME -> SHOCK_HURT_ROW;
			case STARVATION_GROUP_NAME -> STARVATION_HURT_ROW;
			case ENDER_GROUP_NAME -> ENDER_HURT_ROW;
			default -> null;
		};
	}

	public static int selectOverlayRow(ResourceLocation damageTypeId) {
		return selectOverlayRow(damageTypeId, null);
	}

	public static int selectOverlayRow(ResourceLocation damageTypeId, Integer compatibleOverlayRow) {
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
		if (compatibleOverlayRow != null) {
			return compatibleOverlayRow;
		}

		return builtInOverlayRowForPath(damageTypePath);
	}

	private static int builtInOverlayRowForPath(String damageTypePath) {
		for (DamageOverlayRule rule : BUILT_IN_RULES) {
			if (rule.matches(damageTypePath)) {
				return rule.overlayRow();
			}
		}

		return VANILLA_HURT_ROW;
	}

	private static Map<String, String> createBuiltInDamageTypeOverrides() {
		Map<String, String> overrides = new LinkedHashMap<>();
		overrides.put("minecraft:in_fire", FIRE_GROUP_NAME);
		overrides.put("minecraft:on_fire", FIRE_GROUP_NAME);
		overrides.put("minecraft:lava", FIRE_GROUP_NAME);
		overrides.put("minecraft:hot_floor", FIRE_GROUP_NAME);
		overrides.put("minecraft:freeze", FROST_GROUP_NAME);
		overrides.put("minecraft:cactus", PLANT_GROUP_NAME);
		overrides.put("minecraft:sweet_berry_bush", PLANT_GROUP_NAME);
		overrides.put("minecraft:magic", ARCANE_GROUP_NAME);
		overrides.put("minecraft:indirect_magic", ARCANE_GROUP_NAME);
		overrides.put("minecraft:wither", ARCANE_GROUP_NAME);
		overrides.put("minecraft:wither_skull", ARCANE_GROUP_NAME);
		overrides.put("minecraft:lightning_bolt", SHOCK_GROUP_NAME);
		overrides.put("minecraft:starve", STARVATION_GROUP_NAME);
		overrides.put("minecraft:dragon_breath", ARCANE_GROUP_NAME);
		overrides.put("minecraft:sonic_boom", ARCANE_GROUP_NAME);
		overrides.put("minecraft:thorns", ARCANE_GROUP_NAME);
		overrides.put("minecraft:explosion", FIRE_GROUP_NAME);
		overrides.put("minecraft:player_explosion", FIRE_GROUP_NAME);
		overrides.put("minecraft:fireworks", FIRE_GROUP_NAME);
		overrides.put("minecraft:drown", FROST_GROUP_NAME);
		overrides.put("minecraft:out_of_world", ARCANE_GROUP_NAME);
		overrides.put("minecraft:outside_border", ARCANE_GROUP_NAME);
		overrides.put("minecraft:generic_kill", ARCANE_GROUP_NAME);

		return Collections.unmodifiableMap(overrides);
	}

	private record DamageOverlayRule(int overlayRow, String... damageTypePathNeedles) {
		private boolean matches(String damageTypePath) {
			for (String damageTypePathNeedle : damageTypePathNeedles) {
				if (damageTypePath.contains(damageTypePathNeedle)) {
					return true;
				}
			}

			return false;
		}
	}
}
