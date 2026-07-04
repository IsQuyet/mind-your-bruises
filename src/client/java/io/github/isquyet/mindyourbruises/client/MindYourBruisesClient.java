package io.github.isquyet.mindyourbruises.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MindYourBruisesClient implements ClientModInitializer {
	public static final String MOD_ID = "mind-your-bruises";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		MindYourBruisesConfig.load();
		LOGGER.info("Mind Your Bruises initialized.");
	}
}
