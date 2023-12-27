/*

 *



 */
package net.purefps.settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.purefps.PFPSClient;
import net.purefps.clickgui.Component;
import net.purefps.clickgui.components.FileComponent;
import net.purefps.keybinds.PossibleKeybind;
import net.purefps.util.json.JsonException;
import net.purefps.util.json.JsonUtils;

public final class FileSetting extends Setting
{
	private final Path folder;
	private String selectedFile = "";
	private final Consumer<Path> createDefaultFiles;

	public FileSetting(String name, String description, String folderName,
		Consumer<Path> createDefaultFiles)
	{
		super(name, description);
		folder = PFPSClient.INSTANCE.getWurstFolder().resolve(folderName);
		this.createDefaultFiles = createDefaultFiles;
		setSelectedFileToDefault();
	}

	public Path getFolder()
	{
		return folder;
	}

	public String getSelectedFileName()
	{
		return selectedFile;
	}

	public Path getSelectedFile()
	{
		return folder.resolve(selectedFile);
	}

	public void setSelectedFile(String selectedFile)
	{
		Objects.requireNonNull(selectedFile);

		Path newSelectedFile = folder.resolve(selectedFile);
		if(!Files.exists(newSelectedFile))
			return;

		this.selectedFile = selectedFile;
		PFPSClient.INSTANCE.saveSettings();
	}

	private void setSelectedFileToDefault()
	{
		ArrayList<Path> files = listFiles();

		if(files.isEmpty())
			files = createDefaultFiles();

		selectedFile = "" + files.get(0).getFileName();
	}

	private ArrayList<Path> createDefaultFiles()
	{
		try
		{
			Files.createDirectories(folder);

		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}

		createDefaultFiles.accept(folder);

		ArrayList<Path> files = listFiles();
		if(files.isEmpty())
			throw new IllegalStateException(
				"Created default files but folder is still empty!");

		return files;
	}

	public void resetFolder()
	{
		for(Path path : listFiles())
			try
			{
				Files.delete(path);

			}catch(IOException e)
			{
				throw new RuntimeException(e);
			}

		setSelectedFileToDefault();
		PFPSClient.INSTANCE.saveSettings();
	}

	public ArrayList<Path> listFiles()
	{
		if(!Files.isDirectory(folder))
			return new ArrayList<>();

		try(Stream<Path> files = Files.list(folder))
		{
			return files.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(ArrayList::new));

		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Component getComponent()
	{
		return new FileComponent(this);
	}

	@Override
	public void fromJson(JsonElement json)
	{
		try
		{
			String newFile = JsonUtils.getAsString(json);

			if(newFile.isEmpty() || !Files.exists(folder.resolve(newFile)))
				throw new JsonException();

			selectedFile = newFile;

		}catch(JsonException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public JsonElement toJson()
	{
		return new JsonPrimitive(selectedFile);
	}

	@Override
	public JsonObject exportWikiData()
	{
		JsonObject json = new JsonObject();
		json.addProperty("name", getName());
		json.addProperty("descriptionKey", getDescriptionKey());
		json.addProperty("type", "File");

		Path mcFolder = PFPSClient.INSTANCE.getWurstFolder().getParent();
		if(folder.startsWith(mcFolder))
			json.addProperty("folder", mcFolder.relativize(folder).toString());

		return json;
	}

	@Override
	public Set<PossibleKeybind> getPossibleKeybinds(String featureName)
	{
		return new LinkedHashSet<>();
	}
}
