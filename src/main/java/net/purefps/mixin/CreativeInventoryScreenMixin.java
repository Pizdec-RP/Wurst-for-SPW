/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.CreativeScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.purefps.PFPSClient;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin
	extends AbstractInventoryScreen<CreativeScreenHandler>
{
	private CreativeInventoryScreenMixin(PFPSClient wurst,
		CreativeScreenHandler screenHandler, PlayerInventory inventory,
		Text title)
	{
		super(screenHandler, inventory, title);
	}

	@Inject(at = @At("HEAD"),
		method = "shouldShowOperatorTab(Lnet/minecraft/entity/player/PlayerEntity;)Z",
		cancellable = true)
	private void onShouldShowOperatorTab(PlayerEntity player,
		CallbackInfoReturnable<Boolean> cir)
	{
		if(PFPSClient.INSTANCE.isEnabled())
			cir.setReturnValue(true);
	}
}
