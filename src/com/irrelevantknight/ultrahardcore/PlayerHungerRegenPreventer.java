package com.irrelevantknight.ultrahardcore;

import java.util.HashMap;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.potion.PotionEffectType;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.PotionEffectAppliedHook;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.plugin.PluginListener;

public class PlayerHungerRegenPreventer implements PluginListener
{
	private UltraHardcore plugin;
	HashMap<String, Integer> canRegen = new HashMap<String, Integer>();
	
	public PlayerHungerRegenPreventer(UltraHardcore instance)
	{
		plugin = instance;
	}
	
	//Prevent player from regenerating due to hunger
	@HookHandler
	public void onPlayerHealthChange(HealthChangeHook hook)
	{
		int oldHealth = hook.getOldValue();
		int newHealth = hook.getNewValue();
		if (oldHealth < newHealth)
		{
			if (!(canRegen.containsKey(hook.getPlayer().getName())))
			{
				setRegenTimes(hook.getPlayer(), 0);
			}
			if (getRegenTimes(hook.getPlayer()) < 1)
			{
				hook.setCanceled();
			}
			else
			{
				removeRegenTimes(hook.getPlayer(), 1);
			}
		}
	}
	
	//Set a healthPotionUsed to true when a health potion is used
	@HookHandler
	public void onPotionEffectApplied(PotionEffectAppliedHook hook)
	{
		if (hook.getEntity() instanceof Player)
		{
			plugin.getLogman().logInfo("POTION APPLIED");
			if (hook.getPotionEffect().getPotionID() == PotionEffectType.HEAL.getID())
			{
				addRegenTimes(hook.getEntity().getPlayer(), 1);
			}
			else if (hook.getPotionEffect().getPotionID() == PotionEffectType.REGENERATION.getID())
			{
				int potionDuration = hook.getPotionEffect().getDuration();
				int potionAmplifier = hook.getPotionEffect().getAmplifier();
				int delayBetweenHealing = (int) (25 * Math.pow(0.5, potionAmplifier));
				int timesToHeal = potionDuration / delayBetweenHealing;
				addRegenTimes(hook.getEntity().getPlayer(), timesToHeal);
			}
		}
	}
	
	//Some methods
	void addRegenTimes(Player p, int times)
	{
		canRegen.put(p.getName(), canRegen.get(p.getName()) + times);
	}
	void removeRegenTimes(Player p, int times)
	{
		canRegen.put(p.getName(), canRegen.get(p.getName()) - times);
	}
	void setRegenTimes(Player p, int times)
	{
		canRegen.put(p.getName(), times);
	}
	int getRegenTimes(Player p)
	{
		return canRegen.get(p.getName());
	}
}