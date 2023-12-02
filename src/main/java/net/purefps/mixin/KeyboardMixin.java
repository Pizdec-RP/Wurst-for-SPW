/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Keyboard;
import net.purefps.event.EventManager;
import net.purefps.events.KeyPressListener.KeyPressEvent;

@Mixin(Keyboard.class)
public class KeyboardMixin
{
	@Inject(at = @At("HEAD"), method = "onKey(JIIII)V")
	private void onOnKey(long windowHandle, int key, int scancode, int action,
		int modifiers, CallbackInfo ci)
	{
		EventManager.fire(new KeyPressEvent(key, scancode, action, modifiers));
	}
}
