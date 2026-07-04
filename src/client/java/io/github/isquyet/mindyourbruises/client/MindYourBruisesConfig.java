package io.github.isquyet.mindyourbruises.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.isquyet.mindyourbruises.client.render.DamageOverlayPalette;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MindYourBruisesConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("mind-your-bruises.json");
	private static final long MAX_CONFIG_SIZE_BYTES = 16384L;
	private static final boolean DEFAULT_ENABLED = true;
	private static final boolean DEFAULT_USE_STATUS_EFFECT_HINTS = true;

	private static final String DEFAULT_FIRE_COLOR = "#ff7014";
	private static final String DEFAULT_FROST_COLOR = "#4bd2ff";
	private static final String DEFAULT_TOXIC_COLOR = "#50dc3c";
	private static final String DEFAULT_FALLBACK_COLOR = "#ff0000";
	private static final String DEFAULT_ARCANE_COLOR = "#b950ff";
	private static final String DEFAULT_WATER_COLOR = "#3c78ff";
	private static final String DEFAULT_VOID_COLOR = "#2d005a";
	private static final String DEFAULT_WITHER_COLOR = "#d6d6c8";
	private static final String DEFAULT_SHOCK_COLOR = "#d8f6ff";
	private static final String DEFAULT_STARVATION_COLOR = "#9a7a32";
	private static final String DEFAULT_ENDER_COLOR = "#2bd6b3";

	private static MindYourBruisesConfig instance = new MindYourBruisesConfig();

	private Boolean enabled = DEFAULT_ENABLED;
	private Boolean useStatusEffectHints = DEFAULT_USE_STATUS_EFFECT_HINTS;
	private String fireColor = DEFAULT_FIRE_COLOR;
	private String frostColor = DEFAULT_FROST_COLOR;
	private String toxicColor = DEFAULT_TOXIC_COLOR;
	private String fallbackColor = DEFAULT_FALLBACK_COLOR;
	private String arcaneColor = DEFAULT_ARCANE_COLOR;
	private String waterColor = DEFAULT_WATER_COLOR;
	private String voidColor = DEFAULT_VOID_COLOR;
	private String witherColor = DEFAULT_WITHER_COLOR;
	private String shockColor = DEFAULT_SHOCK_COLOR;
	private String starvationColor = DEFAULT_STARVATION_COLOR;
	private String enderColor = DEFAULT_ENDER_COLOR;
	private Map<String, String> damageTypeOverrides = defaultDamageTypeOverrides();

	public static MindYourBruisesConfig get() {
		return instance;
	}

	public static void load() {
		if (Files.notExists(CONFIG_PATH)) {
			instance = new MindYourBruisesConfig();
			save();
			return;
		}

		try {
			long configSize = Files.size(CONFIG_PATH);
			if (configSize > MAX_CONFIG_SIZE_BYTES) {
				MindYourBruisesClient.LOGGER.warn("Mind Your Bruises config is too large ({} bytes). Backing it up and using defaults.", configSize);
				recoverWithDefaults();
				return;
			}

			MindYourBruisesConfig config;
			try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
				config = GSON.fromJson(reader, MindYourBruisesConfig.class);
			}

			if (config == null) {
				MindYourBruisesClient.LOGGER.warn("Mind Your Bruises config is empty or invalid. Backing it up and using defaults.");
				recoverWithDefaults();
				return;
			}

			boolean configChanged = config.normalizeDefaults();
			instance = config;
			if (configChanged) {
				save();
			}
		} catch (IOException | JsonParseException exception) {
			MindYourBruisesClient.LOGGER.warn("Failed to load Mind Your Bruises config. Backing it up and using defaults.", exception);
			recoverWithDefaults();
		}
	}

	private static void recoverWithDefaults() {
		instance = new MindYourBruisesConfig();
		if (backupConfig()) {
			save();
			return;
		}

		MindYourBruisesClient.LOGGER.warn("Using default Mind Your Bruises config for this session because the existing config could not be backed up.");
	}

	private static boolean backupConfig() {
		if (Files.notExists(CONFIG_PATH)) {
			return true;
		}

		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			Files.move(CONFIG_PATH, timestampedBackupPath());
			return true;
		} catch (IOException exception) {
			MindYourBruisesClient.LOGGER.warn("Failed to back up Mind Your Bruises config.", exception);
			return false;
		}
	}

	private static Path timestampedBackupPath() {
		return CONFIG_PATH.resolveSibling(CONFIG_PATH.getFileName() + "." + System.currentTimeMillis() + ".bak");
	}

	public static void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(instance, writer);
			}
		} catch (IOException exception) {
			MindYourBruisesClient.LOGGER.warn("Failed to save Mind Your Bruises config.", exception);
		}
	}

	public boolean enabled() {
		return enabled == null ? DEFAULT_ENABLED : enabled;
	}

	public boolean useStatusEffectHints() {
		return useStatusEffectHints == null ? DEFAULT_USE_STATUS_EFFECT_HINTS : useStatusEffectHints;
	}

	public int colorForOverlayRow(int overlayRow) {
		if (!enabled()) {
			return parseRgbColor(DEFAULT_FALLBACK_COLOR, DEFAULT_FALLBACK_COLOR);
		}

		return switch (overlayRow) {
			case DamageOverlayPalette.FIRE_HURT_ROW -> parseRgbColor(fireColor, DEFAULT_FIRE_COLOR);
			case DamageOverlayPalette.FROST_HURT_ROW -> parseRgbColor(frostColor, DEFAULT_FROST_COLOR);
			case DamageOverlayPalette.TOXIC_HURT_ROW -> parseRgbColor(toxicColor, DEFAULT_TOXIC_COLOR);
			case DamageOverlayPalette.ARCANE_HURT_ROW -> parseRgbColor(arcaneColor, DEFAULT_ARCANE_COLOR);
			case DamageOverlayPalette.WATER_HURT_ROW -> parseRgbColor(waterColor, DEFAULT_WATER_COLOR);
			case DamageOverlayPalette.VOID_HURT_ROW -> parseRgbColor(voidColor, DEFAULT_VOID_COLOR);
			case DamageOverlayPalette.WITHER_HURT_ROW -> parseRgbColor(witherColor, DEFAULT_WITHER_COLOR);
			case DamageOverlayPalette.SHOCK_HURT_ROW -> parseRgbColor(shockColor, DEFAULT_SHOCK_COLOR);
			case DamageOverlayPalette.STARVATION_HURT_ROW -> parseRgbColor(starvationColor, DEFAULT_STARVATION_COLOR);
			case DamageOverlayPalette.ENDER_HURT_ROW -> parseRgbColor(enderColor, DEFAULT_ENDER_COLOR);
			default -> parseRgbColor(fallbackColor, DEFAULT_FALLBACK_COLOR);
		};
	}

	public Integer overlayRowForDamageType(Identifier damageTypeId) {
		if (damageTypeId == null || damageTypeOverrides == null) {
			return null;
		}

		String overrideName = damageTypeOverrides.get(damageTypeId.toString().toLowerCase(Locale.ROOT));
		if (overrideName == null) {
			return null;
		}

		return overlayRowForName(overrideName);
	}

	private boolean normalizeDefaults() {
		boolean configChanged = false;
		if (enabled == null) {
			enabled = DEFAULT_ENABLED;
			configChanged = true;
		}

		if (useStatusEffectHints == null) {
			useStatusEffectHints = DEFAULT_USE_STATUS_EFFECT_HINTS;
			configChanged = true;
		}

		ColorNormalization fireColorNormalization = normalizeColor(fireColor, DEFAULT_FIRE_COLOR);
		fireColor = fireColorNormalization.color();
		configChanged |= fireColorNormalization.changed();

		ColorNormalization frostColorNormalization = normalizeColor(frostColor, DEFAULT_FROST_COLOR);
		frostColor = frostColorNormalization.color();
		configChanged |= frostColorNormalization.changed();

		ColorNormalization toxicColorNormalization = normalizeColor(toxicColor, DEFAULT_TOXIC_COLOR);
		toxicColor = toxicColorNormalization.color();
		configChanged |= toxicColorNormalization.changed();

		ColorNormalization fallbackColorNormalization = normalizeColor(fallbackColor, DEFAULT_FALLBACK_COLOR);
		fallbackColor = fallbackColorNormalization.color();
		configChanged |= fallbackColorNormalization.changed();

		ColorNormalization arcaneColorNormalization = normalizeColor(arcaneColor, DEFAULT_ARCANE_COLOR);
		arcaneColor = arcaneColorNormalization.color();
		configChanged |= arcaneColorNormalization.changed();

		ColorNormalization waterColorNormalization = normalizeColor(waterColor, DEFAULT_WATER_COLOR);
		waterColor = waterColorNormalization.color();
		configChanged |= waterColorNormalization.changed();

		ColorNormalization voidColorNormalization = normalizeColor(voidColor, DEFAULT_VOID_COLOR);
		voidColor = voidColorNormalization.color();
		configChanged |= voidColorNormalization.changed();

		ColorNormalization witherColorNormalization = normalizeColor(witherColor, DEFAULT_WITHER_COLOR);
		witherColor = witherColorNormalization.color();
		configChanged |= witherColorNormalization.changed();

		ColorNormalization shockColorNormalization = normalizeColor(shockColor, DEFAULT_SHOCK_COLOR);
		shockColor = shockColorNormalization.color();
		configChanged |= shockColorNormalization.changed();

		ColorNormalization starvationColorNormalization = normalizeColor(starvationColor, DEFAULT_STARVATION_COLOR);
		starvationColor = starvationColorNormalization.color();
		configChanged |= starvationColorNormalization.changed();

		ColorNormalization enderColorNormalization = normalizeColor(enderColor, DEFAULT_ENDER_COLOR);
		enderColor = enderColorNormalization.color();
		configChanged |= enderColorNormalization.changed();

		Map<String, String> normalizedOverrides = normalizeDamageTypeOverrides(damageTypeOverrides);
		if (!normalizedOverrides.equals(damageTypeOverrides)) {
			damageTypeOverrides = normalizedOverrides;
			configChanged = true;
		}

		return configChanged;
	}

	private static ColorNormalization normalizeColor(String color, String defaultColor) {
		if (color == null) {
			return new ColorNormalization(defaultColor, true);
		}

		String normalizedColor = color.trim().toLowerCase(Locale.ROOT);
		if (!normalizedColor.startsWith("#")) {
			normalizedColor = "#" + normalizedColor;
		}

		if (!isValidRgbColor(normalizedColor)) {
			return new ColorNormalization(defaultColor, true);
		}

		return new ColorNormalization(normalizedColor, !normalizedColor.equals(color));
	}

	private static Map<String, String> normalizeDamageTypeOverrides(Map<String, String> overrides) {
		if (overrides == null) {
			return defaultDamageTypeOverrides();
		}

		Map<String, String> normalizedOverrides = new LinkedHashMap<>();
		for (Map.Entry<String, String> override : overrides.entrySet()) {
			if (override.getKey() == null || override.getValue() == null) {
				continue;
			}

			String damageTypeId = override.getKey().trim().toLowerCase(Locale.ROOT);
			String overlayRowName = override.getValue().trim().toLowerCase(Locale.ROOT);
			if (damageTypeId.isEmpty() || overlayRowForName(overlayRowName) == null) {
				continue;
			}

			normalizedOverrides.put(damageTypeId, overlayRowName);
		}

		return normalizedOverrides;
	}

	private static Integer overlayRowForName(String overlayRowName) {
		if (overlayRowName == null) {
			return null;
		}

		return switch (overlayRowName.trim().toLowerCase(Locale.ROOT)) {
			case "fire" -> DamageOverlayPalette.FIRE_HURT_ROW;
			case "frost" -> DamageOverlayPalette.FROST_HURT_ROW;
			case "toxic" -> DamageOverlayPalette.TOXIC_HURT_ROW;
			case "fallback", "default", "vanilla" -> DamageOverlayPalette.VANILLA_HURT_ROW;
			case "arcane" -> DamageOverlayPalette.ARCANE_HURT_ROW;
			case "water" -> DamageOverlayPalette.WATER_HURT_ROW;
			case "void" -> DamageOverlayPalette.VOID_HURT_ROW;
			case "wither" -> DamageOverlayPalette.WITHER_HURT_ROW;
			case "shock" -> DamageOverlayPalette.SHOCK_HURT_ROW;
			case "starvation" -> DamageOverlayPalette.STARVATION_HURT_ROW;
			case "ender" -> DamageOverlayPalette.ENDER_HURT_ROW;
			default -> null;
		};
	}

	private static int parseRgbColor(String color, String defaultColor) {
		String normalizedColor = normalizeColor(color, defaultColor).color();
		int redGreenBlue = Integer.parseInt(normalizedColor.substring(1), 16);
		return (DamageOverlayPalette.VANILLA_HURT_OVERLAY_ALPHA << 24) | redGreenBlue;
	}

	private static boolean isValidRgbColor(String color) {
		if (color.length() != 7 || color.charAt(0) != '#') {
			return false;
		}

		for (int characterIndex = 1; characterIndex < color.length(); characterIndex++) {
			char character = color.charAt(characterIndex);
			boolean decimalDigit = character >= '0' && character <= '9';
			boolean lowercaseHexDigit = character >= 'a' && character <= 'f';
			if (!decimalDigit && !lowercaseHexDigit) {
				return false;
			}
		}

		return true;
	}

	private static Map<String, String> defaultDamageTypeOverrides() {
		Map<String, String> overrides = new LinkedHashMap<>();
		overrides.put("minecraft:in_fire", "fire");
		overrides.put("minecraft:on_fire", "fire");
		overrides.put("minecraft:lava", "fire");
		overrides.put("minecraft:hot_floor", "fire");
		overrides.put("minecraft:freeze", "frost");
		overrides.put("minecraft:cactus", "toxic");
		overrides.put("minecraft:sweet_berry_bush", "toxic");
		overrides.put("minecraft:sting", "toxic");
		overrides.put("minecraft:magic", "arcane");
		overrides.put("minecraft:indirect_magic", "arcane");
		overrides.put("minecraft:wither", "wither");
		overrides.put("minecraft:wither_skull", "wither");
		overrides.put("minecraft:lightning_bolt", "shock");
		overrides.put("minecraft:starve", "starvation");
		overrides.put("minecraft:ender_pearl", "ender");
		overrides.put("minecraft:dragon_breath", "arcane");
		overrides.put("minecraft:sonic_boom", "arcane");
		overrides.put("minecraft:thorns", "arcane");
		overrides.put("minecraft:explosion", "fire");
		overrides.put("minecraft:player_explosion", "fire");
		overrides.put("minecraft:fireworks", "fire");
		overrides.put("minecraft:drown", "water");
		overrides.put("minecraft:out_of_world", "void");
		overrides.put("minecraft:outside_border", "void");
		overrides.put("minecraft:generic_kill", "void");
		return overrides;
	}

	private record ColorNormalization(String color, boolean changed) {
	}
}
