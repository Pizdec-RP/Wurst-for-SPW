/*

 *



 */
package net.purefps.other_features;

import java.nio.file.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.purefps.Category;
import net.purefps.Feature;
import net.purefps.keybinds.Keybind;
import net.purefps.keybinds.KeybindList;
import net.purefps.module.Hack;
import net.purefps.other_feature.OtherFeature;
import net.purefps.settings.Setting;
import net.purefps.util.ChatUtils;
import net.purefps.util.json.JsonUtils;

public final class WikiDataExportOtf extends OtherFeature
{
	public WikiDataExportOtf()
	{
		super("WikiDataExport",
			"Creates a JSON file full of technical details about all the"
				+ " different features and settings in this version of Wurst."
				+ " Primarily used to update the Wurst Wiki.");
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Export Data";
	}
	
	@Override
	public void doPrimaryAction()
	{
		try
		{
			Path exportFile = WURST.getWurstFolder().resolve("wiki-data.json");
			
			JsonObject json = new JsonObject();
			for(Hack hack : WURST.getHax().getAllHax())
				json.add(hack.getName(), hackToJson(hack));
			
			JsonUtils.toJson(json, exportFile);
			
		}catch(Exception e)
		{
			ChatUtils.error("Failed to export data: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private JsonObject hackToJson(Hack hack)
	{
		JsonObject json = new JsonObject();
		json.addProperty("name", hack.getName());
		json.addProperty("descriptionKey", hack.getDescriptionKey());
		json.addProperty("type", "Hack");
		Category category = hack.getCategory();
		if(category != null)
			json.addProperty("category", category.getName());
		json.addProperty("keybind", getDefaultKeybind(hack));
		json.addProperty("stateSaved", hack.isStateSaved());
		json.addProperty("class", hack.getClass().getName());
		
		JsonArray settings = new JsonArray();
		for(Setting setting : hack.getSettings().values())
			settings.add(setting.exportWikiData());
		json.add("settings", settings);
		return json;
	}
	
	private String getDefaultKeybind(Feature feature)
	{
		String name = feature.getName().toLowerCase().replace(" ", "_");
		if(name.startsWith("."))
			name = name.substring(1);
		
		for(Keybind keybind : KeybindList.DEFAULT_KEYBINDS)
			if(keybind.getCommands().toLowerCase().contains(name))
				return keybind.getKey();
			
		return null;
	}
}
