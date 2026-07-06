package io.github.isquyet.mindyourbruises.client.compat;

import net.minecraft.resources.Identifier;

public final class DamageTintCompatibility {
	private static final boolean HAS_ACTIVE_COMPATIBILITY = BurnBySoulFireCompatibility.isLoaded();

	private DamageTintCompatibility() {
	}

	public static boolean hasActiveCompatibility() {
		return HAS_ACTIVE_COMPATIBILITY;
	}

	public static Integer overlayRowForDamage(int entityId, Identifier damageTypeId) {
		Integer burnBySoulFireOverlayRow = BurnBySoulFireCompatibility.overlayRowForDamage(entityId, damageTypeId);
		if (burnBySoulFireOverlayRow != null) {
			return burnBySoulFireOverlayRow;
		}

		return null;
	}
}
