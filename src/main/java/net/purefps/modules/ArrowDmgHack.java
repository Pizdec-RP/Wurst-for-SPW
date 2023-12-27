/*

 *



 */
package net.purefps.modules;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.StopUsingItemListener;
import net.purefps.module.Hack;
import net.purefps.settings.CheckboxSetting;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;

@SearchTags({"arrow dmg", "ArrowDamage", "arrow damage"})
public final class ArrowDmgHack extends Hack implements StopUsingItemListener
{
	private final SliderSetting packets = new SliderSetting("Packets",
		"description.purefps.setting.arrowdmg.packets", 200, 2, 7000, 20,
		ValueDisplay.INTEGER);

	private final CheckboxSetting yeetTridents =
		new CheckboxSetting("Trident yeet mode",
			"description.purefps.setting.arrowdmg.trident_yeet_mode", false);

	public ArrowDmgHack()
	{
		super("ArrowDMG");
		setCategory(Category.COMBAT);
		addSetting(packets);
		addSetting(yeetTridents);
	}

	@Override
	protected void onEnable()
	{
		EVENTS.add(StopUsingItemListener.class, this);
	}

	@Override
	protected void onDisable()
	{
		EVENTS.remove(StopUsingItemListener.class, this);
	}

	@Override
	public void onStopUsingItem()
	{
		ClientPlayerEntity player = MC.player;
		ClientPlayNetworkHandler netHandler = player.networkHandler;

		if(!isValidItem(player.getMainHandStack().getItem()))
			return;

		netHandler.sendPacket(
			new ClientCommandC2SPacket(player, Mode.START_SPRINTING));

		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();

		for(int i = 0; i < packets.getValueI() / 2; i++)
		{
			netHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x,
				y - 1e-10, z, true));
			netHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x,
				y + 1e-10, z, false));
		}
	}

	private boolean isValidItem(Item item)
	{
		if(yeetTridents.isChecked() && item == Items.TRIDENT)
			return true;

		return item == Items.BOW;
	}
}
