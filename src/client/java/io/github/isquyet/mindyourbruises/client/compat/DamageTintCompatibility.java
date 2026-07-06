package io.github.isquyet.mindyourbruises.client.compat;

import net.minecraft.resources.Identifier;

public final class DamageTintCompatibility {
	private DamageTintCompatibility() {
	}

	public static Integer overlayRowForDamage(int entityId, Identifier damageTypeId) {
		Integer burnBySoulFireOverlayRow = BurnBySoulFireCompatibility.overlayRowForDamage(entityId, damageTypeId);
		if (burnBySoulFireOverlayRow != null) {
			return burnBySoulFireOverlayRow;
		}

		return null;
	}
}
