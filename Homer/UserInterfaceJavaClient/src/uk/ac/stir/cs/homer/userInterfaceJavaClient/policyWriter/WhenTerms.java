package uk.ac.stir.cs.homer.userInterfaceJavaClient.policyWriter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import utils.VerticalFlowLayout;

public class WhenTerms extends JPanel
{
	private final HomerDatabase database;
	private List<WhenTerm> whenTerms;
	
	public WhenTerms(HomerDatabase database)
	{
		this.database = database;
		this.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER));
		
		initialise();
	}

	private void initialise()
	{
		whenTerms = new ArrayList<WhenTerm>();
		WhenTerm starterTerm = new WhenTerm(this, database, true);
		whenTerms.add(starterTerm);
		this.add(starterTerm);
	}
	
	public void addNewTerm(WhenTerm insertAfter)
	{
		WhenTerm newTerm = new WhenTerm(this, database, false);
		int indexOfPreviousTerm = whenTerms.indexOf(insertAfter);
		
		whenTerms.add(indexOfPreviousTerm+1, newTerm);
		
		this.add(newTerm, indexOfPreviousTerm+1);
		this.validate();
		this.updateUI();
	}

	public JSONObject getJSON() throws JSONException
	{
		List<WhenTerm> copyOfWhenTerms = new ArrayList<WhenTerm>(whenTerms);
		return handleKids(copyOfWhenTerms);
	}

	private JSONObject handleKids(List<WhenTerm> kids) throws JSONException
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
			
			WhenTerm whenTerm = kids.get(index);
			
			if (whenTerm.getJoinType().equals(JoinType.OPEN))
			{
				List<WhenTerm> grandkids = new ArrayList<WhenTerm>(kids.subList(index, kids.size()));
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
