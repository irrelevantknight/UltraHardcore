package com.irrelevantknight.ultrahardcore;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.PotionEffectAppliedHook;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.plugin.PluginListener;

public class PlayerHungerRegenPreventer implements PluginListener
{
	
	private UltraHardcore plugin;
	
	public PlayerHungerRegenPreventer(UltraHardcore instance)
	{
		plugin = instance;
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
			if (newHealth > hook.getPlayer().getMaxHealth())
			{
				hook.setCanceled();
			}
		}
		else if (newHealth < oldHealth)
		{
			int healthDifference = oldHealth - newHealth;
			hook.getPlayer().setMaxHealth(hook.getPlayer().getMaxHealth() - healthDifference);
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
				hook.getEntity().getPlayer().setMaxHealth(hook.getEntity().getPlayer().getMaxHealth() + heartsToAdd);
			}
		}
	}
	
	
}