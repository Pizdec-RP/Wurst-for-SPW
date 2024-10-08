/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.purefps.event.EventManager;
import net.purefps.events.ShouldDrawSideListener.ShouldDrawSideEvent;

@Pseudo
@Mixin(targets = {
	// current target
	"me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache",
	// < Sodium 0.5.0
	"me.jellysquid.mods.sodium.client.render.occlusion.BlockOcclusionCache"},
	remap = false)
public class SodiumBlockOcclusionCacheMixin
{
	/**
	 * This mixin hides and shows regular full blocks when using X-Ray with
	 * Sodium installed.
	 */
	@Inject(at = @At("HEAD"), method = "shouldDrawSide", cancellable = true)
	public void shouldDrawSide(BlockState state, BlockView world, BlockPos pos,
		Direction side, CallbackInfoReturnable<Boolean> cir)
	{
		ShouldDrawSideEvent event = new ShouldDrawSideEvent(state, pos);
		EventManager.fire(event);

		if(event.isRendered() != null)
			cir.setReturnValue(event.isRendered());
	}
}
