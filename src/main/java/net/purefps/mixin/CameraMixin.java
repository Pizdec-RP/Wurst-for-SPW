/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.purefps.PFPSClient;
import net.purefps.modules.CameraDistanceHack;

@Mixin(Camera.class)
public abstract class CameraMixin
{
	@ModifyVariable(at = @At("HEAD"),
		method = "clipToSpace(D)D",
		argsOnly = true)
	private double changeClipToSpaceDistance(double desiredCameraDistance)
	{
		CameraDistanceHack cameraDistance =
			PFPSClient.INSTANCE.getHax().cameraDistanceHack;
		if(cameraDistance.isEnabled())
			return cameraDistance.getDistance();

		return desiredCameraDistance;
	}

	@Inject(at = @At("HEAD"), method = "clipToSpace(D)D", cancellable = true)
	private void onClipToSpace(double desiredCameraDistance,
		CallbackInfoReturnable<Double> cir)
	{
		if(PFPSClient.INSTANCE.getHax().cameraNoClipHack.isEnabled())
			cir.setReturnValue(desiredCameraDistance);
	}

	@Inject(at = @At("HEAD"),
		method = "getSubmersionType()Lnet/minecraft/client/render/CameraSubmersionType;",
		cancellable = true)
	private void onGetSubmersionType(
		CallbackInfoReturnable<CameraSubmersionType> cir)
	{
		if(PFPSClient.INSTANCE.getHax().noOverlayHack.isEnabled())
			cir.setReturnValue(CameraSubmersionType.NONE);
	}
}
