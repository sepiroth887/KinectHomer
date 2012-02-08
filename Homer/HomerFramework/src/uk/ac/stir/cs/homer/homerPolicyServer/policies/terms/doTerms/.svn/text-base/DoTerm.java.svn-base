package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.doTerms;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;

public abstract class DoTerm
{
	protected Policy policy;
	protected TermType termType;
	protected List<DoTerm> children;
	
	public static DoTerm create(Policy policy, JSONObject json) throws JSONException
	{
		DoTerm t = null;
		JSONArray jsonToPassToChild = null;
		
		if (json.has(TermType.AND.name()))
		{
			t = new DoAndTerm();
			jsonToPassToChild = json.getJSONArray(TermType.AND.name());
		}
		else if (json.has(TermType.OR.name()))
		{
			t = new DoOrTerm();
			jsonToPassToChild = json.getJSONArray(TermType.OR.name());
		}
		else if (json.has(TermType.IF.name()))
		{
			t = new DoIfTerm(policy, json.getJSONObject(TermType.IF.name()));
			t.reset();		
			
			return t;
		}
		else 
		{
			t = new ActionOrConditionEventTerm(json);
		}
		
		t.setPolicy(policy);
		
		if (jsonToPassToChild!=null) t.children = DoTerm.createList(policy, jsonToPassToChild, t); 
		t.reset();		
		
		return t;
	}
	
	public static List<DoTerm> createList(Policy policy, JSONArray json,DoTerm parentTerm) throws JSONException
	{
		if (parentTerm.getTermType() != TermType.ACTION)
		{
			List<DoTerm> children = new ArrayList<DoTerm>();
			
			for (int index = 0; index < json.length(); index++)
			{
				children.add(DoTerm.create(policy, json.getJSONObject(index)));
			}
			
			return children;
		}
		return null;
	}
	
	public void setPolicy(Policy policy)
	{
		this.policy = policy;
	}
	
	public abstract void reset();
	public abstract TermType getTermType();

	public abstract void executeActions();
	public abstract boolean evaluate();

	public List<DoTerm> getChildren()
	{
		return children;
	}

	public boolean hasChildren()
	{
		return children!=null;
	}
}
