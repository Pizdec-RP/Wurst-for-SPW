/*

 *



 */
package net.purefps.other_feature;

import net.purefps.Feature;

public abstract class OtherFeature extends Feature
{
	private final String name;
	private final String description;
	
	public OtherFeature(String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	
	@Override
	public final String getName()
	{
		return name;
	}
	
	@Override
	public String getDescription()
	{
		return WURST.translate(description);
	}
	
	@Override
	public boolean isEnabled()
	{
		return false;
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "";
	}
	
	@Override
	public void doPrimaryAction()
	{
		
	}
}
