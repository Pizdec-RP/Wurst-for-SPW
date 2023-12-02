/*

 *



 */
package net.purefps.commands;

import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.events.UpdateListener;

public final class DropCmd extends Command implements UpdateListener
{
	private int slowModeTimer;
	private int slowModeSlotCounter;
	
	public DropCmd()
	{
		super("drop", "Drops all your items on the ground.", ".drop",
			"Slow mode: .drop slow",
			"If regular .drop kicks you from the server,",
			"use slow mode instead.");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 1)
			throw new CmdSyntaxError();
		
		if(args.length < 1)
		{
			dropAllItems();
			return;
		}
		
		if(!args[0].equalsIgnoreCase("slow"))
			throw new CmdSyntaxError();
		
		slowModeTimer = 5;
		slowModeSlotCounter = 9;
		EVENTS.add(UpdateListener.class, this);
	}
	
	private void dropAllItems()
	{
		for(int i = 9; i < 45; i++)
			IMC.getInteractionManager().windowClick_THROW(i);
	}
	
	@Override
	public void onUpdate()
	{
		slowModeTimer--;
		if(slowModeTimer > 0)
			return;
		
		skipEmptySlots();
		IMC.getInteractionManager().windowClick_THROW(slowModeSlotCounter);
		
		slowModeSlotCounter++;
		slowModeTimer = 5;
		
		if(slowModeSlotCounter >= 45)
			EVENTS.remove(UpdateListener.class, this);
	}
	
	private void skipEmptySlots()
	{
		while(slowModeSlotCounter < 45)
		{
			int adjustedSlot = slowModeSlotCounter;
			if(adjustedSlot >= 36)
				adjustedSlot -= 36;
			
			if(!MC.player.getInventory().getStack(adjustedSlot).isEmpty())
				break;
			
			slowModeSlotCounter++;
		}
	}
}
