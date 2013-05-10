package com.irrelevantknight.ultrahardcore;

import java.util.HashMap;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.potion.PotionEffectType;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.PotionEffectAppliedHook;
import net.canarymod.hook.entity.PotionEffectFinishHook;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.plugin.PluginListener;

public class PlayerHungerRegenPreventer implements PluginListener
{
	HashMap<String, Integer> regenPotionPoints = new HashMap<String, Integer>();
	HashMap<String, Boolean> hadHealthPotion = new HashMap<String, Boolean>();
	HashMap<String, Boolean> isDead = new HashMap<String, Boolean>();
	
	//Prevent player from regenerating due to hunger
	@HookHandler
	public void onHealthChange(HealthChangeHook hook)
	{
		int oldHealth = hook.getOldValue();
		int newHealth = hook.getNewValue();
		if (oldHealth < newHealth)
		{
			if (!(regenPotionPoints.containsKey(hook.getPlayer().getName())))
			{
				setRegenTimes(hook.getPlayer(), 0);
			}
			if (!(hadHealthPotion.containsKey(hook.getPlayer().getName())))
			{
				setHadHealthPotion(hook.getPlayer(), false);
			}
			if (getIsDead(hook.getPlayer()))
			{
				setIsDead(hook.getPlayer(), false);
			}
			else if (getHadHealthPotion(hook.getPlayer()))
			{
				setHadHealthPotion(hook.getPlayer(), false);
			}
			else if (getRegenTimes(hook.getPlayer()) > 1)
			{
				removeRegenTimes(hook.getPlayer(), 1);
			}
			else
			{
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler
	public void onPotionEffectApplied(PotionEffectAppliedHook hook)
	{
		if (hook.getEntity() instanceof Player)
		{
			if (hook.getPotionEffect().getPotionID() == PotionEffectType.HEAL.getID())
			{
				setHadHealthPotion(hook.getEntity().getPlayer(), true);
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
	
	@HookHandler
	public void onPotionEffectFinish(PotionEffectFinishHook hook)
	{
		if (hook.getEntity() instanceof Player)
		{
			if (hook.getPotionEffect().getPotionID() == PotionEffectType.REGENERATION.getID())
			{
				setRegenTimes(hook.getEntity().getPlayer(), 0);
			}
		}
	}
	
	@HookHandler
	public void onDeath(PlayerDeathHook hook)
	{
		setIsDead(hook.getPlayer(), true);
	}
	
	@HookHandler
	public void onLogin (ConnectionHook hook)
	{
		if (hook.getPlayer().getDeathTicks() > 0)
		{
			setIsDead(hook.getPlayer(), true);
		}
	}
	
	//Some methods
	void addRegenTimes(Player p, int times)
	{
		regenPotionPoints.put(p.getName(), regenPotionPoints.get(p.getName()) + times);
	}
	void removeRegenTimes(Player p, int times)
	{
		regenPotionPoints.put(p.getName(), regenPotionPoints.get(p.getName()) - times);
	}
	void setRegenTimes(Player p, int times)
	{
		regenPotionPoints.put(p.getName(), times);
	}
	int getRegenTimes(Player p)
	{
		return regenPotionPoints.get(p.getName());
	}
	void setHadHealthPotion(Player p, boolean b)
	{
		hadHealthPotion.put(p.getName(), b);
	}
	boolean getHadHealthPotion(Player p)
	{
		return hadHealthPotion.get(p.getName());
	}
	void setIsDead(Player p, boolean b)
	{
		isDead.put(p.getName(), b);
	}
	boolean getIsDead(Player p)
	{
		return isDead.get(p.getName());
	}
}