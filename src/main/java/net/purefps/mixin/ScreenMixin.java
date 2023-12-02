/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.purefps.PFPSClient;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement
	implements Drawable
{
	@Inject(at = @At("HEAD"),
		method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;)V",
		cancellable = true)
	public void onRenderBackground(DrawContext context, CallbackInfo ci)
	{
		if(PFPSClient.INSTANCE.getHax().noBackgroundHack
			.shouldCancelBackground((Screen)(Object)this))
			ci.cancel();
	}
}
