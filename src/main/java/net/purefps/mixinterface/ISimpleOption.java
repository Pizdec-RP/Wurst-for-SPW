/*

 *



 */
package net.purefps.mixinterface;

import net.minecraft.client.option.SimpleOption;

public interface ISimpleOption<T>
{
	/**
	 * Forces the value of the option to the specified value, even if it's
	 * outside of the normal range.
	 */
	public void forceSetValue(T newValue);
	
	/**
	 * Returns the given SimpleOption object as an ISimpleOption, allowing you
	 * to access the forceSetValue() method.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ISimpleOption<T> get(SimpleOption<T> option)
	{
		return (ISimpleOption<T>)(Object)option;
	}
}
