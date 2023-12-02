/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.purefps.PFPSClient;
import net.purefps.mixinterface.IKeyBinding;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements IKeyBinding
{
	@Shadow
	private InputUtil.Key boundKey;
	
	@Override
	public boolean isActallyPressed()
	{
		long handle = PFPSClient.MC.getWindow().getHandle();
		int code = boundKey.getCode();
		return InputUtil.isKeyPressed(handle, code);
	}
	
	@Override
	public void resetPressedState()
	{
		setPressed(isActallyPressed());
	}
	
	@Shadow
	public abstract void setPressed(boolean pressed);
}
