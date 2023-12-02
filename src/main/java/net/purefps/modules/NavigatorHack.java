/*

 *



 */
package net.purefps.modules;

import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.module.DontSaveState;
import net.purefps.module.Hack;
import net.purefps.navigator.NavigatorMainScreen;

@DontSaveState
@DontBlock
@SearchTags({"ClickGUI", "click gui", "SearchGUI", "search gui", "HackMenu",
	"hack menu"})
public final class NavigatorHack extends Hack
{
	public NavigatorHack()
	{
		super("Navigator");
	}
	
	@Override
	public void onEnable()
	{
		if(!(MC.currentScreen instanceof NavigatorMainScreen))
			MC.setScreen(new NavigatorMainScreen());
		
		setEnabled(false);
	}
}
