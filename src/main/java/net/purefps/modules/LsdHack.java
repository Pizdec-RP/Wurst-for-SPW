/*

 *



 */
package net.purefps.modules;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.purefps.Category;
import net.purefps.module.DontSaveState;
import net.purefps.module.Hack;

@DontSaveState
public final class LsdHack extends Hack
{
	public LsdHack()
	{
		super("LSD");
		setCategory(Category.FUN);
	}
	
	@Override
	public void onEnable()
	{
		if(!(MC.getCameraEntity() instanceof PlayerEntity))
		{
			setEnabled(false);
			return;
		}
		
		if(MC.gameRenderer.getPostProcessor() != null)
			MC.gameRenderer.disablePostProcessor();
		
		MC.gameRenderer
			.loadPostProcessor(new Identifier("shaders/post/wobble.json"));
	}
	
	@Override
	public void onDisable()
	{
		if(MC.gameRenderer.getPostProcessor() != null)
			MC.gameRenderer.disablePostProcessor();
	}
}
