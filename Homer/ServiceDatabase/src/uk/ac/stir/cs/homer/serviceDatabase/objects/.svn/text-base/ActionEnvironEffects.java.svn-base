package uk.ac.stir.cs.homer.serviceDatabase.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ActionEnvironEffects
{
	private final String userDeviceTypeID;
	private final String systemDeviceTypeID;
	
	private Map<String, Set<EnvironEffectRelationship>> effectsPerAction = new HashMap<String, Set<EnvironEffectRelationship>>();
	
	public ActionEnvironEffects(String userDeviceTypeID, String systemDeviceTypeID)
	{
		this.userDeviceTypeID = userDeviceTypeID;
		this.systemDeviceTypeID = systemDeviceTypeID;	
	}
	
	public void addRelationship(String actionID, Environ environ, EnvironEffect effect)
	{
		if (!effectsPerAction.containsKey(actionID))
		{
			Set<EnvironEffectRelationship> environEffects = new HashSet<EnvironEffectRelationship>();
			effectsPerAction.put(actionID, environEffects);
		}
		Map<Environ, EnvironEffect> environEffectsForAction = getEnvironEffectsForAction(actionID);
		if (environEffectsForAction.size() > 0 && environEffectsForAction.containsKey(environ))
		{
			updateRelationshipEffect(actionID, environ, effect.name());
		}
		else
		{
			effectsPerAction.get(actionID).add(new EnvironEffectRelationship(environ, effect));
		}
	}
	public void removeRelationship(String actionID, Environ environ)
	{
		Set<EnvironEffectRelationship> set = effectsPerAction.get(actionID);
		if (set!=null) set.remove(new EnvironEffectRelationship(environ, null));
	}
	
	public void updateRelationshipEffect(String actionID, Environ environ, String newEffect)
	{
		removeRelationship(actionID, environ);
		addRelationship(actionID, environ, EnvironEffect.valueOf(newEffect.toUpperCase()));
	}
	
	public void updateRelationshipEnviron(String actionID, Environ previousEnviron,
			Environ newEnviron, String effect)
	{
		removeRelationship(actionID, previousEnviron);
		addRelationship(actionID, newEnviron, EnvironEffect.valueOf(effect.toUpperCase()));
	}
	
	public String getSystemDeviceTypeID()
	{
		return systemDeviceTypeID;
	}
	public String getUserDeviceTypeID()
	{
		return userDeviceTypeID;
	}
	
	public String[] getActionIDs()
	{
		return effectsPerAction.keySet().toArray(new String[0]);
	}
	
	public Map<Environ, EnvironEffect> getEnvironEffectsForAction(String actionID)
	{
		Map<Environ, EnvironEffect> effects = new HashMap<Environ, EnvironEffect>();
		for (EnvironEffectRelationship envEffect : effectsPerAction.get(actionID))
		{
			effects.put(envEffect.getEnviron(), envEffect.getEffect());
		}
		return effects;
	}
	
	class EnvironEffectRelationship 
	{
		private final Environ environ;
		private final EnvironEffect effect;
		
		public EnvironEffectRelationship(Environ environ, EnvironEffect effect)
		{
			this.environ = environ;
			this.effect = effect;
		}
		
		public EnvironEffect getEffect()
		{
			return effect;
		}
		public Environ getEnviron()
		{
			return environ;
		}
		
		@Override
		public boolean equals(Object env)
		{
			if (env!= null && env instanceof EnvironEffectRelationship)
			{
				EnvironEffectRelationship other = (EnvironEffectRelationship)env;
				return other.getEnviron().equals(getEnviron());
			}
			return super.equals(env);
		}
		
		@Override
		public int hashCode()
		{
			return getEnviron().hashCode();
		}
	}


}
