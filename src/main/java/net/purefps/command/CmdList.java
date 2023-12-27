/*

 *



 */
package net.purefps.command;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.TreeMap;

import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.purefps.commands.AddAltCmd;
import net.purefps.commands.AnnoyCmd;
import net.purefps.commands.AuthorCmd;
import net.purefps.commands.BindCmd;
import net.purefps.commands.BindsCmd;
import net.purefps.commands.BlinkCmd;
import net.purefps.commands.BlockListCmd;
import net.purefps.commands.ClearCmd;
import net.purefps.commands.CopyItemCmd;
import net.purefps.commands.DamageCmd;
import net.purefps.commands.DigCmd;
import net.purefps.commands.DropCmd;
import net.purefps.commands.EnabledHaxCmd;
import net.purefps.commands.EnchantCmd;
import net.purefps.commands.ExcavateCmd;
import net.purefps.commands.FeaturesCmd;
import net.purefps.commands.FollowCmd;
import net.purefps.commands.FriendsCmd;
import net.purefps.commands.GetPosCmd;
import net.purefps.commands.GiveCmd;
import net.purefps.commands.GmCmd;
import net.purefps.commands.GoToCmd;
import net.purefps.commands.HelpCmd;
import net.purefps.commands.InvseeCmd;
import net.purefps.commands.IpCmd;
import net.purefps.commands.ItemListCmd;
import net.purefps.commands.JumpCmd;
import net.purefps.commands.LeaveCmd;
import net.purefps.commands.ModifyCmd;
import net.purefps.commands.PathCmd;
import net.purefps.commands.PotionCmd;
import net.purefps.commands.ProtectCmd;
import net.purefps.commands.RenameCmd;
import net.purefps.commands.RepairCmd;
import net.purefps.commands.RvCmd;
import net.purefps.commands.SayCmd;
import net.purefps.commands.SetBlockCmd;
import net.purefps.commands.SetCheckboxCmd;
import net.purefps.commands.SetColorCmd;
import net.purefps.commands.SetModeCmd;
import net.purefps.commands.SetSliderCmd;
import net.purefps.commands.SettingsCmd;
import net.purefps.commands.SvCmd;
import net.purefps.commands.TCmd;
import net.purefps.commands.TacoCmd;
import net.purefps.commands.TooManyHaxCmd;
import net.purefps.commands.TpCmd;
import net.purefps.commands.UnbindCmd;
import net.purefps.commands.VClipCmd;
import net.purefps.commands.ViewNbtCmd;
import net.purefps.commands.XrayCmd;

public final class CmdList
{
	public final AddAltCmd addAltCmd = new AddAltCmd();
	public final AnnoyCmd annoyCmd = new AnnoyCmd();
	public final AuthorCmd authorCmd = new AuthorCmd();
	public final BindCmd bindCmd = new BindCmd();
	public final BindsCmd bindsCmd = new BindsCmd();
	public final BlinkCmd blinkCmd = new BlinkCmd();
	public final BlockListCmd blockListCmd = new BlockListCmd();
	public final ClearCmd clearCmd = new ClearCmd();
	public final CopyItemCmd copyitemCmd = new CopyItemCmd();
	public final DamageCmd damageCmd = new DamageCmd();
	public final DigCmd digCmd = new DigCmd();
	public final DropCmd dropCmd = new DropCmd();
	public final EnabledHaxCmd enabledHaxCmd = new EnabledHaxCmd();
	public final EnchantCmd enchantCmd = new EnchantCmd();
	public final ExcavateCmd excavateCmd = new ExcavateCmd();
	public final FeaturesCmd featuresCmd = new FeaturesCmd();
	public final FollowCmd followCmd = new FollowCmd();
	public final FriendsCmd friendsCmd = new FriendsCmd();
	public final GetPosCmd getPosCmd = new GetPosCmd();
	public final GiveCmd giveCmd = new GiveCmd();
	public final GmCmd gmCmd = new GmCmd();
	public final GoToCmd goToCmd = new GoToCmd();
	public final HelpCmd helpCmd = new HelpCmd();
	public final InvseeCmd invseeCmd = new InvseeCmd();
	public final IpCmd ipCmd = new IpCmd();
	public final ItemListCmd itemListCmd = new ItemListCmd();
	public final JumpCmd jumpCmd = new JumpCmd();
	public final LeaveCmd leaveCmd = new LeaveCmd();
	public final ModifyCmd modifyCmd = new ModifyCmd();
	public final PathCmd pathCmd = new PathCmd();
	public final PotionCmd potionCmd = new PotionCmd();
	public final ProtectCmd protectCmd = new ProtectCmd();
	public final RenameCmd renameCmd = new RenameCmd();
	public final RepairCmd repairCmd = new RepairCmd();
	public final RvCmd rvCmd = new RvCmd();
	public final SvCmd svCmd = new SvCmd();
	public final SayCmd sayCmd = new SayCmd();
	public final SetBlockCmd setBlockCmd = new SetBlockCmd();
	public final SetCheckboxCmd setCheckboxCmd = new SetCheckboxCmd();
	public final SetColorCmd setColorCmd = new SetColorCmd();
	public final SetModeCmd setModeCmd = new SetModeCmd();
	public final SetSliderCmd setSliderCmd = new SetSliderCmd();
	public final SettingsCmd settingsCmd = new SettingsCmd();
	public final TacoCmd tacoCmd = new TacoCmd();
	public final TCmd tCmd = new TCmd();
	public final TooManyHaxCmd tooManyHaxCmd = new TooManyHaxCmd();
	public final TpCmd tpCmd = new TpCmd();
	public final UnbindCmd unbindCmd = new UnbindCmd();
	public final VClipCmd vClipCmd = new VClipCmd();
	public final ViewNbtCmd viewNbtCmd = new ViewNbtCmd();
	public final XrayCmd xrayCmd = new XrayCmd();

	private final TreeMap<String, Command> cmds =
		new TreeMap<>(String::compareToIgnoreCase);

	public CmdList()
	{
		try
		{
			for(Field field : CmdList.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Cmd"))
					continue;

				Command cmd = (Command)field.get(this);
				cmds.put(cmd.getName(), cmd);
			}

		}catch(Exception e)
		{
			String message = "Initializing Wurst commands";
			CrashReport report = CrashReport.create(e, message);
			throw new CrashException(report);
		}
	}

	public Command getCmdByName(String name)
	{
		return cmds.get("." + name);
	}

	public Collection<Command> getAllCmds()
	{
		return cmds.values();
	}

	public int countCmds()
	{
		return cmds.size();
	}
}
