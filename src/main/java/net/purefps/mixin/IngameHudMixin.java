/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import net.purefps.PFPSClient;
import net.purefps.event.EventManager;
import net.purefps.events.GUIRenderListener.GUIRenderEvent;

//import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public class IngameHudMixin
{
	//TODO PROBLEM
	@Shadow
	@Final
	private DebugHud debugHud;
	
	// runs after renderScoreboardSidebar()
	// and before playerListHud.setVisible()
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/gui/DrawContext;FLorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V")
	private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
		if(debugHud.shouldShowDebugHud())
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
