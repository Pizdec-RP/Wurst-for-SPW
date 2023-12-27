/*

 *



 */
package net.purefps.modules.chestesp;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.purefps.settings.CheckboxSetting;
import net.purefps.settings.ColorSetting;
import net.purefps.util.EntityUtils;

public final class ChestEspEntityGroup extends ChestEspGroup
{
	private final ArrayList<Entity> entities = new ArrayList<>();

	public ChestEspEntityGroup(ColorSetting color, CheckboxSetting enabled)
	{
		super(color, enabled);
	}

	public void add(Entity e)
	{
		entities.add(e);
	}

	@Override
	public void clear()
	{
		entities.clear();
		super.clear();
	}

	public void updateBoxes(float partialTicks)
	{
		boxes.clear();

		for(Entity e : entities)
			boxes.add(EntityUtils.getLerpedBox(e, partialTicks));
	}
}
