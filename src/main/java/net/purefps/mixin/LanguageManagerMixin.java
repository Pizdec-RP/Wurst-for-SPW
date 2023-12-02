/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.purefps.mixinterface.ILanguageManager;

@Mixin(LanguageManager.class)
public abstract class LanguageManagerMixin
	implements SynchronousResourceReloader, ILanguageManager
{
	private TranslationStorage wurstEnglish;
	
	@Inject(at = @At("HEAD"),
		method = "reload(Lnet/minecraft/resource/ResourceManager;)V")
	private void onReload(ResourceManager manager, CallbackInfo ci)
	{
		wurstEnglish = TranslationStorage.load(manager,
			Lists.newArrayList("en_us"), false);
	}
	
	@Override
	public TranslationStorage wurst_getEnglish()
	{
		return wurstEnglish;
	}
}
