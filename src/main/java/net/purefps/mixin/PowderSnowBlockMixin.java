/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.purefps.PFPSClient;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin extends Block
	implements FluidDrainable
{
	private PowderSnowBlockMixin(PFPSClient wurst, Settings settings)
	{
		super(settings);
	}
	
	@Inject(at = @At("HEAD"),
		method = "canWalkOnPowderSnow(Lnet/minecraft/entity/Entity;)Z",
		cancellable = true)
	private static void onCanWalkOnPowderSnow(Entity entity,
		CallbackInfoReturnable<Boolean> cir)
	{
		if(!PFPSClient.INSTANCE.getHax().snowShoeHack.isEnabled())
			return;
		
		if(entity == PFPSClient.MC.player)
			cir.setReturnValue(true);
	}
}
