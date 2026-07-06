package io.github.isquyet.mindyourbruises.client.mixin;

import io.github.isquyet.mindyourbruises.client.render.DamageOverlayPalette;
import io.github.isquyet.mindyourbruises.client.render.DamageTintRegistry;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
	private static void mindyourbruises$useDamageOverlayRow(LivingEntity entity, float whiteOverlayProgress, CallbackInfoReturnable<Integer> callbackInfo) {
		int hurtOverlayRow = DamageTintRegistry.getHurtOverlayRow(entity, entity.hurtTime > 0 || entity.deathTime > 0);
		if (hurtOverlayRow == DamageOverlayPalette.VANILLA_HURT_ROW) {
			return;
		}

		callbackInfo.setReturnValue(OverlayTexture.pack(OverlayTexture.u(whiteOverlayProgress), hurtOverlayRow));
	}
}
