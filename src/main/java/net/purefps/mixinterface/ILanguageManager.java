/*

 *



 */
package net.purefps.mixinterface;

import net.minecraft.client.resource.language.TranslationStorage;
import net.purefps.PFPSClient;

public interface ILanguageManager
{
	public TranslationStorage wurst_getEnglish();
	
	public static TranslationStorage getEnglish()
	{
		return ((ILanguageManager)PFPSClient.MC.getLanguageManager())
			.wurst_getEnglish();
	}
}
