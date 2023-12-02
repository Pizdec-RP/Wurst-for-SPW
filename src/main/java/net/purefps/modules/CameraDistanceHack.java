/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;

@SearchTags({"camera distance", "CamDistance", "cam distance"})
public final class CameraDistanceHack extends Hack
{
	private final SliderSetting distance =
		new SliderSetting("Distance", 12, -0.5, 150, 0.5, ValueDisplay.DECIMAL);
	
	public CameraDistanceHack()
	{
		super("CameraDistance");
		setCategory(Category.RENDER);
		addSetting(distance);
	}
	
	public double getDistance()
	{
		return distance.getValueF();
	}
	
	// See CameraMixin.changeClipToSpaceDistance()
}
