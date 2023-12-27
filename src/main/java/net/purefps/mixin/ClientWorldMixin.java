/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.world.GameMode;
import net.purefps.PFPSClient;

@Mixin(ClientWorld.class)
public class ClientWorldMixin
{
	@Shadow
	@Final
	private MinecraftClient client;

	/**
	 * This is the part that makes BarrierESP work.
	 */
	@Inject(at = @At("HEAD"),
		method = "getBlockParticle()Lnet/minecraft/block/Block;",
		cancellable = true)
	private void onGetBlockParticle(CallbackInfoReturnable<Block> cir)
	{
		// Pause BarrierESP when holding a light in Creative Mode, since it
		// would otherwise prevent the player from seeing light blocks.
		if(!PFPSClient.INSTANCE.getHax().barrierEspHack.isEnabled() || (client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE
			&& client.player.getMainHandStack().getItem() == Items.LIGHT))
			return;

		cir.setReturnValue(Blocks.BARRIER);
	}
}
