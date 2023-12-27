/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.purefps.event.EventManager;
import net.purefps.events.RenderBlockEntityListener.RenderBlockEntityEvent;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin
{
	@Inject(at = @At("HEAD"),
		method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",
		cancellable = true)
	private <E extends BlockEntity> void onRender(E blockEntity,
		float tickDelta, MatrixStack matrices,
		VertexConsumerProvider vertexConsumers, CallbackInfo ci)
	{
		RenderBlockEntityEvent event = new RenderBlockEntityEvent(blockEntity);
		EventManager.fire(event);

		if(event.isCancelled())
			ci.cancel();
	}
}
