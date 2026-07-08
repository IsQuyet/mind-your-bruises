package io.github.isquyet.mindyourbruises.client.render;

import io.github.isquyet.mindyourbruises.client.compat.DamageTintCompatibility;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public final class DamageTintRegistry {
	private static final boolean HAS_ACTIVE_COMPATIBILITY = DamageTintCompatibility.hasActiveCompatibility();
	private static final int MAX_TRACKED_ENTITIES = 512;
	private static final long TICKS_PER_SECOND = 20L;
	private static final long DAMAGE_TINT_TTL_TICKS = 2L * TICKS_PER_SECOND;

	private static final Map<Integer, DamageTintEntry> RECENT_DAMAGE_BY_ENTITY_ID = new LinkedHashMap<>() {
		@Override
		protected boolean removeEldestEntry(Map.Entry<Integer, DamageTintEntry> eldest) {
			return size() > MAX_TRACKED_ENTITIES;
		}
	};

	private DamageTintRegistry() {
	}

	public static void rememberDamage(int entityId, Holder<DamageType> damageTypeHolder) {
		Identifier damageTypeId = resolveDamageTypeId(damageTypeHolder);
		int hurtOverlayRow = selectHurtOverlayRow(entityId, damageTypeId);

		RECENT_DAMAGE_BY_ENTITY_ID.put(entityId, new DamageTintEntry(hurtOverlayRow, currentGameTime()));
	}

	public static int getHurtOverlayRow(LivingEntity entity, boolean hasHurtOverlay) {
		if (!hasHurtOverlay) {
			return DamageOverlayPalette.VANILLA_HURT_ROW;
		}

		DamageTintEntry damageTintEntry = RECENT_DAMAGE_BY_ENTITY_ID.get(entity.getId());
		if (damageTintEntry == null) {
			return DamageOverlayPalette.VANILLA_HURT_ROW;
		}

		long currentGameTime = currentGameTime();
		if (currentGameTime - damageTintEntry.gameTime() > DAMAGE_TINT_TTL_TICKS) {
			RECENT_DAMAGE_BY_ENTITY_ID.remove(entity.getId());
			return DamageOverlayPalette.VANILLA_HURT_ROW;
		}

		return damageTintEntry.hurtOverlayRow();
	}

	private static Identifier resolveDamageTypeId(Holder<DamageType> damageTypeHolder) {
		if (damageTypeHolder == null) {
			return null;
		}

		return damageTypeHolder.unwrapKey()
			.map(ResourceKey::identifier)
			.orElse(null);
	}

	private static int selectHurtOverlayRow(int entityId, Identifier damageTypeId) {
		if (!HAS_ACTIVE_COMPATIBILITY) {
			return DamageOverlayPalette.selectOverlayRow(damageTypeId);
		}

		Integer compatibleOverlayRow = DamageTintCompatibility.overlayRowForDamage(entityId, damageTypeId);
		return DamageOverlayPalette.selectOverlayRow(damageTypeId, compatibleOverlayRow);
	}

	private static long currentGameTime() {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			return 0L;
		}

		return minecraft.level.getGameTime();
	}

	private record DamageTintEntry(int hurtOverlayRow, long gameTime) {
	}
}
