package io.github.isquyet.mindyourbruises.client.mixin;

import io.github.isquyet.mindyourbruises.client.render.DamageOverlayPalette;
import io.github.isquyet.mindyourbruises.client.render.DamageTintRegistry;
import io.github.isquyet.mindyourbruises.client.render.DamageTintRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void mindyourbruises$storeDamageOverlayRow(LivingEntity entity, LivingEntityRenderState state, float tickProgress, CallbackInfo callbackInfo) {
		int hurtOverlayRow = DamageTintRegistry.getHurtOverlayRow(entity, state.hasRedOverlay);
		((DamageTintRenderState) state).mindyourbruises$setHurtOverlayRow(hurtOverlayRow);
	}

	@Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
	private static void mindyourbruises$useDamageOverlayRow(LivingEntityRenderState state, float whiteOverlayProgress, CallbackInfoReturnable<Integer> callbackInfo) {
		if (!state.hasRedOverlay) {
			return;
		}

		int hurtOverlayRow = ((DamageTintRenderState) state).mindyourbruises$getHurtOverlayRow();
		if (hurtOverlayRow == DamageOverlayPalette.VANILLA_HURT_ROW) {
			return;
		}

		callbackInfo.setReturnValue(OverlayTexture.pack(OverlayTexture.u(whiteOverlayProgress), hurtOverlayRow));
	}
}
