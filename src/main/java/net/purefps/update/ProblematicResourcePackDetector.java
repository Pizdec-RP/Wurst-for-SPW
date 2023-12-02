/*

 *



 */
package net.purefps.update;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.purefps.PFPSClient;
import net.purefps.events.UpdateListener;
import net.purefps.util.ChatUtils;
import net.purefps.util.StreamUtils;

public final class ProblematicResourcePackDetector implements UpdateListener
{
	private static final String WARNING_MESSAGE =
		"VanillaTweaks \"Twinkling Stars\" pack detected. This resource pack is known to cause problems with Wurst!";
	
	private boolean running;
	
	public void start()
	{
		if(running)
			return;
		
		PFPSClient.INSTANCE.getEventManager().add(UpdateListener.class, this);
		running = true;
	}
	
	@Override
	public void onUpdate()
	{
		if(PFPSClient.INSTANCE.isEnabled() && isTwinklingStarsInstalled())
			ChatUtils.warning(WARNING_MESSAGE);
		
		PFPSClient.INSTANCE.getEventManager().remove(UpdateListener.class,
			this);
		running = false;
	}
	
	private boolean isTwinklingStarsInstalled()
	{
		Collection<ResourcePackProfile> enabledProfiles =
			PFPSClient.MC.getResourcePackManager().getEnabledProfiles();
		
		for(ResourcePackProfile profile : enabledProfiles)
		{
			if(!isVanillaTweaks(profile))
				continue;
			
			ResourcePack pack = profile.createResourcePack();
			if(!containsTwinklingStars(pack))
				continue;
			
			return true;
		}
		
		return false;
	}
	
	private boolean isVanillaTweaks(ResourcePackProfile profile)
	{
		return profile.getDescription().getString().contains("Vanilla Tweaks");
	}
	
	private boolean containsTwinklingStars(ResourcePack pack)
	{
		try
		{
			// some implementations of ResourcePack.openRoot() throw an
			// IllegalArgumentException when the pack doesn't contain the
			// specified file
			InputSupplier<InputStream> supplier =
				pack.openRoot("Selected Packs.txt");
			if(supplier == null)
				return false;
			
			ArrayList<String> lines = StreamUtils.readAllLines(supplier.get());
			
			return lines.stream()
				.anyMatch(line -> line.contains("TwinklingStars"));
			
		}catch(IOException | IllegalArgumentException e)
		{
			return false;
		}
	}
}
