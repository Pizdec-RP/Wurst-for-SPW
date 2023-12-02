/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerInventory;
import net.purefps.PFPSClient;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin
{
	@Inject(at = @At("HEAD"), method = "scrollInHotbar(D)V", cancellable = true)
	private void onScrollInHotbar(double scrollAmount, CallbackInfo ci)
	{
		if(PFPSClient.INSTANCE.getOtfs().zoomOtf
			.shouldPreventHotbarScrolling())
			ci.cancel();
	}
}
