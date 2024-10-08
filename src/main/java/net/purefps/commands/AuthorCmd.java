/*

 *



 */
package net.purefps.commands;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtString;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;

public final class AuthorCmd extends Command
{
	public AuthorCmd()
	{
		super("author", "Changes the author of a written book.\n"
			+ "Requires creative mode.", ".author <author>");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length == 0)
			throw new CmdSyntaxError();

		if(!MC.player.getAbilities().creativeMode)
			throw new CmdError("Creative mode only.");

		ItemStack heldItem = MC.player.getInventory().getMainHandStack();
		int heldItemID = Item.getRawId(heldItem.getItem());
		int writtenBookID = Item.getRawId(Items.WRITTEN_BOOK);

		if(heldItemID != writtenBookID)
			throw new CmdError(
				"You must hold a written book in your main hand.");

		String author = String.join(" ", args);
		heldItem.setSubNbt("author", NbtString.of(author));
	}
}
