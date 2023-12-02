/*

 *



 */
package net.purefps.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Pattern;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.StringHelper;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.ChatInputListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.DontSaveState;
import net.purefps.module.Hack;
import net.purefps.settings.CheckboxSetting;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.TextFieldSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;
import net.purefps.util.ChatUtils;

@SearchTags({"mass tpa"})
@DontSaveState
public final class MassTpaHack extends Hack
	implements UpdateListener, ChatInputListener
{
	private static final Pattern ALLOWED_COMMANDS =
		Pattern.compile("^/+[a-zA-Z0-9_\\-]+$");
	
	private final TextFieldSetting commandSetting =
		new TextFieldSetting("Command",
			"The command to use for teleporting.\n"
				+ "Examples: /tp, /tpa, /tpahere, /tpo",
			"/tpa",
			s -> s.length() < 64 && ALLOWED_COMMANDS.matcher(s).matches());
	
	private final SliderSetting delay = new SliderSetting("Delay",
		"The delay between each teleportation request.", 20, 1, 200, 1,
		ValueDisplay.INTEGER.withSuffix(" ticks").withLabel(1, "1 tick"));
	
	private final CheckboxSetting ignoreErrors =
		new CheckboxSetting("Ignore errors",
			"Whether to ignore messages from the server telling you that the"
				+ " teleportation command isn't valid or that you don't have"
				+ " permission to use it.",
			false);
	
	private final CheckboxSetting stopWhenAccepted = new CheckboxSetting(
		"Stop when accepted", "Whether to stop sending more teleportation"
			+ " requests when someone accepts one of them.",
		true);
	
	private final Random random = new Random();
	private final ArrayList<String> players = new ArrayList<>();
	
	private String command;
	private int index;
	private int timer;
	
	public MassTpaHack()
	{
		super("MassTPA");
		setCategory(Category.CHAT);
		addSetting(commandSetting);
		addSetting(delay);
		addSetting(ignoreErrors);
		addSetting(stopWhenAccepted);
	}
	
	@Override
	public void onEnable()
	{
		// reset state
		players.clear();
		index = 0;
		timer = 0;
		
		// cache command in case the setting is changed mid-run
		command = commandSetting.getValue().substring(1);
		
		// collect player names
		String playerName = MC.getSession().getProfile().getName();
		for(PlayerListEntry info : MC.player.networkHandler.getPlayerList())
		{
			String name = info.getProfile().getName();
			name = StringHelper.stripTextFormat(name);
			
			if(name.equalsIgnoreCase(playerName))
				continue;
			
			players.add(name);
		}
		
		Collections.shuffle(players, random);
		
		EVENTS.add(ChatInputListener.class, this);
		EVENTS.add(UpdateListener.class, this);
		
		if(players.isEmpty())
		{
			ChatUtils.error("Couldn't find any players.");
			setEnabled(false);
		}
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(ChatInputListener.class, this);
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(timer > 0)
		{
			timer--;
			return;
		}
		
		if(index >= players.size())
		{
			setEnabled(false);
			return;
		}
		
		MC.getNetworkHandler()
			.sendChatCommand(command + " " + players.get(index));
		
		index++;
		timer = delay.getValueI() - 1;
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		String message = event.getComponent().getString().toLowerCase();
		if(message.startsWith("\u00a7c[\u00a76wurst\u00a7c]"))
			return;
		
		if(message.contains("/help") || message.contains("permission"))
		{
			if(ignoreErrors.isChecked())
				return;
			
			event.cancel();
			ChatUtils.error("This server doesn't have a "
				+ command.toUpperCase() + " command.");
			setEnabled(false);
			
		}else if(message.contains("accepted") && message.contains("request")
			|| message.contains("akzeptiert") && message.contains("anfrage"))
		{
			if(!stopWhenAccepted.isChecked())
				return;
			
			event.cancel();
			ChatUtils.message("Someone accepted your " + command.toUpperCase()
				+ " request. Stopping.");
			setEnabled(false);
		}
	}
}
