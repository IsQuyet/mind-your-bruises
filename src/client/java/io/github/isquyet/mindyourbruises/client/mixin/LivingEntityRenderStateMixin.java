package io.github.isquyet.mindyourbruises.client.mixin;

import io.github.isquyet.mindyourbruises.client.render.DamageOverlayPalette;
import io.github.isquyet.mindyourbruises.client.render.DamageTintRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements DamageTintRenderState {
	@Unique
	private int mindyourbruises$hurtOverlayRow = DamageOverlayPalette.VANILLA_HURT_ROW;

	@Override
	public void mindyourbruises$setHurtOverlayRow(int hurtOverlayRow) {
		this.mindyourbruises$hurtOverlayRow = hurtOverlayRow;
	}

	@Override
	public int mindyourbruises$getHurtOverlayRow() {
		return this.mindyourbruises$hurtOverlayRow;
	}
}
