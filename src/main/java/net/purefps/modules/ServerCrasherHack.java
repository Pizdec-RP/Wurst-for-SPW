/*

 *



 */
package net.purefps.modules;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;
import net.purefps.util.ChatUtils;

@SearchTags({"server crasher", "ServerCrepperSpawnEgg",
	"server creeper spawn egg"})
public final class ServerCrasherHack extends Hack
{
	public ServerCrasherHack()
	{
		super("ServerCrasher");

		setCategory(Category.ITEMS);
	}

	@Override
	public void onEnable()
	{
		if(!MC.player.getAbilities().creativeMode)
		{
			ChatUtils.error("Creative mode only.");
			setEnabled(false);
			return;
		}

		Item item = Registries.ITEM.get(new Identifier("creeper_spawn_egg"));
		ItemStack stack = new ItemStack(item, 1);
		stack.setNbt(createNBT());

		placeStackInHotbar(stack);
		setEnabled(false);
	}

	private NbtCompound createNBT()
	{
		try
		{
			return StringNbtReader.parse(
				"{display:{Lore:['\"\u00a7r1. Place item in dispenser.\"','\"\u00a7r2. Dispense item.\"','\"\u00a7r3. Ssss... BOOM!\"'],Name:'{\"text\":\"\u00a7rServer Creeper\"}'},EntityTag:{CustomName:\"TEST\",id:\"Creeper\",CustomNameVisible:1}}");

		}catch(CommandSyntaxException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void placeStackInHotbar(ItemStack stack)
	{
		for(int i = 0; i < 9; i++)
		{
			if(!MC.player.getInventory().getStack(i).isEmpty())
				continue;

			MC.player.networkHandler.sendPacket(
				new CreativeInventoryActionC2SPacket(36 + i, stack));
			ChatUtils.message("Item created.");
			return;
		}

		ChatUtils.error("Please clear a slot in your hotbar.");
	}
}
