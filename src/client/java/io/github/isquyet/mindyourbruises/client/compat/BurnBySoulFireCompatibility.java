package io.github.isquyet.mindyourbruises.client.compat;

import io.github.isquyet.mindyourbruises.client.render.DamageOverlayPalette;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;

public final class BurnBySoulFireCompatibility {
	private static final String MOD_ID = "burnbysoulfire";
	private static final boolean LOADED = FabricLoader.getInstance().isModLoaded(MOD_ID);

	private BurnBySoulFireCompatibility() {
	}

	public static Integer overlayRowForDamage(int entityId, Identifier damageTypeId) {
		if (!LOADED || !isVanillaInFireDamage(damageTypeId) || !isEntityTouchingSoulFire(entityId)) {
			return null;
		}

		return DamageOverlayPalette.FROST_HURT_ROW;
	}

	private static boolean isVanillaInFireDamage(Identifier damageTypeId) {
		return damageTypeId != null && "minecraft:in_fire".equals(damageTypeId.toString());
	}

	private static boolean isEntityTouchingSoulFire(int entityId) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			return false;
		}

		Entity entity = minecraft.level.getEntity(entityId);
		if (entity == null) {
			return false;
		}

		return minecraft.level.getBlockStates(entity.getBoundingBox())
			.anyMatch(blockState -> blockState.getBlock() == Blocks.SOUL_FIRE);
	}
}
