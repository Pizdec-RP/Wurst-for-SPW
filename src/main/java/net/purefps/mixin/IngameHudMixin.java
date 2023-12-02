/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import net.purefps.PFPSClient;
import net.purefps.event.EventManager;
import net.purefps.events.GUIRenderListener.GUIRenderEvent;

@Mixin(InGameHud.class)
public class IngameHudMixin
{
	@Inject(
		at = @At(value = "INVOKE",
			target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
			remap = false,
			ordinal = 3),
		method = "render(Lnet/minecraft/client/gui/DrawContext;F)V")
	private void onRender(DrawContext context, float tickDelta, CallbackInfo ci)
	{
		if(PFPSClient.MC.options.debugEnabled)
			return;
		
		EventManager.fire(new GUIRenderEvent(context, tickDelta));
	}
	
	@Inject(at = @At("HEAD"),
		method = "renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
		cancellable = true)
	private void onRenderOverlay(DrawContext context, Identifier texture,
		float opacity, CallbackInfo ci)
	{
		if(texture == null
			|| !"textures/misc/pumpkinblur.png".equals(texture.getPath()))
			return;
		
		if(PFPSClient.INSTANCE.getHax().noPumpkinHack.isEnabled())
			ci.cancel();
	}
}
