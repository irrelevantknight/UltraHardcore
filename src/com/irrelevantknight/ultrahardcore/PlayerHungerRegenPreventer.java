package com.irrelevantknight.ultrahardcore;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.potion.PotionEffect;
import net.canarymod.api.potion.PotionEffectType;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.PotionEffectAppliedHook;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.plugin.PluginListener;

public class PlayerHungerRegenPreventer implements PluginListener
{
	
	private UltraHardcore plugin;
	boolean healthPotionUsed;
	
	public PlayerHungerRegenPreventer(UltraHardcore instance)
	{
		plugin = instance;
	}
	
	//Check if the player has a regen potion or has used a health potion
	boolean hasHealthOrRegen(Player p)
	{
		if (healthPotionUsed == true)
		{
			healthPotionUsed = false;
			return true;
		}
		else
		{
			for (PotionEffect e : p.getAllActivePotionEffects())
			{
				if (e.getPotionID() == PotionEffectType.REGENERATION.getID())
				{
					return true;
				}
			}
			return false;
		}
	}

	//Prevent player from regenerating due to having enough hunger
	@HookHandler
	public void onPlayerHealthChange(HealthChangeHook hook)
	{
		int newHealth = hook.getNewValue();
		int oldHealth = hook.getOldValue();	
		if (!(hasHealthOrRegen(hook.getPlayer())) && (oldHealth < newHealth))
		{
			hook.setCanceled();
		}
	}
	
	//Set a healthPotionUsed to true when a health potion is used
	@HookHandler
	public void onPotionEffectApplied(PotionEffectAppliedHook hook)
	{
		if (hook.getPotionEffect().getPotionID() == PotionEffectType.HEAL.getID())
		{
			healthPotionUsed = true;
		}
	}
}