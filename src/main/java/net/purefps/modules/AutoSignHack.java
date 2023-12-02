/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.DontSaveState;
import net.purefps.module.Hack;

@SearchTags({"auto sign"})
@DontSaveState
public final class AutoSignHack extends Hack
{
	private String[] signText;
	
	public AutoSignHack()
	{
		super("AutoSign");
		setCategory(Category.BLOCKS);
	}
	
	@Override
	public void onDisable()
	{
		signText = null;
	}
	
	public String[] getSignText()
	{
		return signText;
	}
	
	public void setSignText(String[] signText)
	{
		if(isEnabled() && this.signText == null)
			this.signText = signText;
	}
}
