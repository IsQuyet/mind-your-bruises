package io.github.isquyet.mindyourbruises.client.compat;

import io.github.isquyet.mindyourbruises.client.MindYourBruisesClient;
import io.github.isquyet.mindyourbruises.client.render.DamageOverlayPalette;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BurnBySoulFireCompatibility {
	private static final String MOD_ID = "burnbysoulfire";
	private static final String ENTITY_INTERFACE_CLASS_NAME = "aquariusplayz.burnbysoulfire.IEntity";
	private static final String IS_BURNING_BY_SOUL_FIRE_METHOD_NAME = "isBurningBySoulFire";
	private static final boolean LOADED = FabricLoader.getInstance().isModLoaded(MOD_ID);
	private static final SoulFireStateAccessor SOUL_FIRE_STATE_ACCESSOR = SoulFireStateAccessor.create();
	private static boolean reflectionFailureLogged = false;

	private BurnBySoulFireCompatibility() {
	}

	public static boolean isLoaded() {
		return LOADED;
	}

	public static Integer overlayRowForDamage(int entityId, ResourceLocation damageTypeId) {
		if (!LOADED || !isVanillaFireDamage(damageTypeId)) {
			return null;
		}

		Entity entity = resolveEntity(entityId);
		if (entity == null) {
			return null;
		}

		if (isEntityBurningBySoulFire(entity, damageTypeId)) {
			return DamageOverlayPalette.FROST_HURT_ROW;
		}

		return null;
	}

	private static boolean isVanillaFireDamage(ResourceLocation damageTypeId) {
		if (damageTypeId == null) {
			return false;
		}

		String damageTypeName = damageTypeId.toString();
		return "minecraft:in_fire".equals(damageTypeName) || "minecraft:on_fire".equals(damageTypeName);
	}

	private static Entity resolveEntity(int entityId) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			return null;
		}

		return minecraft.level.getEntity(entityId);
	}

	private static boolean isEntityBurningBySoulFire(Entity entity, ResourceLocation damageTypeId) {
		Boolean reflectedSoulFireState = reflectedSoulFireState(entity);
		if (reflectedSoulFireState != null && reflectedSoulFireState) {
			return true;
		}

		return isVanillaInFireDamage(damageTypeId) && isEntityTouchingSoulFire(entity);
	}

	private static Boolean reflectedSoulFireState(Entity entity) {
		if (SOUL_FIRE_STATE_ACCESSOR == null) {
			return null;
		}

		try {
			return SOUL_FIRE_STATE_ACCESSOR.isBurningBySoulFire(entity);
		} catch (IllegalAccessException | InvocationTargetException | LinkageError exception) {
			logReflectionFailure(exception);
			return null;
		}
	}

	private static boolean isVanillaInFireDamage(ResourceLocation damageTypeId) {
		return damageTypeId != null && "minecraft:in_fire".equals(damageTypeId.toString());
	}

	private static boolean isEntityTouchingSoulFire(Entity entity) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			return false;
		}

		return minecraft.level.getBlockStates(entity.getBoundingBox())
			.anyMatch(blockState -> blockState.getBlock() == Blocks.SOUL_FIRE);
	}

	private static void logReflectionFailure(Throwable exception) {
		if (reflectionFailureLogged) {
			return;
		}

		reflectionFailureLogged = true;
		MindYourBruisesClient.LOGGER.debug("Failed to read burn-by-soul-fire entity state. Falling back to soul fire contact detection.", exception);
	}

	private record SoulFireStateAccessor(Class<?> entityInterface, Method isBurningBySoulFireMethod) {
		private static SoulFireStateAccessor create() {
			if (!LOADED) {
				return null;
			}

			try {
				Class<?> entityInterface = Class.forName(ENTITY_INTERFACE_CLASS_NAME);
				Method isBurningBySoulFireMethod = entityInterface.getMethod(IS_BURNING_BY_SOUL_FIRE_METHOD_NAME);
				return new SoulFireStateAccessor(entityInterface, isBurningBySoulFireMethod);
			} catch (ClassNotFoundException | NoSuchMethodException | LinkageError exception) {
				MindYourBruisesClient.LOGGER.debug("burn-by-soul-fire is loaded, but its entity state API could not be found. Falling back to soul fire contact detection.", exception);
				return null;
			}
		}

		private Boolean isBurningBySoulFire(Entity entity) throws InvocationTargetException, IllegalAccessException {
			if (!entityInterface.isInstance(entity)) {
				return null;
			}

			Object result = isBurningBySoulFireMethod.invoke(entity);
			if (result instanceof Boolean isBurningBySoulFire) {
				return isBurningBySoulFire;
			}

			return null;
		}
	}
}
