/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.gui.screen.DisconnectedRealmsScreen;
import net.minecraft.client.realms.gui.screen.RealmsScreen;
import net.minecraft.text.Text;
import net.purefps.PFPSClient;
import net.purefps.nochatreports.ForcedChatReportsScreen;

@Mixin(DisconnectedRealmsScreen.class)
public class DisconnectedRealmsScreenMixin extends RealmsScreen
{
	@Shadow
	@Final
	private Text reason;
	@Shadow
	@Final
	private Screen parent;
	
	private DisconnectedRealmsScreenMixin(PFPSClient wurst, Text title)
	{
		super(title);
	}
	
	@Inject(at = @At("TAIL"), method = "init()V")
	private void onInit(CallbackInfo ci)
	{
		if(!PFPSClient.INSTANCE.isEnabled())
			return;
		
		System.out.println("Realms disconnected: " + reason);
		
		if(ForcedChatReportsScreen.isCausedByNoChatReports(reason))
			client.setScreen(new ForcedChatReportsScreen(parent));
	}
}
