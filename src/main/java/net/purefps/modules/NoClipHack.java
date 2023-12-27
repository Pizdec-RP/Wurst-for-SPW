/*

 *



 */
package net.purefps.modules;

import net.minecraft.client.network.ClientPlayerEntity;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.AirStrafingSpeedListener;
import net.purefps.events.IsNormalCubeListener;
import net.purefps.events.PlayerMoveListener;
import net.purefps.events.SetOpaqueCubeListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({"no clip"})
public final class NoClipHack extends Hack
	implements UpdateListener, PlayerMoveListener, IsNormalCubeListener,
	SetOpaqueCubeListener, AirStrafingSpeedListener
{
	public NoClipHack()
	{
		super("NoClip");
		setCategory(Category.MOVEMENT);
	}

	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(PlayerMoveListener.class, this);
		EVENTS.add(IsNormalCubeListener.class, this);
		EVENTS.add(SetOpaqueCubeListener.class, this);
		EVENTS.add(AirStrafingSpeedListener.class, this);
	}

	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(PlayerMoveListener.class, this);
		EVENTS.remove(IsNormalCubeListener.class, this);
		EVENTS.remove(SetOpaqueCubeListener.class, this);
		EVENTS.remove(AirStrafingSpeedListener.class, this);

		MC.player.noClip = false;
	}

	@Override
	public void onUpdate()
	{
		ClientPlayerEntity player = MC.player;

		player.noClip = true;
		player.fallDistance = 0;
		player.setOnGround(false);

		player.getAbilities().flying = false;
		player.setVelocity(0, 0, 0);

		float speed = 0.2F;
		if(MC.options.jumpKey.isPressed())
			player.addVelocity(0, speed, 0);
		if(MC.options.sneakKey.isPressed())
			player.addVelocity(0, -speed, 0);
	}

	@Override
	public void onGetAirStrafingSpeed(AirStrafingSpeedEvent event)
	{
		event.setSpeed(0.2F);
	}

	@Override
	public void onPlayerMove()
	{
		MC.player.noClip = true;
	}

	@Override
	public void onIsNormalCube(IsNormalCubeEvent event)
	{
		event.cancel();
	}

	@Override
	public void onSetOpaqueCube(SetOpaqueCubeEvent event)
	{
		event.cancel();
	}
}
