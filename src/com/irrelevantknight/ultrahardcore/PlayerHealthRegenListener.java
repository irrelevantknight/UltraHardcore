package com.irrelevantknight.ultrahardcore;

import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.plugin.PluginListener;

public class PlayerHealthRegenListener implements PluginListener
{
	//Don't allow players to regen health
	@HookHandler
	public void onPlayerHealthChange(HealthChangeHook hook)
	{
		if (hook.getNewValue() > hook.getOldValue())
		{
			hook.setCanceled();
		}
	}
}