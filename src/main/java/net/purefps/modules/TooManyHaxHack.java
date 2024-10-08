/*

 *



 */
package net.purefps.modules;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.purefps.Category;
import net.purefps.DontBlock;
import net.purefps.Feature;
import net.purefps.SearchTags;
import net.purefps.TMHFile;
import net.purefps.module.Hack;
import net.purefps.util.json.JsonException;

@SearchTags({"too many hax", "TooManyHacks", "too many hacks", "YesCheat+",
	"YesCheatPlus", "yes cheat plus"})
@DontBlock
public final class TooManyHaxHack extends Hack
{
	private final ArrayList<Feature> blockedFeatures = new ArrayList<>();
	private final Path profilesFolder;
	private final TMHFile file;

	public TooManyHaxHack()
	{
		super("TooManyHax");
		setCategory(Category.OTHER);

		Path wurstFolder = WURST.getWurstFolder();
		profilesFolder = wurstFolder.resolve("toomanyhax");

		Path filePath = wurstFolder.resolve("toomanyhax.json");
		file = new TMHFile(filePath, blockedFeatures);
	}

	public void loadBlockedHacksFile()
	{
		file.load();
	}

	@Override
	public String getRenderName()
	{
		return getName() + " [" + blockedFeatures.size() + " blocked]";
	}

	@Override
	protected void onEnable()
	{
		disableBlockedHacks();
	}

	private void disableBlockedHacks()
	{
		for(Feature feature : blockedFeatures)
		{
			if(!(feature instanceof Hack))
				continue;

			((Hack)feature).setEnabled(false);
		}
	}

	public ArrayList<Path> listProfiles()
	{
		if(!Files.isDirectory(profilesFolder))
			return new ArrayList<>();

		try(Stream<Path> files = Files.list(profilesFolder))
		{
			return files.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(ArrayList::new));

		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void loadProfile(String fileName) throws IOException, JsonException
	{
		file.loadProfile(profilesFolder.resolve(fileName));
		disableBlockedHacks();
	}

	public void saveProfile(String fileName) throws IOException, JsonException
	{
		file.saveProfile(profilesFolder.resolve(fileName));
	}

	public boolean isBlocked(Feature feature)
	{
		return blockedFeatures.contains(feature);
	}

	public void setBlocked(Feature feature, boolean blocked)
	{
		if(blocked)
		{
			if(!feature.isSafeToBlock())
				throw new IllegalArgumentException();

			blockedFeatures.add(feature);
			blockedFeatures
				.sort(Comparator.comparing(f -> f.getName().toLowerCase()));

		}else
			blockedFeatures.remove(feature);

		file.save();
	}

	public void blockAll()
	{
		blockedFeatures.clear();

		ArrayList<Feature> features = new ArrayList<>();
		features.addAll(WURST.getHax().getAllHax());
		features.addAll(WURST.getCmds().getAllCmds());
		features.addAll(WURST.getOtfs().getAllOtfs());

		for(Feature feature : features)
		{
			if(!feature.isSafeToBlock())
				continue;

			blockedFeatures.add(feature);
		}

		blockedFeatures
			.sort(Comparator.comparing(f -> f.getName().toLowerCase()));

		file.save();
	}

	public void unblockAll()
	{
		blockedFeatures.clear();
		file.save();
	}

	public List<Feature> getBlockedFeatures()
	{
		return Collections.unmodifiableList(blockedFeatures);
	}
}
