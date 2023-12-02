/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.StatsListener;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.purefps.PFPSClient;

@Mixin(StatsScreen.class)
public abstract class StatsScreenMixin extends Screen implements StatsListener
{
	private StatsScreenMixin(PFPSClient wurst, Text title)
	{
		super(title);
	}
	
	@Inject(at = @At("TAIL"), method = "createButtons()V")
	private void onCreateButtons(CallbackInfo ci)
	{
		if(PFPSClient.INSTANCE.getOtfs().disableOtf.shouldHideEnableButton())
			return;
		
		ButtonWidget toggleWurstButton =
			ButtonWidget.builder(Text.literal(""), this::toggleWurst)
				.dimensions(width / 2 - 152, height - 28, 150, 20).build();
		
		updateWurstButtonText(toggleWurstButton);
		addDrawableChild(toggleWurstButton);
		
		for(ClickableWidget button : Screens.getButtons(this))
		{
			if(!button.getMessage().getString()
				.equals(I18n.translate("gui.done")))
				continue;
			
			button.setX(width / 2 + 2);
			button.setWidth(150);
		}
	}
	
	private void toggleWurst(ButtonWidget button)
	{
		PFPSClient wurst = PFPSClient.INSTANCE;
		wurst.setEnabled(!wurst.isEnabled());
		
		updateWurstButtonText(button);
	}
	
	private void updateWurstButtonText(ButtonWidget button)
	{
		PFPSClient wurst = PFPSClient.INSTANCE;
		String text = (wurst.isEnabled() ? "Disable" : "Enable") + " Wurst";
		button.setMessage(Text.literal(text));
	}
}
