/*

 *



 */
package net.purefps;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeSet;

import com.google.gson.JsonArray;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.purefps.commands.FriendsCmd;
import net.purefps.settings.CheckboxSetting;
import net.purefps.util.json.JsonException;
import net.purefps.util.json.JsonUtils;

public class FriendsList
{
	private final TreeSet<String> friends = new TreeSet<>();
	private Path path;

	public FriendsList(Path path)
	{
		this.path = path;
	}

	public void addAndSave(String name)
	{
		friends.add(name);
		save();
	}

	public void removeAndSave(String name)
	{
		friends.remove(name);
		save();
	}

	public void removeAllAndSave()
	{
		friends.clear();
		save();
	}

	public void middleClick(Entity entity)
	{
		if(entity == null || !(entity instanceof PlayerEntity))
			return;

		FriendsCmd friendsCmd = PFPSClient.INSTANCE.getCmds().friendsCmd;
		CheckboxSetting middleClickFriends = friendsCmd.getMiddleClickFriends();
		if(!middleClickFriends.isChecked())
			return;

		String name = entity.getName().getString();

		if(contains(name))
			removeAndSave(name);
		else
			addAndSave(name);
	}

	public boolean contains(String name)
	{
		return friends.contains(name);
	}

	public boolean isFriend(Entity entity)
	{
		return entity != null && contains(entity.getName().getString());
	}

	public ArrayList<String> toList()
	{
		return new ArrayList<>(friends);
	}

	public void load()
	{
		try
		{
			friends.clear();
			friends.addAll(JsonUtils.parseFileToArray(path).getAllStrings());

		}catch(NoSuchFileException e)
		{
			// The file doesn't exist yet. No problem, we'll create it later.

		}catch(IOException | JsonException e)
		{
			System.out.println("Couldn't load " + path.getFileName());
			e.printStackTrace();
		}

		save();
	}

	private void save()
	{
		try
		{
			JsonUtils.toJson(createJson(), path);

		}catch(JsonException | IOException e)
		{
			System.out.println("Couldn't save " + path.getFileName());
			e.printStackTrace();
		}
	}

	private JsonArray createJson()
	{
		JsonArray json = new JsonArray();
		friends.forEach(json::add);
		return json;
	}
}
