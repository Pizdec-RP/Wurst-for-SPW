/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.session.telemetry.TelemetryManager;
import net.minecraft.client.session.telemetry.TelemetrySender;
import net.purefps.PFPSClient;

@Mixin(TelemetryManager.class)
public class TelemetryManagerMixin
{
	@Inject(at = @At("HEAD"),
		method = "getSender()Lnet/minecraft/client/session/telemetry/TelemetrySender;",
		cancellable = true)
	private void onGetSender(CallbackInfoReturnable<TelemetrySender> cir)
	{
		if(!PFPSClient.INSTANCE.getOtfs().noTelemetryOtf.isEnabled())
			return;
		
		// Return a dummy that can't actually send anything. :)
		cir.setReturnValue(TelemetrySender.NOOP);
	}
}
