package com.irrelevantknight.ultrahardcore;

import net.canarymod.Canary;
import net.canarymod.plugin.Plugin;

public class UltraHardcore extends Plugin
{

	@Override
	public void disable()
	{
		getLogman().logInfo(getName() + " has been disabled");
	}

	@Override
	public boolean enable()
	{
		Canary.hooks().registerListener(new PlayerHealthRegenListener(), this);
		getLogman().logInfo("Enabling " + getName() + " version " + getVersion() + " by " + getAuthor());
		return true;
	}

}
