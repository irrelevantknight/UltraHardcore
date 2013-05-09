package com.irrelevantknight.ultrahardcore;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.potion.PotionEffect;
import net.canarymod.api.potion.PotionEffectType;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.plugin.PluginListener;

public class PlayerHungerRegenPreventer implements PluginListener
{
	
	private UltraHardcore plugin;
	
	public PlayerHungerRegenPreventer(UltraHardcore instance)
	{
		plugin = instance;
	}
	
	boolean hasHealthOrRegen(Player p)
	{
		for (PotionEffect e : p.getAllActivePotionEffects())
		{
			if ((e.getPotionID() == PotionEffectType.REGENERATION.getID()) | e.getPotionID() == PotionEffectType.HEAL.getID())
			{
				return true;
			}
		}
		return false;
	}

	//Prevent player from regenerating due to having enough hunger
	@HookHandler
	public void onPlayerHealthChange(HealthChangeHook hook)
	{
		int newHealth = hook.getNewValue();
		int oldHealth = hook.getOldValue();	
		if ((oldHealth < newHealth) && !(hasHealthOrRegen(hook.getPlayer())))
		{
			hook.setCanceled();
		}
	}
}