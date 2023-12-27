/*

 *



 */
package net.purefps.modules.autofish;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.purefps.PFPSClient;
import net.purefps.settings.CheckboxSetting;
import net.purefps.util.ChatUtils;

public class ShallowWaterWarningCheckbox extends CheckboxSetting
{
	private boolean hasAlreadyWarned;

	public ShallowWaterWarningCheckbox()
	{
		super("Shallow water warning",
			"Displays a warning message in chat when you are fishing in shallow"
				+ " water.",
			true);
	}

	public void reset()
	{
		hasAlreadyWarned = false;
	}

	public void checkWaterAround(FishingBobberEntity bobber)
	{
		if(bobber.isOpenOrWaterAround(bobber.getBlockPos()))
		{
			hasAlreadyWarned = false;
			return;
		}

		if(isChecked() && !hasAlreadyWarned)
		{
			ChatUtils.warning("You are currently fishing in shallow water.");
			ChatUtils.message(
				"You can't get any treasure items while fishing like this.");

			if(!PFPSClient.INSTANCE.getHax().openWaterEspHack.isEnabled())
				ChatUtils.message("Use OpenWaterESP to find open water.");

			hasAlreadyWarned = true;
		}
	}
}
