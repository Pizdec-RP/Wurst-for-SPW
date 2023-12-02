/*

 *



 */
package net.purefps.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.entity.EntityLike;
import net.purefps.PFPSClient;
import net.purefps.event.EventManager;
import net.purefps.events.VelocityFromEntityCollisionListener.VelocityFromEntityCollisionEvent;
import net.purefps.events.VelocityFromFluidListener.VelocityFromFluidEvent;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput
{
	@Redirect(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
		opcode = Opcodes.INVOKEVIRTUAL,
		ordinal = 0),
		method = "updateMovementInFluid(Lnet/minecraft/registry/tag/TagKey;D)Z")
	private void setVelocityFromFluid(Entity entity, Vec3d velocity)
	{
		VelocityFromFluidEvent event =
			new VelocityFromFluidEvent((Entity)(Object)this);
		EventManager.fire(event);
		
		if(!event.isCancelled())
			entity.setVelocity(velocity);
	}
	
	@Inject(at = @At("HEAD"),
		method = "Lnet/minecraft/entity/Entity;pushAwayFrom(Lnet/minecraft/entity/Entity;)V",
		cancellable = true)
	private void onPushAwayFrom(Entity entity, CallbackInfo ci)
	{
		VelocityFromEntityCollisionEvent event =
			new VelocityFromEntityCollisionEvent((Entity)(Object)this);
		EventManager.fire(event);
		
		if(event.isCancelled())
			ci.cancel();
	}
	
	/**
	 * Makes invisible entities render as ghosts if TrueSight is enabled.
	 */
	@Inject(at = @At("RETURN"),
		method = "Lnet/minecraft/entity/Entity;isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z",
		cancellable = true)
	private void onIsInvisibleTo(PlayerEntity player,
		CallbackInfoReturnable<Boolean> cir)
	{
		// Return early if the entity is not invisible
		if(!cir.getReturnValueZ())
			return;
		
		if(PFPSClient.INSTANCE.getHax().trueSightHack
			.shouldBeVisible((Entity)(Object)this))
			cir.setReturnValue(false);
	}
}
