/*

 *



 */
package net.purefps.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.purefps.PFPSClient;
import net.purefps.clickgui.Component;
import net.purefps.clickgui.components.BlockListEditButton;
import net.purefps.keybinds.PossibleKeybind;
import net.purefps.util.BlockUtils;
import net.purefps.util.json.JsonException;
import net.purefps.util.json.JsonUtils;
import net.purefps.util.json.WsonArray;

public final class BlockListSetting extends Setting
{
	private final ArrayList<String> blockNames = new ArrayList<>();
	private final String[] defaultNames;

	public BlockListSetting(String name, String description, String... blocks)
	{
		super(name, description);

		Arrays.stream(blocks).parallel()
			.map(s -> Registries.BLOCK.get(new Identifier(s)))
			.filter(Objects::nonNull).map(BlockUtils::getName).distinct()
			.sorted().forEachOrdered(s -> blockNames.add(s));
		defaultNames = blockNames.toArray(new String[0]);
	}

	public List<String> getBlockNames()
	{
		return Collections.unmodifiableList(blockNames);
	}

	public void add(Block block)
	{
		String name = BlockUtils.getName(block);
		if(Collections.binarySearch(blockNames, name) >= 0)
			return;

		blockNames.add(name);
		Collections.sort(blockNames);
		PFPSClient.INSTANCE.saveSettings();
	}

	public void remove(int index)
	{
		if(index < 0 || index >= blockNames.size())
			return;

		blockNames.remove(index);
		PFPSClient.INSTANCE.saveSettings();
	}

	public void resetToDefaults()
	{
		blockNames.clear();
		blockNames.addAll(Arrays.asList(defaultNames));
		PFPSClient.INSTANCE.saveSettings();
	}

	@Override
	public Component getComponent()
	{
		return new BlockListEditButton(this);
	}

	@Override
	public void fromJson(JsonElement json)
	{
		try
		{
			WsonArray wson = JsonUtils.getAsArray(json);
			blockNames.clear();

			wson.getAllStrings().parallelStream()
				.map(s -> Registries.BLOCK.get(new Identifier(s)))
				.filter(Objects::nonNull).map(BlockUtils::getName).distinct()
				.sorted().forEachOrdered(s -> blockNames.add(s));

		}catch(JsonException e)
		{
			e.printStackTrace();
			resetToDefaults();
		}
	}

	@Override
	public JsonElement toJson()
	{
		JsonArray json = new JsonArray();
		blockNames.forEach(s -> json.add(s));
		return json;
	}

	@Override
	public JsonObject exportWikiData()
	{
		JsonObject json = new JsonObject();

		json.addProperty("name", getName());
		json.addProperty("descriptionKey", getDescriptionKey());
		json.addProperty("type", "BlockList");

		JsonArray defaultBlocksJson = new JsonArray();
		for(String blockName : defaultNames)
			defaultBlocksJson.add(blockName);
		json.add("defaultBlocks", defaultBlocksJson);

		return json;
	}

	@Override
	public Set<PossibleKeybind> getPossibleKeybinds(String featureName)
	{
		String fullName = featureName + " " + getName();

		String command = ".blocklist " + featureName.toLowerCase() + " ";
		command += getName().toLowerCase().replace(" ", "_") + " ";

		LinkedHashSet<PossibleKeybind> pkb = new LinkedHashSet<>();
		// Can't just list all the blocks here. Would need to change UI to allow
		// user to choose a block after selecting this option.
		// pkb.add(new PossibleKeybind(command + "add dirt",
		// "Add dirt to " + fullName));
		// pkb.add(new PossibleKeybind(command + "remove dirt",
		// "Remove dirt from " + fullName));
		pkb.add(new PossibleKeybind(command + "reset", "Reset " + fullName));

		return pkb;
	}
}
