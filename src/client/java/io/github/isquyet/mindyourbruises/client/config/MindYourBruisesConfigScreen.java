package io.github.isquyet.mindyourbruises.client.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import io.github.isquyet.mindyourbruises.client.MindYourBruisesConfig;
import io.github.isquyet.mindyourbruises.client.render.DamageOverlayTextureRefresher;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MindYourBruisesConfigScreen {
	private MindYourBruisesConfigScreen() {
	}

	public static Screen create(Screen parentScreen) {
		MindYourBruisesConfig config = MindYourBruisesConfig.get();

		return YetAnotherConfigLib.createBuilder()
			.title(Component.translatable("mind-your-bruises.config.title"))
			.category(generalCategory(config))
			.category(advancedCategory(config))
			.save(() -> {
				MindYourBruisesConfig.save();
				DamageOverlayTextureRefresher.refresh();
			})
			.build()
			.generateScreen(parentScreen);
	}

	private static ConfigCategory generalCategory(MindYourBruisesConfig config) {
		return ConfigCategory.createBuilder()
			.name(Component.translatable("mind-your-bruises.config.category.general"))
			.tooltip(Component.translatable("mind-your-bruises.config.category.general.tooltip"))
			.group(OptionGroup.createBuilder()
				.name(Component.translatable("mind-your-bruises.config.group.behavior"))
				.option(Option.<Boolean>createBuilder()
					.name(Component.translatable("mind-your-bruises.config.option.enabled"))
					.description(OptionDescription.of(Component.translatable("mind-your-bruises.config.option.enabled.description")))
					.binding(true, config::enabled, config::setEnabled)
					.controller(TickBoxControllerBuilder::create)
					.build())
				.build())
			.group(OptionGroup.createBuilder()
				.name(Component.translatable("mind-your-bruises.config.group.colors"))
				.description(OptionDescription.of(Component.translatable("mind-your-bruises.config.group.colors.description")))
				.option(colorOption(
					"mind-your-bruises.config.color.fire",
					"mind-your-bruises.config.color.fire.description",
					MindYourBruisesConfig.defaultFireColor(),
					config::fireColor,
					config::setFireColor
				))
				.option(colorOption(
					"mind-your-bruises.config.color.frost",
					"mind-your-bruises.config.color.frost.description",
					MindYourBruisesConfig.defaultFrostColor(),
					config::frostColor,
					config::setFrostColor
				))
				.option(colorOption(
					"mind-your-bruises.config.color.plant",
					"mind-your-bruises.config.color.plant.description",
					MindYourBruisesConfig.defaultPlantColor(),
					config::plantColor,
					config::setPlantColor
				))
				.option(colorOption(
					"mind-your-bruises.config.color.arcane",
					"mind-your-bruises.config.color.arcane.description",
					MindYourBruisesConfig.defaultArcaneColor(),
					config::arcaneColor,
					config::setArcaneColor
				))
				.option(colorOption(
					"mind-your-bruises.config.color.shock",
					"mind-your-bruises.config.color.shock.description",
					MindYourBruisesConfig.defaultShockColor(),
					config::shockColor,
					config::setShockColor
				))
				.option(colorOption(
					"mind-your-bruises.config.color.starvation",
					"mind-your-bruises.config.color.starvation.description",
					MindYourBruisesConfig.defaultStarvationColor(),
					config::starvationColor,
					config::setStarvationColor
				))
				.option(colorOption(
					"mind-your-bruises.config.color.ender",
					"mind-your-bruises.config.color.ender.description",
					MindYourBruisesConfig.defaultEnderColor(),
					config::enderColor,
					config::setEnderColor
				))
				.option(colorOption(
					"mind-your-bruises.config.color.fallback",
					"mind-your-bruises.config.color.fallback.description",
					MindYourBruisesConfig.defaultFallbackColor(),
					config::fallbackColor,
					config::setFallbackColor
				))
				.build())
			.build();
	}

	private static ConfigCategory advancedCategory(MindYourBruisesConfig config) {
		return ConfigCategory.createBuilder()
			.name(Component.translatable("mind-your-bruises.config.category.advanced"))
			.tooltip(Component.translatable("mind-your-bruises.config.category.advanced.tooltip"))
			.option(ListOption.<String>createBuilder()
				.name(Component.translatable("mind-your-bruises.config.option.damage_type_overrides"))
				.description(OptionDescription.of(Component.translatable(
					"mind-your-bruises.config.option.damage_type_overrides.description",
					MindYourBruisesConfig.validColorGroupNames()
				)))
				.binding(MindYourBruisesConfig.defaultDamageTypeOverrideLines(), config::damageTypeOverrideLines, config::setDamageTypeOverrideLines)
				.controller(StringControllerBuilder::create)
				.initial("examplemod:thorny_vine=plant")
				.build())
			.build();
	}

	private static Option<Color> colorOption(String nameKey, String descriptionKey, String defaultColor, Supplier<String> getter, Consumer<String> setter) {
		return Option.<Color>createBuilder()
			.name(Component.translatable(nameKey))
			.description(OptionDescription.of(Component.translatable(descriptionKey)))
			.binding(parseColor(defaultColor), () -> parseColor(getter.get()), color -> setter.accept(formatColor(color)))
			.controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
			.build();
	}

	private static Color parseColor(String hexColor) {
		return new Color(Integer.parseInt(hexColor.substring(1), 16));
	}

	private static String formatColor(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
}
