/*

 *



 */
package net.purefps.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;
import net.purefps.PFPSClient;
import net.purefps.event.EventManager;
import net.purefps.events.AirStrafingSpeedListener.AirStrafingSpeedEvent;
import net.purefps.events.IsPlayerInLavaListener.IsPlayerInLavaEvent;
import net.purefps.events.IsPlayerInWaterListener.IsPlayerInWaterEvent;
import net.purefps.events.KnockbackListener.KnockbackEvent;
import net.purefps.events.PlayerMoveListener.PlayerMoveEvent;
import net.purefps.events.PostMotionListener.PostMotionEvent;
import net.purefps.events.PreMotionListener.PreMotionEvent;
import net.purefps.events.UpdateListener.UpdateEvent;
import net.purefps.mixinterface.IClientPlayerEntity;
import net.purefps.module.HackList;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity
	implements IClientPlayerEntity
{
	@Shadow
	@Final
	protected MinecraftClient client;

	private Screen tempCurrentScreen;
	private boolean hideNextItemUse;

	public ClientPlayerEntityMixin(PFPSClient wurst, ClientWorld world,
		GameProfile profile)
	{
		super(world, profile);
	}

	@Inject(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
		ordinal = 0), method = "tick()V")
	private void onTick(CallbackInfo ci)
	{
		EventManager.fire(UpdateEvent.INSTANCE);
	}

	/**
	 * This mixin runs just before the tickMovement() method calls
	 * isUsingItem(), so that the onIsUsingItem() mixin knows which
	 * call to intercept.
	 */
	@Inject(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z",
		ordinal = 0), method = "tickMovement()V")
	private void onTickMovementItemUse(CallbackInfo ci)
	{
		if(PFPSClient.INSTANCE.getHax().noSlowdownHack.isEnabled())
			hideNextItemUse = true;
	}

	/**
	 * Pretends that the player is not using an item when instructed to do so by
	 * the onTickMovement() mixin.
	 */
	@Inject(at = @At("HEAD"), method = "isUsingItem()Z", cancellable = true)
	private void onIsUsingItem(CallbackInfoReturnable<Boolean> cir)
	{
		if(!hideNextItemUse)
			return;

		cir.setReturnValue(false);
		hideNextItemUse = false;
	}

	/**
	 * This mixin is injected into a random field access later in the
	 * tickMovement() method to ensure that hideNextItemUse is always reset
	 * after the item use slowdown calculation.
	 */
	@Inject(at = @At(value = "FIELD",
		target = "Lnet/minecraft/client/network/ClientPlayerEntity;ticksToNextAutojump:I",
		opcode = Opcodes.GETFIELD,
		ordinal = 0), method = "tickMovement()V")
	private void afterIsUsingItem(CallbackInfo ci)
	{
		hideNextItemUse = false;
	}

	@Inject(at = @At("HEAD"), method = "sendMovementPackets()V")
	private void onSendMovementPacketsHEAD(CallbackInfo ci)
	{
		EventManager.fire(PreMotionEvent.INSTANCE);
	}

	@Inject(at = @At("TAIL"), method = "sendMovementPackets()V")
	private void onSendMovementPacketsTAIL(CallbackInfo ci)
	{
		EventManager.fire(PostMotionEvent.INSTANCE);
	}

	@Inject(at = @At("HEAD"),
		method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
	private void onMove(MovementType type, Vec3d offset, CallbackInfo ci)
	{
		EventManager.fire(PlayerMoveEvent.INSTANCE);
	}

	@Inject(at = @At("HEAD"),
		method = "isAutoJumpEnabled()Z",
		cancellable = true)
	private void onIsAutoJumpEnabled(CallbackInfoReturnable<Boolean> cir)
	{
		if(!PFPSClient.INSTANCE.getHax().stepHack.isAutoJumpAllowed())
			cir.setReturnValue(false);
	}

	/**
	 * When PortalGUI is enabled, this mixin temporarily sets the current screen
	 * to null to prevent the updateNausea() method from closing it.
	 */
	@Inject(at = @At(value = "FIELD",
		target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;",
		opcode = Opcodes.GETFIELD,
		ordinal = 0), method = "updateNausea()V")
	private void beforeUpdateNausea(CallbackInfo ci)
	{
		if(!PFPSClient.INSTANCE.getHax().portalGuiHack.isEnabled())
			return;

		tempCurrentScreen = client.currentScreen;
		client.currentScreen = null;
	}

	/**
	 * This mixin restores the current screen as soon as the updateNausea()
	 * method is done looking at it.
	 */
	@Inject(at = @At(value = "FIELD",
		target = "Lnet/minecraft/client/network/ClientPlayerEntity;nauseaIntensity:F",
		opcode = Opcodes.GETFIELD,
		ordinal = 1), method = "updateNausea()V")
	private void afterUpdateNausea(CallbackInfo ci)
	{
		if(tempCurrentScreen == null)
			return;

		client.currentScreen = tempCurrentScreen;
		tempCurrentScreen = null;
	}

	/**
	 * Getter method for what used to be airStrafingSpeed.
	 * Overridden to allow for the speed to be modified by hacks.
	 */
	@Override
	protected float getOffGroundSpeed()
	{
		AirStrafingSpeedEvent event =
			new AirStrafingSpeedEvent(super.getOffGroundSpeed());
		EventManager.fire(event);
		return event.getSpeed();
	}

	@Override
	public void setVelocityClient(double x, double y, double z)
	{
		KnockbackEvent event = new KnockbackEvent(x, y, z);
		EventManager.fire(event);
		super.setVelocityClient(event.getX(), event.getY(), event.getZ());
	}

	@Override
	public boolean isTouchingWater()
	{
		boolean inWater = super.isTouchingWater();
		IsPlayerInWaterEvent event = new IsPlayerInWaterEvent(inWater);
		EventManager.fire(event);

		return event.isInWater();
	}

	@Override
	public boolean isInLava()
	{
		boolean inLava = super.isInLava();
		IsPlayerInLavaEvent event = new IsPlayerInLavaEvent(inLava);
		EventManager.fire(event);

		return event.isInLava();
	}

	@Override
	public boolean isSpectator()
	{
		return super.isSpectator()
			|| PFPSClient.INSTANCE.getHax().freecamHack.isEnabled();
	}

	@Override
	public boolean isTouchingWaterBypass()
	{
		return super.isTouchingWater();
	}

	@Override
	protected float getJumpVelocity()
	{
		return super.getJumpVelocity()
			+ PFPSClient.INSTANCE.getHax().highJumpHack
				.getAdditionalJumpMotion();
	}

	/**
	 * This is the part that makes SafeWalk work.
	 */
	@Override
	protected boolean clipAtLedge()
	{
		return super.clipAtLedge()
			|| PFPSClient.INSTANCE.getHax().safeWalkHack.isEnabled();
	}

	/**
	 * This mixin allows SafeWalk to sneak visibly when the player is
	 * near a ledge.
	 */
	@Override
	protected Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type)
	{
		Vec3d result = super.adjustMovementForSneaking(movement, type);

		if(movement != null)
			PFPSClient.INSTANCE.getHax().safeWalkHack
				.onClipAtLedge(!movement.equals(result));

		return result;
	}

	@Override
	public boolean hasStatusEffect(StatusEffect effect)
	{
		HackList hax = PFPSClient.INSTANCE.getHax();

		if(effect == StatusEffects.NIGHT_VISION
			&& hax.fullbrightHack.isNightVisionActive())
			return true;

		if((effect == StatusEffects.LEVITATION
			&& hax.noLevitationHack.isEnabled()) || (effect == StatusEffects.DARKNESS && hax.antiBlindHack.isEnabled()))
			return false;

		return super.hasStatusEffect(effect);
	}
}
