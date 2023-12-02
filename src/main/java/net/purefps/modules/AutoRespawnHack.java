/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.PFPSClient;
import net.purefps.events.DeathListener;
import net.purefps.module.Hack;
import net.purefps.settings.CheckboxSetting;

@SearchTags({"auto respawn", "AutoRevive", "auto revive"})
public final class AutoRespawnHack extends Hack implements DeathListener
{
	private final CheckboxSetting button =
		new CheckboxSetting("Death screen button", "Shows a button on the death"
			+ " screen that lets you quickly enable AutoRespawn.", true);
	
	public AutoRespawnHack()
	{
		super("AutoRespawn");
		setCategory(Category.COMBAT);
		addSetting(button);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(DeathListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(DeathListener.class, this);
	}
	
	@Override
	public void onDeath()
	{
		MC.player.requestRespawn();
		MC.setScreen(null);
	}
	
	public boolean shouldShowButton()
	{
		return PFPSClient.INSTANCE.isEnabled() && !isEnabled()
			&& button.isChecked();
	}
}
