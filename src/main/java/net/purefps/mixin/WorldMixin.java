/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.purefps.PFPSClient;
import net.purefps.modules.NoWeatherHack;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess, AutoCloseable
{
	@Inject(at = @At("HEAD"),
		method = "getRainGradient(F)F",
		cancellable = true)
	private void onGetRainGradient(float delta,
		CallbackInfoReturnable<Float> cir)
	{
		if(PFPSClient.INSTANCE.getHax().noWeatherHack.isRainDisabled())
			cir.setReturnValue(0F);
	}

	@Override
	public float getSkyAngle(float tickDelta)
	{
		NoWeatherHack noWeather = PFPSClient.INSTANCE.getHax().noWeatherHack;

		long timeOfDay = noWeather.isTimeChanged() ? noWeather.getChangedTime()
			: getLevelProperties().getTimeOfDay();

		return getDimension().getSkyAngle(timeOfDay);
	}

	@Override
	public int getMoonPhase()
	{
		NoWeatherHack noWeather = PFPSClient.INSTANCE.getHax().noWeatherHack;

		if(noWeather.isMoonPhaseChanged())
			return noWeather.getChangedMoonPhase();

		return getDimension().getMoonPhase(getLunarTime());
	}
}
