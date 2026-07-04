package io.github.isquyet.mindyourbruises.client.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import io.github.isquyet.mindyourbruises.client.MindYourBruisesClient;
import io.github.isquyet.mindyourbruises.client.render.DamageOverlayPalette;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {
	private static final int OVERLAY_TEXTURE_SIZE = 16;
	private static final int HURT_OVERLAY_ROW_COUNT = 8;

	@Shadow
	@Final
	private DynamicTexture texture;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void mindyourbruises$recolorHurtOverlayRows(CallbackInfo callbackInfo) {
		NativeImage pixels = this.texture.getPixels();
		if (pixels == null) {
			MindYourBruisesClient.LOGGER.warn("Could not recolor entity overlay texture because its pixels are unavailable.");
			return;
		}

		for (int hurtOverlayRow = 0; hurtOverlayRow < HURT_OVERLAY_ROW_COUNT; hurtOverlayRow++) {
			int rowColor = DamageOverlayPalette.colorForOverlayRow(hurtOverlayRow);
			for (int overlayColumn = 0; overlayColumn < OVERLAY_TEXTURE_SIZE; overlayColumn++) {
				pixels.setPixel(overlayColumn, hurtOverlayRow, rowColor);
			}
		}

		this.texture.upload();
	}
}
