/*

 *



 */
package net.purefps.commands;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.purefps.SearchTags;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.util.ChatUtils;

@SearchTags({"view nbt", "NBTViewer", "nbt viewer"})
public final class ViewNbtCmd extends Command
{
	public ViewNbtCmd()
	{
		super("viewnbt", "Shows you the NBT data of an item.", ".viewnbt",
			"Copy to clipboard: .viewnbt copy");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		ClientPlayerEntity player = MC.player;
		ItemStack stack = player.getInventory().getMainHandStack();
		if(stack.isEmpty())
			throw new CmdError("You must hold an item in your main hand.");

		NbtCompound tag = stack.getNbt();
		String nbt = tag == null ? "" : tag.asString();

		switch(String.join(" ", args).toLowerCase())
		{
			case "":
			ChatUtils.message("NBT: " + nbt);
			break;

			case "copy":
			MC.keyboard.setClipboard(nbt);
			ChatUtils.message("NBT data copied to clipboard.");
			break;

			default:
			throw new CmdSyntaxError();
		}
	}
}
