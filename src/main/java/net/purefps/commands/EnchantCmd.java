/*

 *



 */
package net.purefps.commands;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.util.ChatUtils;
import net.purefps.util.ItemUtils;

public final class EnchantCmd extends Command
{
	public EnchantCmd()
	{
		super("enchant", "Enchants an item with everything,\n"
			+ "except for silk touch and curses.", ".enchant");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(!MC.player.getAbilities().creativeMode)
			throw new CmdError("Creative mode only.");

		if(args.length > 1)
			throw new CmdSyntaxError();

		enchant(getHeldItem(), 127);
		ChatUtils.message("Item enchanted.");
	}

	private ItemStack getHeldItem() throws CmdError
	{
		ItemStack stack = MC.player.getMainHandStack();

		if(stack.isEmpty())
			stack = MC.player.getOffHandStack();

		if(stack.isEmpty())
			throw new CmdError("There is no item in your hand.");

		return stack;
	}

	private void enchant(ItemStack stack, int level)
	{
		for(Enchantment enchantment : Registries.ENCHANTMENT)
		{
			// Skip curses
			// Skip Silk Touch so it doesn't remove Fortune
			if(enchantment.isCursed() || (enchantment == Enchantments.SILK_TOUCH))
				continue;

			// Limit Quick Charge to level 5 so it doesn't break
			if(enchantment == Enchantments.QUICK_CHARGE)
			{
				stack.addEnchantment(enchantment, Math.min(level, 5));
				continue;
			}

			ItemUtils.addEnchantment(stack, enchantment, level);
		}
	}

	@Override
	public String getPrimaryAction()
	{
		return "Enchant Held Item";
	}

	@Override
	public void doPrimaryAction()
	{
		WURST.getCmdProcessor().process("enchant");
	}
}
