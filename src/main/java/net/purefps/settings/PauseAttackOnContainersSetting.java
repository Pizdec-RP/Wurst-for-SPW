/*

 *



 */
package net.purefps.settings;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.purefps.PFPSClient;

public final class PauseAttackOnContainersSetting extends CheckboxSetting
{
	public PauseAttackOnContainersSetting(boolean checked)
	{
		super("Pause on containers",
			"description.wurst.setting.generic.pause_attack_on_containers",
			checked);
	}
	
	public PauseAttackOnContainersSetting(String name, String description,
		boolean checked)
	{
		super(name, description, checked);
	}
	
	public boolean shouldPause()
	{
		if(!isChecked())
			return false;
		
		Screen screen = PFPSClient.MC.currentScreen;
		
		return screen instanceof HandledScreen
			&& !(screen instanceof AbstractInventoryScreen);
	}
}
