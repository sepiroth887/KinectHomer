package uk.ac.stir.cs.homer.userInterfaceJavaClient.policyWriter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import utils.VerticalFlowLayout;

public class DoTerms extends JPanel
{
	private final HomerDatabase database;
	private List<DoTerm> doTerms;
	
	public DoTerms(HomerDatabase database)
	{
		this.database = database;
		this.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER));
		
		initialise();
	}

	private void initialise()
	{
		doTerms = new ArrayList<DoTerm>();
		DoTerm starterTerm = new DoTerm(this, database, true);
		doTerms.add(starterTerm);
		this.add(starterTerm);
	}
	
	public void addNewTerm(DoTerm doTerm)
	{
		DoTerm newTerm = new DoTerm(this, database, false);
		int indexOfPreviousTerm = doTerms.indexOf(doTerm);
		
		doTerms.add(indexOfPreviousTerm+1, newTerm);
		
		this.add(newTerm, indexOfPreviousTerm+1);
		this.validate();
		this.updateUI();
	}

	public JSONObject getJSON() throws JSONException
	{
		List<DoTerm> copyOfWhenTerms = new ArrayList<DoTerm>(doTerms);
		return handleKids(copyOfWhenTerms);
	}

	private JSONObject handleKids(List<DoTerm> kids) throws JSONException
	{		
		JSONArray terms = new JSONArray();
		terms.put(kids.get(0).toJSON());

		JSONObject parent = new JSONObject();
		
		// last term or only one child		
		if (kids.size() == 1 || (kids.get(1).getJoinType().equals(JoinType.CLOSE)) )
		{
			parent.put(kids.get(0).getJoinOperator().name(), terms);
			return parent;
		}
		else
		{
			parent.put(kids.get(1).getJoinOperator().name(), terms);
		}
		
		int numTermsHandled = 1;
		
		for (int index = 1; index < kids.size(); index++)
		{
			// Here to take into account the terms which would have been handled as grandkids
			if (index!=numTermsHandled) continue;
			
			DoTerm whenTerm = kids.get(index);
			
			if (whenTerm.getJoinType().equals(JoinType.OPEN))
			{
				List<DoTerm> grandkids = new ArrayList<DoTerm>(kids.subList(index, kids.size()));
				terms.put(handleKids(grandkids)); 
				
				numTermsHandled+= (kids.size()-index-grandkids.size())-1;
			}
			else if (whenTerm.getJoinType().equals(JoinType.CLOSE))
			{
				break;
			}
			else 	
			{				
				numTermsHandled++;
				terms.put(whenTerm.toJSON());
			}	
		}
		
		for (int i = 0; i < numTermsHandled; i++)
		{
			kids.remove(0);
		}
		return parent;
	}
}
