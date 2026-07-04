package io.github.isquyet.mindyourbruises.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.isquyet.mindyourbruises.client.MindYourBruisesClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MindYourBruisesModMenuIntegration implements ModMenuApi {
	private static final String YET_ANOTHER_CONFIG_LIB_MOD_ID = "yet_another_config_lib_v3";
	private static final String CONFIG_SCREEN_CLASS_NAME = "io.github.isquyet.mindyourbruises.client.config.MindYourBruisesConfigScreen";

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (!FabricLoader.getInstance().isModLoaded(YET_ANOTHER_CONFIG_LIB_MOD_ID)) {
			return parentScreen -> {
				MindYourBruisesClient.LOGGER.warn("Mind Your Bruises in-game config requires YetAnotherConfigLib. Edit config/mind-your-bruises.json manually and restart the game, or install YetAnotherConfigLib to use the Mod Menu config screen.");
				return parentScreen;
			};
		}

		return this::createConfigScreen;
	}

	private Screen createConfigScreen(Screen parentScreen) {
		try {
			Class<?> configScreenClass = Class.forName(CONFIG_SCREEN_CLASS_NAME);
			Method createMethod = configScreenClass.getMethod("create", Screen.class);
			Object configScreen = createMethod.invoke(null, parentScreen);
			if (configScreen instanceof Screen screen) {
				return screen;
			}

			MindYourBruisesClient.LOGGER.warn("Mind Your Bruises config screen factory returned an unexpected value. Falling back to the parent screen.");
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | LinkageError exception) {
			MindYourBruisesClient.LOGGER.warn("Failed to open the Mind Your Bruises in-game config screen. Edit config/mind-your-bruises.json manually and restart the game, or check that YetAnotherConfigLib is installed correctly.", exception);
		}

		return parentScreen;
	}
}
