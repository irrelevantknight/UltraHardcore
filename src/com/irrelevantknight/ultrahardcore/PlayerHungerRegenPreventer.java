package com.irrelevantknight.ultrahardcore;

import java.util.HashMap;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.PotionEffectAppliedHook;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.plugin.PluginListener;

public class PlayerHungerRegenPreventer implements PluginListener
{
	
	private UltraHardcore plugin;
	HashMap<String, Integer> maxAllowedHealth = new HashMap<String, Integer>();
	
	public PlayerHungerRegenPreventer(UltraHardcore instance)
	{
		plugin = instance;
	}
	
	private void setMaxAllowedHealth(Player p, int health)
	{
		if (health > 20)
		{
			maxAllowedHealth.put(p.getName(), 20);
		}
		else
		{
			maxAllowedHealth.put(p.getName(), health);
		}
	}
	private int getMaxAllowedHealth(Player p)
	{
		return maxAllowedHealth.get(p.getName());
	}
	
	@HookHandler
	public void onPlayerLogin(ConnectionHook hook)
	{
		setMaxAllowedHealth(hook.getPlayer(),
				hook.getPlayer().getHealth());
	}

	//Prevent player from regenerating due to having enough hunger
	@HookHandler
	public void onPlayerHealthChange(HealthChangeHook hook)
	{
		int newHealth = hook.getNewValue();
		int oldHealth = hook.getOldValue();
		
		//Not working properly at the moment
		if (newHealth > oldHealth)
		{
			if (newHealth > getMaxAllowedHealth(hook.getPlayer()))
			{
				hook.setCanceled();
			}
		}
		else if (newHealth < oldHealth)
		{
			int healthDifference = oldHealth - newHealth;
			setMaxAllowedHealth(hook.getPlayer(),
					getMaxAllowedHealth(hook.getPlayer()) - healthDifference);
		}
	}
	
	@HookHandler
	public void onPotionEffectApplied(PotionEffectAppliedHook hook)
	{
		if (hook.getEntity() instanceof Player)
		{
			//If the potion is a regeneration potion
			if (hook.getPotionEffect().getPotionID() == 10)
			{
				//Calculate the amount of hearts to add
				int potionDuration = hook.getPotionEffect().getDuration();
				int potionAmplifier = hook.getPotionEffect().getAmplifier();
				int delayBetweenHealing = (int) (25 * Math.pow(0.5, potionAmplifier));
				int heartsToAdd = potionDuration / delayBetweenHealing;
				setMaxAllowedHealth(hook.getEntity().getPlayer(),
						getMaxAllowedHealth(hook.getEntity().getPlayer()) + heartsToAdd);
			}
		}
	}
	
	
}