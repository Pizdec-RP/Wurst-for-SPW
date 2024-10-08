/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.StatusEffectFogModifier;
import net.minecraft.entity.Entity;
import net.purefps.PFPSClient;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin
{
	@Inject(at = @At("HEAD"),
		method = "getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;",
		cancellable = true)
	private static void onGetFogModifier(Entity entity, float tickDelta,
		CallbackInfoReturnable<StatusEffectFogModifier> ci)
	{
		if(PFPSClient.INSTANCE.getHax().antiBlindHack.isEnabled())
			ci.setReturnValue(null);
	}
}
