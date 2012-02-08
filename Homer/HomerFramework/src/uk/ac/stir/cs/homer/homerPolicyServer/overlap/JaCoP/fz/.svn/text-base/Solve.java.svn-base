/**
 *  Solve.java 
 *  This file is part of JaCoP.
 *
 *  JaCoP is a Java Constraint Programming solver. 
 *	
 *	Copyright (C) 2000-2008 Krzysztof Kuchcinski and Radoslaw Szymanek
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  Notwithstanding any other provision of this License, the copyright
 *  owners of this work supplement the terms of this License with terms
 *  prohibiting misrepresentation of the origin of this work and requiring
 *  that modified versions of this work be marked in reasonable ways as
 *  different from the original version. This supplement of the license
 *  terms is in accordance with Section 7 of GNU Affero General Public
 *  License version 3.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.fz;

import java.util.ArrayList;

import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.Constraint;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XgtC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XltC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XplusYeqC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.BooleanVar;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntDomain;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntVar;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Store;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Var;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.ComparatorVariable;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.CreditCalculator;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.DepthFirstSearch;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.IndomainMin;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.LDS;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.PrintOutListener;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.Search;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.SelectChoicePoint;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.SimpleSelect;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.SimpleSolutionListener;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.set.core.SetVar;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.set.search.IndomainSetMin;


/**
 * 
 * The parser part responsible for parsing the solve part of the flatzinc file,
 * building a related search and executing it.
 *
 * Current implementation runs also final search on all variables to ensure
 * that they are ground.
 * 
 * @author Krzysztof Kuchcinski
 *
 */
public class Solve implements ParserTreeConstants {

    Tables dictionary;
    Options options;
    Store store;
    int initNumberConstraints;
    int NumberBoolVariables;

    ComparatorVariable tieBreaking=null;
    SelectChoicePoint<Var> variable_selection;
    ArrayList<Search<Var>> list_seq_searches = null;

    boolean debug = false;
    boolean print_search_info = false;
    boolean setSearch = false;
    boolean heuristicSeqSearch = false;
	
    Var costVariable;
	
    int costValue;

    Parser parser;

    /**
     * It creates a parser for the solve part of the flatzinc file. 
     * 
     * @param store the constraint store within which context the search will take place.
     */
    public Solve(Store store) {
	this.store = store;
    }

    /**
     * It parses the solve part. 
     * 
     * @param node the current parsing node.
     * @param table the table containing all the various variable definitions encoutered thus far.
     * @param opt option specifies to flatzinc parser in respect to search (e.g. all solutions). 
     */
    public void search(ASTSolveItem node, Tables table, Options opt) {

// 	System.out.println(table);

	initNumberConstraints = store.numberConstraints();

	if (opt.getVerbose())
	    System.out.println("Model constraints defined. Variables = "+store.size() + ", Bool variables = "+NumberBoolVariables +
			       ", Constraints = "+initNumberConstraints);

	dictionary = table;
	options = opt;
	int solveKind=-1;

	// node.dump("");

	ASTSolveKind kind=null;
	int count = node.jjtGetNumChildren();

	// 	System.out.println("Number constraints = "+store.numberConstraints());
	// 	System.out.println("Number  of variables = "+store.size());

	if (count == 1) {// only solve kind => default search

 	    new SearchItem(store, dictionary);
	    kind = (ASTSolveKind)node.jjtGetChild(0);
	    solveKind = getKind(kind.getKind());
	    run_single_search(solveKind, kind, null);
	}
	else if (count == 2) {// single annotation

	    SearchItem si = new SearchItem(store, dictionary);
	    si.searchParameters(node, 0);
	    // 	    System.out.println("*** "+si);
	    String search_type = si.type();

	    if (search_type.equals("int_search") || search_type.equals("set_search") ||
		search_type.equals("bool_search")) {

		kind = (ASTSolveKind)node.jjtGetChild(1);
		solveKind = getKind(kind.getKind());

		run_single_search(solveKind, kind, si);
	    }
	    else if (search_type.equals("seq_search")) {
		kind = (ASTSolveKind)node.jjtGetChild(1);
		solveKind = getKind(kind.getKind());

		run_sequence_search(solveKind, kind, si);
	    }
	    else {
		System.err.println("Not recognized structure of solve statement; compilation aborted");
		System.exit(0);
	    } 
	}
	else if (count > 2) {// several annotations
	    SearchItem si = new SearchItem(store, dictionary);
	    si.searchParametersForSeveralAnnotations(node, 0);
// 	    System.out.println("*** "+si +"\nsize="+si.search_seqSize());

 	    kind = (ASTSolveKind)node.jjtGetChild(si.search_seqSize());
 	    solveKind = getKind(kind.getKind());
// 	    System.out.println ("kind="+kind+" solveKind="+solveKind);

  	    run_sequence_search(solveKind, kind, si);
	}
	else
	    {
	    System.err.println("Not recognized structure of solve statement; compilation aborted");
	    System.exit(0);
	}
    }

    void run_single_search(int solveKind, SimpleNode kind, SearchItem si) {

	if (options.getVerbose()) {
	    String solve="notKnown";
	    switch (solveKind) {
	    case 0: 
		solve = "satisfy"; break; // satisfy
	    case 1: 
		solve = "minimize("+ getCost((ASTSolveExpr)kind.jjtGetChild(0))+") "; break; // minimize
	    case 2: 
		solve = "maximize("+ getCost((ASTSolveExpr)kind.jjtGetChild(0))+") "; break; // maximize
	    }
  	    System.out.println(solve + " : " + si);
	}

	IntVar cost = null;
	IntVar max_cost = null;
	DepthFirstSearch<Var> label = null;
	boolean optimization = false;


	DepthFirstSearch[] final_search = null;
	label = null;
	if (si != null) {
	    if (si.type().equals("int_search")) {
		label = int_search(si);
		label.setSolutionListener(new EmptyListener<Var>());
		label.setPrintInfo(false);

		// time-out option
		int to = options.getTimeOut();
		if (to > 0) 
		    label.setTimeOut(to);
	    }
	    else if (si.type().equals("bool_search")) {
		label = int_search(si);
		label.setSolutionListener(new EmptyListener<Var>());
		label.setPrintInfo(false);

		// time-out option
		int to = options.getTimeOut();
		if (to > 0) 
		    label.setTimeOut(to);
	    }
	    else if (si.type().equals("set_search")) {
		label = set_search(si);
		setSearch=true;
		label.setSolutionListener(new EmptyListener<Var>());
		label.setPrintInfo(false);

		// time-out option
		int to = options.getTimeOut();
		if (to > 0) 
		    label.setTimeOut(to);
	    }
	    else {
		System.err.println("Not recognized or supported search type \""+si.type()+"\"; compilation aborted");
		System.exit(0);
	    }
	}

	if (solveKind > 0) {
	    optimization = true;
	    cost = (IntVar) getCost((ASTSolveExpr)kind.jjtGetChild(0));
	    if ( solveKind == 1)  // minimize
		costVariable = cost; 
	    else { // maximize
		max_cost = new IntVar(store, "-"+cost.id(), IntDomain.MinInt, 
						 IntDomain.MaxInt);
		pose(new XplusYeqC(max_cost, cost, 0));
		costVariable = max_cost;
	    }
	}

	// adds child search for cost; to be sure that all variables get a value
	final_search = setSubSearchForAll(label, options.getAll());

	if (si == null) {
	    si = new SearchItem(store, dictionary);
	    si.explore = "complete";
	    if (final_search[0] != null) 
		label = final_search[0];
	    else if (final_search[1] != null)
		label = final_search[1];
	    else if (final_search[2] != null)
		label = final_search[2];	    
	}

	// LDS & Credit heuristic search
	if (si.exploration().equals("lds")) 
	    lds_search(label, si.ldsValue);
	// Credit heuristic search
	else if (si.exploration().equals("credit")) 
	    credit_search(label, si.creditValue, si.bbsValue);

	boolean Result = false;	

	Thread tread = java.lang.Thread.currentThread();
	java.lang.management.ThreadMXBean b = java.lang.management.ManagementFactory.getThreadMXBean();


	long startCPU = b.getThreadCpuTime(tread.getId());
	// 	long startUser = b.getThreadUserTime(tread.getId());

	if (si == null || si.exploration() == null || si.exploration().equals("complete") 
		|| si.exploration().equals("lds")
		|| si.exploration().equals("credit")
		)
	    switch (solveKind) {
	    case 0: // satisfy

		if (options.getAll()) { // all solutions
 		    label.getSolutionListener().searchAll(true); 
 		    label.getSolutionListener().recordSolutions(false);
		    if (options.getNumberSolutions()>0)
			label.getSolutionListener().setSolutionLimit(options.getNumberSolutions());
		}

		Result = label.labeling(store, variable_selection);
		break;
	    case 1: // minimize
// 		optimization = true;
// 		cost = (IntVar) getCost((ASTSolveExpr)kind.jjtGetChild(0));
// 		costVariable = cost;

		if (options.getNumberSolutions()>0) {
		    label.getSolutionListener().setSolutionLimit(options.getNumberSolutions());
		    label.respectSolutionLimitInOptimization=true;
		}

		Result = label.labeling(store, variable_selection, cost);
		break;
	    case 2: //maximize
// 		optimization = true;
// 		cost = (IntVar) getCost((ASTSolveExpr)kind.jjtGetChild(0));
// 		IntVar max_cost = new IntVar(store, "-"+cost.id(), IntDomain.MinInt, 
// 						 IntDomain.MaxInt);
// 		pose(new XplusYeqC(max_cost, cost, 0));

// 		costVariable = cost;

		if (options.getNumberSolutions()>0) {
		    label.getSolutionListener().setSolutionLimit(options.getNumberSolutions());
		    label.respectSolutionLimitInOptimization=true;
		}

		Result = label.labeling(store, variable_selection, max_cost);
		break;
	    }
	else {
	    System.err.println("Not recognized or supported "+si.exploration()+" search explorarion strategy ; compilation aborted");
	    System.exit(0);
	}

	if (Result) {
	    if (!optimization && options.getAll()) {
		// 		label.getSolutionListener().printAllSolutions();
		if (si.exploration().equals("complete")) 
		    if (! label.timeOutOccured) {
			if (options.getNumberSolutions() == -1 || options.getNumberSolutions() > label.getSolutionListener().solutionsNo())
			    System.out.println("==========");
		    }
		    else
			System.out.println("=====TIME-OUT=====");
		else
		    if (label.timeOutOccured) 
			System.out.println("=====TIME-OUT=====");
	    }
	    else if (optimization) {
		if (si.exploration().equals("complete"))
		    if (! label.timeOutOccured) {
			if (options.getNumberSolutions() == -1 || options.getNumberSolutions() > label.getSolutionListener().solutionsNo())
			    System.out.println("==========");
		    }    
		    else
			System.out.println("=====TIME-OUT=====");
		else
		    if (label.timeOutOccured) 
			System.out.println("=====TIME-OUT=====");
	    }
	}
	else
	    if (label.timeOutOccured) {
		System.out.println("=====UNKNOWN=====");
		System.out.println("=====TIME-OUT=====");
	    }
	    else
		if (si.exploration().equals("complete"))
		    System.out.println("=====UNSATISFIABLE=====");
		else
		    System.out.println("=====UNKNOWN=====");

	if (options.getStatistics()) {
	    int nodes = label.getNodes(), 
		decisions = label.getDecisions(), 
		wrong = label.getWrongDecisions(), 
		backtracks = label.getBacktracks(), 
		depth = label.getMaximumDepth(),
		solutions = label.getSolutionListener().solutionsNo();

	    for (int i=0; i < final_search.length; i++) {
		Search l = final_search[i];
		if ( l!= null) {
		    nodes += l.getNodes(); 
		    decisions += l.getDecisions();
		    wrong += l.getWrongDecisions();
		    backtracks += l.getBacktracks();
		    depth += l.getMaximumDepth();
		    solutions = l.getSolutionListener().solutionsNo();
		}
	    }
	    System.out.println("\nModel variables : "+(int)(store.size()+NumberBoolVariables)+
			       "\nModel constraints : "+initNumberConstraints+
			       "\n\nSearch CPU time : " + (b.getThreadCpuTime(tread.getId()) - startCPU)/(long)1e+6 + "ms"+
			       "\nSearch nodes : "+nodes+
			       "\nSearch decisions : "+decisions+
			       "\nWrong search decisions : "+wrong+
			       "\nSearch backtracks : "+backtracks+
			       "\nMax search depth : "+depth+
			       "\nNumber solutions : "+ solutions 
			       );
	}
    }

    DepthFirstSearch<Var>[] setSubSearchForAll(DepthFirstSearch<Var> label, boolean searchAll) {

	DepthFirstSearch[] intAndSetSearch = new DepthFirstSearch[3];

	Var[] int_search_variables = null,
	    set_search_variables = null,
	    bool_search_variables = null;

	// collect integer & bool variables for search
	int int_varSize = 0, bool_varSize=0;
	for (int i=0; i<dictionary.defaultSearchVariables.size(); i++)
	    if (dictionary.defaultSearchVariables.get(i) instanceof uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.BooleanVar)
		bool_varSize++;
	    else
		int_varSize++;
	for (int i=0; i<dictionary.defaultSearchArrays.size(); i++)
	    if (dictionary.defaultSearchArrays.get(i)[0]  instanceof uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.BooleanVar)
		bool_varSize += dictionary.defaultSearchArrays.get(i).length;
	    else
		int_varSize += dictionary.defaultSearchArrays.get(i).length;

	int_search_variables = new IntVar[int_varSize];
	bool_search_variables = new IntVar[bool_varSize];

	int bool_n=0, int_n=0;
	for (int i=0; i<dictionary.defaultSearchArrays.size(); i++)
	    for (int j=0; j<dictionary.defaultSearchArrays.get(i).length; j++) {
		Var v = dictionary.defaultSearchArrays.get(i)[j];
 		if (v  instanceof uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.BooleanVar) 
 		    bool_search_variables[bool_n++] = v;
		else
 		    int_search_variables[int_n++] = v;
	    }
	for (int i=0; i<dictionary.defaultSearchVariables.size(); i++) {
	    Var v = dictionary.defaultSearchVariables.get(i);
	    if (v  instanceof uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.BooleanVar) 
		bool_search_variables[bool_n++] = v;
	    else
		int_search_variables[int_n++] = v;
	}

	// collect set variables for search
	int n=0;
	int varSize = dictionary.defaultSearchSetVariables.size();
	for (int i=0; i<dictionary.defaultSearchSetArrays.size(); i++)
	    varSize += dictionary.defaultSearchSetArrays.get(i).length;

	set_search_variables = new SetVar[varSize];
	for (int i=0; i<dictionary.defaultSearchSetArrays.size(); i++)
	    for (int j=0; j<dictionary.defaultSearchSetArrays.get(i).length; j++)
		set_search_variables[n++] = dictionary.defaultSearchSetArrays.get(i)[j];
	for (int i=0; i<dictionary.defaultSearchSetVariables.size(); i++)
	    set_search_variables[n++] = dictionary.defaultSearchSetVariables.get(i);

//  	System.out.println ("int variables = " + java.util.Arrays.asList(int_search_variables));
//     	System.out.println ("boolean varibales = " + java.util.Arrays.asList(bool_search_variables));
//     	System.out.println ("set varibales = " + java.util.Arrays.asList(set_search_variables));
// 	System.out.println ("cost = " + costVariable);

	DepthFirstSearch<Var> lastSearch = label;
	DepthFirstSearch<Var> intSearch = new DepthFirstSearch<Var>();
 	if (int_search_variables.length != 0) {
	    // add search containing int variables to be sure that they get a value
 	    SelectChoicePoint<Var> intSelect = new SimpleSelect<Var>(int_search_variables, null, new IndomainMin());
	    if (variable_selection == null)
		variable_selection = intSelect;
	    intSearch.setSelectChoicePoint(intSelect);
	    intSearch.setPrintInfo(false);
	    if (lastSearch != null) 
		lastSearch.addChildSearch(intSearch);
	    lastSearch = intSearch;
	    if (bool_search_variables.length == 0 && set_search_variables.length == 0 ) {
		intSearch.setSolutionListener(new CostListener<Var>());

		if (costVariable != null) {
		    intSearch.setCostVar( (IntVar)costVariable);
		    intSearch.setOptimize( true);
		}
	    }
	    else
		intSearch.setSolutionListener(new EmptyListener<Var>());

	    if (searchAll) {
  		intSearch.getSolutionListener().searchAll(true); 
		intSearch.getSolutionListener().recordSolutions(false);
	    }	    
	    if (options.getNumberSolutions()>0)
		intSearch.getSolutionListener().setSolutionLimit(options.getNumberSolutions());

	    // time-out option
	    int to = options.getTimeOut();
	    if (to > 0) 
		intSearch.setTimeOut(to);

	    intAndSetSearch[0] = intSearch;
	}

	DepthFirstSearch<Var> boolSearch = new DepthFirstSearch<Var>();
 	if (bool_search_variables.length != 0) {
	    // add search containing boolean variables to be sure that they get a value
	    SelectChoicePoint<Var> boolSelect = new SimpleSelect<Var>(bool_search_variables, null, new IndomainMin());
	    if (variable_selection == null)
		variable_selection = boolSelect;
	    boolSearch.setSelectChoicePoint(boolSelect);
	    boolSearch.setPrintInfo(false);
	    if (lastSearch != null) 
		lastSearch.addChildSearch(boolSearch);
	    lastSearch = boolSearch;
	    if (set_search_variables.length == 0) {
		boolSearch.setSolutionListener(new CostListener<Var>()); 

		if (costVariable != null) {
		    intSearch.setCostVar( (IntVar)costVariable);
		    intSearch.setOptimize( true);
		}
	    }
	    else
		boolSearch.setSolutionListener(new EmptyListener<Var>());
	    if (searchAll) {
		boolSearch.getSolutionListener().searchAll(true); 
		boolSearch.getSolutionListener().recordSolutions(false);
	    }
	    if (options.getNumberSolutions()>0)
		boolSearch.getSolutionListener().setSolutionLimit(options.getNumberSolutions());

	    // time-out option
	    int to = options.getTimeOut();
	    if (to > 0) 
		boolSearch.setTimeOut(to);

	    intAndSetSearch[1] = boolSearch;
	}


	if (set_search_variables.length != 0) {
	    // add set search containing all variables to be sure that they get a value
	    DepthFirstSearch<Var> setSearch = new DepthFirstSearch<Var>();
	    SelectChoicePoint<Var> setSelect = new SimpleSelect<Var>(set_search_variables, null, new IndomainSetMin());
	    if (variable_selection == null)
		variable_selection = setSelect;
	    setSearch.setSelectChoicePoint(setSelect);
	    setSearch.setPrintInfo(false);
	    if (lastSearch != null) 
		lastSearch.addChildSearch(setSearch);
   	    setSearch.setSolutionListener(new CostListener<Var>());

	    if (costVariable != null) {
		intSearch.setCostVar( (IntVar)costVariable);
		intSearch.setOptimize( true);
	    }

	    if (searchAll) {
		setSearch.getSolutionListener().searchAll(true); 
		setSearch.getSolutionListener().recordSolutions(false);
	    }
	    if (options.getNumberSolutions()>0)
		setSearch.getSolutionListener().setSolutionLimit(options.getNumberSolutions());

	    // time-out option
	    int to = options.getTimeOut();
	    if (to > 0) 
		setSearch.setTimeOut(to);

	    intAndSetSearch[2] = setSearch;
	}

	if (int_search_variables.length == 0 && 
	    bool_search_variables.length == 0 && 
	    set_search_variables.length == 0) {

	    System.out.println("----------");
	    System.exit(0);
	}

	return intAndSetSearch;
    }


    void run_sequence_search(int solveKind, SimpleNode kind, SearchItem si) {

	if (options.getVerbose()) {
	    String solve="notKnown";
	    switch (solveKind) {
	    case 0: solve = "satisfy"; break; // satisfy
	    case 1: 
		solve = "minimize("+ getCost((ASTSolveExpr)kind.jjtGetChild(0))+") "; break; // minimize

	    case 2: 
		solve = "maximize("+ getCost((ASTSolveExpr)kind.jjtGetChild(0))+") "; break; // maximize
	    }
	    System.out.println(solve + " : seq_search([" + si + "])");
	}

	DepthFirstSearch<Var> masterLabel = null;
	DepthFirstSearch<Var> last_search = null;
	SelectChoicePoint<Var> masterSelect = null;
	list_seq_searches = new ArrayList<Search<Var>>();

	for (int i=0; i<si.getSearchItems().size(); i++) {
	    if (i == 0) { // master search
		masterLabel = sub_search(si.getSearchItems().get(i), masterLabel, true);
		last_search = masterLabel;
		masterSelect = variable_selection;
		if (!print_search_info) masterLabel.setPrintInfo(false);
 		masterLabel.setSolutionListener(new EmptyListener<Var>());
	    }
	    else {
		DepthFirstSearch<Var> label = sub_search(si.getSearchItems().get(i), last_search, false);
		last_search.addChildSearch(label);
		last_search = label;
		if (!print_search_info) last_search.setPrintInfo(false);
 		label.setSolutionListener(new EmptyListener<Var>());
	    }
	}

 	DepthFirstSearch<Var>[] complementary_search = setSubSearchForAll(last_search, options.getAll());
	boolean connectedToLast=false;
	for (int i = 0; i < complementary_search.length; i++) {
	    if (complementary_search[i]  != null) {
		list_seq_searches.add(complementary_search[i]);
		if (! connectedToLast) {
		    last_search.addChildSearch(complementary_search[i]);
		    connectedToLast = true;
		}
		if (!print_search_info) complementary_search[i].setPrintInfo(false);
	    }
	}

//       	System.out.println(list_seq_searches);

	boolean Result = false;	
	IntVar cost=null;
	boolean optimization = false;

	Search<Var> final_search = list_seq_searches.get(list_seq_searches.size()-1);

	Thread tread = java.lang.Thread.currentThread();
	java.lang.management.ThreadMXBean b = java.lang.management.ManagementFactory.getThreadMXBean();

	long startCPU = b.getThreadCpuTime(tread.getId());
	// 	long startUser = b.getThreadUserTime(tread.getId());

	int to = options.getTimeOut();
	if (to > 0)
	    for (Search s : list_seq_searches)
		s.setTimeOut(to);

	if (si.exploration() == null || si.exploration().equals("complete"))
	    switch (solveKind) {
	    case 0: // satisfy
 		if (options.getAll() ) { // all solutions
 		    int ns = options.getNumberSolutions();
  		    for (int i=0; i<si.getSearchItems().size(); i++) {  //list_seq_searches.size(); i++) {
  			list_seq_searches.get(i).getSolutionListener().searchAll(true);
  			list_seq_searches.get(i).getSolutionListener().recordSolutions(false);
       			if ( ns>0 )
     			    list_seq_searches.get(i).getSolutionListener().setSolutionLimit(ns);
 		    }
 		}

		Result = masterLabel.labeling(store, masterSelect);

		break;

	    case 1: // minimize
		optimization = true;
		cost = getCost((ASTSolveExpr)kind.jjtGetChild(0));

 		Result = restart_search(masterLabel, masterSelect, cost, true);

//     		Result = masterLabel.labeling(store, masterSelect, cost);
		break;
	    case 2: //maximize
		optimization = true;
		cost = getCost((ASTSolveExpr)kind.jjtGetChild(0));

		Result = restart_search(masterLabel, masterSelect, cost, false);

// 		cost = getCost((ASTSolveExpr)kind.jjtGetChild(0));
// 		Variable max_cost = new Variable(store, "-"+cost.id(), IntDomain.MinInt, 
// 		 						 IntDomain.MaxInt);
// 		pose(new XplusYeqC(max_cost, cost, 0));
// 		Cost = max_cost;
// 		final_search.setSolutionListener(new CostListener());

// 		Result = masterLabel.labeling(store, masterSelect, max_cost);
		break;
	    }
	else {
	    System.err.println("Not recognized or supported "+si.exploration()+
			       " search explorarion strategy ; compilation aborted");
	    System.exit(0);
	}

	if (Result) {
	    if (!optimization && options.getAll()) {
		if (!heuristicSeqSearch)
		    if (! anyTimeOutOccured(list_seq_searches)) {
			if (options.getNumberSolutions() == -1 || options.getNumberSolutions() > final_search.getSolutionListener().solutionsNo())
			    System.out.println("==========");
		    }
		    else
			System.out.println("=====TIME-OUT=====");
		else
		    if (anyTimeOutOccured(list_seq_searches)) 
			System.out.println("=====TIME-OUT=====");
	    }
	    else if (optimization) {
		if (!heuristicSeqSearch)
		    if (! anyTimeOutOccured(list_seq_searches)) {
			if (options.getNumberSolutions() == -1 || options.getNumberSolutions() > final_search.getSolutionListener().solutionsNo())
			    System.out.println("==========");
		    }
		    else
			System.out.println("=====TIME-OUT=====");
		else
		    if (anyTimeOutOccured(list_seq_searches)) 
			System.out.println("=====TIME-OUT=====");
	    }
	}
	else
	    if (anyTimeOutOccured(list_seq_searches)) {
		System.out.println("=====UNKNOWN=====");
		System.out.println("=====TIME-OUT=====");
	    }
	    else 
		System.out.println("=====UNSATISFIABLE=====");

	if (options.getStatistics()) {
	    int nodes=0, decisions=0, wrong=0, backtracks=0, depth=0,solutions=0;
	    for (int i=0; i<list_seq_searches.size(); i++) {
		Search label = list_seq_searches.get(i);
		nodes += label.getNodes(); 
		decisions += label.getDecisions();
		wrong += label.getWrongDecisions();
		backtracks += label.getBacktracks();
		depth += label.getMaximumDepth();
		solutions = label.getSolutionListener().solutionsNo();
	    }
	    System.out.println("\nModel variables : "+(int)(store.size()+NumberBoolVariables)+
			       "\nModel constraints : "+initNumberConstraints+
			       "\n\nSearch CPU time : " + (b.getThreadCpuTime(tread.getId()) - startCPU)/(long)1e+6 + "ms"+
			       "\nSearch nodes : "+nodes+
			       "\nSearch decisions : "+decisions+
			       "\nWrong search decisions : "+wrong+
			       "\nSearch backtracks : "+backtracks+
			       "\nMax search depth : "+depth+
			       "\nNumber solutions : "+ solutions 
			       );
	}
    }

    boolean anyTimeOutOccured(ArrayList<Search<Var>> list_seq_searches) {

	for (int i=0; i<list_seq_searches.size(); i++)
	    if ( ((DepthFirstSearch)list_seq_searches.get(i)).timeOutOccured)
		return true;
	return false;
    }


    DepthFirstSearch<Var> sub_search(SearchItem si, DepthFirstSearch<Var> l, boolean master) {
	DepthFirstSearch<Var> last_search = l;
	DepthFirstSearch<Var> label = null;

	if (si.type().equals("int_search")) {
	    label = int_search(si);
	    if (!master) label.setSelectChoicePoint(variable_selection);

	    // LDS heuristic search
	    if (si.exploration().equals("lds")) {
		lds_search(label, si.ldsValue);
		heuristicSeqSearch = true;
	    }
	    // Credit heuristic search
	    if (si.exploration().equals("credit")) {
		credit_search(label, si.creditValue, si.bbsValue);
		heuristicSeqSearch = true;
	    }
	    list_seq_searches.add(label);
	}
	else if (si.type().equals("bool_search")) {
	    label = int_search(si);
	    if (!master) label.setSelectChoicePoint(variable_selection);

	    // LDS heuristic search
	    if (si.exploration().equals("lds")) {
		lds_search(label, si.ldsValue);
		heuristicSeqSearch = true;
	    }
	    // Credit heuristic search
	    if (si.exploration().equals("credit")) {
		credit_search(label, si.creditValue, si.bbsValue);
		heuristicSeqSearch = true;
	    }

	    list_seq_searches.add(label);
	}
	else if (si.type().equals("set_search")) {
	    setSearch=true;
	    label = set_search(si);
	    if (!master) 
		label.setSelectChoicePoint(variable_selection);

	    // LDS heuristic search
	    if (si.exploration().equals("lds")) {
		lds_search(label, si.ldsValue);
		heuristicSeqSearch = true;
	    }
	    // Credit heuristic search
	    if (si.exploration().equals("credit")) {
		credit_search(label, si.creditValue, si.bbsValue);
		heuristicSeqSearch = true;
	    }

	    list_seq_searches.add(label);
	}
	else if (si.type().equals("seq_search")) {
	    for (int i=0; i<si.getSearchItems().size(); i++)
		if (i == 0) { // master search
		    DepthFirstSearch<Var> label_seq = sub_search(si.getSearchItems().get(i), last_search, false);
		    last_search = label_seq;
		    label = label_seq;
		}
		else {
		    DepthFirstSearch<Var> label_seq = sub_search(si.getSearchItems().get(i), last_search, false);
		    last_search.addChildSearch(label_seq);
		    last_search = label_seq;
		}
	}
	else {
	    System.err.println("Not recognized or supported search type \""+si.type()+"\"; compilation aborted");
	    System.exit(0);
	}

	return label;
    }

    DepthFirstSearch<Var> int_search(SearchItem si) {

	variable_selection = si.getIntSelect();
	DepthFirstSearch<Var> label = new DepthFirstSearch<Var>();
	label.setSolutionListener(new PrintOutListener<Var>());
	return label;
    }

    DepthFirstSearch<Var> set_search(SearchItem si) {

	variable_selection = si.getSetSelect();
	DepthFirstSearch<Var> label = new DepthFirstSearch<Var>();
	label.setSolutionListener(new SimpleSolutionListener<Var>());
	return label;
    }

    void printSolution() {

	// System.out.println("*** Solution :");
	if (dictionary.outputVariables.size() > 0)
	    for (int i=0; i<dictionary.outputVariables.size(); i++) {
		Var v = dictionary.outputVariables.get(i);

 		if (v instanceof BooleanVar) {
		    String boolVar = v.id()+" = ";
		    if (v.singleton())
			switch ( ((BooleanVar)v).value()) {
			case 0: boolVar += "false";
			    break;
			case 1: boolVar += "true";
			    break;
			default: boolVar += v.dom();
			}
		    System.out.println(boolVar+";");
		}
		else
		    System.out.println(v+";");
	    }

	for (int i=0; i<dictionary.outputArray.size(); i++) {
	    OutputArrayAnnotation a = dictionary.outputArray.get(i);
	    System.out.println(a);
	}
    }

    int getKind(String k) {
	if (k.equals("satisfy")) // 0 = satisfy
	    return 0;
	else if (k.equals("minimize")) // 1 = minimize
	    return 1;
	else if (k.equals("maximize")) // 2 = maximize
	    return 2;
	else {
	    System.err.println("Not supported search kind; compilation aborted");
	    System.exit(0);
	    return -1;
	}
    }

    IntVar getCost(ASTSolveExpr node) {
	if (node.getType() == 0) // ident
	    return dictionary.getVariable(node.getIdent());
	else if (node.getType() == 1) // array access
	    return dictionary.getVariableArray(node.getIdent())[node.getIndex()];
	else {
	    System.err.println("Wrong cost function specification " + node);
	    System.exit(0);
	    return new IntVar(store);
	}
    }

    Var getVariable(ASTScalarFlatExpr node) {
	if (node.getType() == 0) //int
	    return new IntVar(store, node.getInt(), node.getInt());
	else if (node.getType() == 2) // ident
	    return dictionary.getVariable(node.getIdent());
	else if (node.getType() == 3) // array access
	    return dictionary.getVariableArray(node.getIdent())[node.getInt()];
	else {
	    System.err.println("Wrong parameter " + node);
	    System.exit(0);
	    return new IntVar(store);
	}
    }

    Var[] getVarArray(SimpleNode node) {
	if (node.getId() == JJTARRAYLITERAL) {
	    int count = node.jjtGetNumChildren();
	    Var[] aa = new Var[count];
	    for (int i=0;i<count;i++) {
		ASTScalarFlatExpr child = (ASTScalarFlatExpr)node.jjtGetChild(i);
		Var el = getVariable(child);
		aa[i] = el;
	    }
	    return aa;
	}
	else if (node.getId() == JJTSCALARFLATEXPR) {
	    if (((ASTScalarFlatExpr)node).getType() == 2) // ident
		return dictionary.getVariableArray(((ASTScalarFlatExpr)node).getIdent());
	    else {
		System.err.println("Wrong type of Variable array; compilation aborted."); 
		System.exit(0);
		return new Var[] {};
	    }
	}
	else {
	    System.err.println("Wrong type of Variable array; compilation aborted."); 
	    System.exit(0);
	    return new Var[] {};
	}
    }

    Var[] getSetVarArray(SimpleNode node) {
	if (node.getId() == JJTARRAYLITERAL) {
	    int count = node.jjtGetNumChildren();
	    Var[] aa = new Var[count];
	    for (int i=0;i<count;i++) {
		ASTScalarFlatExpr child = (ASTScalarFlatExpr)node.jjtGetChild(i);
		Var el = getSetVariable(child);
		aa[i] = el;
	    }
	    return aa;
	}
	else if (node.getId() == JJTSCALARFLATEXPR) {
	    if (((ASTScalarFlatExpr)node).getType() == 2) // ident
		return dictionary.getSetVariableArray(((ASTScalarFlatExpr)node).getIdent());
	    else {
		System.err.println("Wrong type of Variable array; compilation aborted."); 
		System.exit(0);
		return new Var[] {};
	    }
	}
	else {
	    System.err.println("Wrong type of Variable array; compilation aborted."); 
	    System.exit(0);
	    return new Var[] {};
	}
    }

    Var getSetVariable(ASTScalarFlatExpr node) {
	if (node.getType() == 2) // ident
	    return dictionary.getSetVariable(node.getIdent());
	else if (node.getType() == 3) // array access
	    return dictionary.getSetVariableArray(node.getIdent())[node.getInt()];
	else {
	    System.err.println("Wrong parameter on list of search set varibales" + node);
	    System.exit(0);
	    return new IntVar(store);
	}
    }

    void pose(Constraint c) {
	store.impose(c);
	if (debug)
	    System.out.println(c);
    }

    boolean restart_search(Search<Var> masterLabel, SelectChoicePoint<Var> masterSelect, 
			   IntVar cost, boolean minimize) {
	costVariable = cost;
	Search<Var> final_search = list_seq_searches.get(list_seq_searches.size()-1);

 	for (Search s : list_seq_searches) 
 	    s.setAssignSolution(false);
	store.setLevel(store.level+1);
	boolean Result = true, optimalResult = false; 
	while (Result) {
	    Result = masterLabel.labeling(store, masterSelect);
	    if (minimize) //minimize
		pose(new XltC(cost, costValue));
	    else // maximize
		pose(new XgtC(cost, costValue));

	    optimalResult = optimalResult || Result;

	    if (options.getNumberSolutions() == final_search.getSolutionListener().solutionsNo())
		break;
	}
	store.removeLevel(store.level);
	store.setLevel(store.level-1);

	Result = optimalResult;
//  	if (Result)
//  	    for (Search s : list_seq_searches) 
//  		s.assignSolution();

	return Result;
    }

    void lds_search(DepthFirstSearch<Var> label, int lds_value) {
	//  	System.out.println("LDS("+lds_value+")");

	LDS<Var> lds = new LDS<Var>(lds_value); 
	if (label.getExitChildListener() == null)
	    label.setExitChildListener(lds);
	else
	    label.getExitChildListener().setChildrenListeners(lds);
    }


    void credit_search(DepthFirstSearch<Var> label, int creditValue, int bbsValue) {
	//  	System.out.println("Credit("+creditValue+", "+bbsValue+")");

	int maxDepth = 1000; //IntDomain.MaxInt;
	CreditCalculator<Var> credit = new CreditCalculator<Var>(creditValue, bbsValue, maxDepth);

	if (label.getConsistencyListener() == null)
	    label.setConsistencyListener(credit);
	else
	    label.getConsistencyListener().setChildrenListeners(credit);

	label.setExitChildListener(credit);
	label.setTimeOutListener(credit);
    }

    String getArrayName(Var v) {
	String s = v.id();
	char c = '['; 
	int ci = (int)c;
	int end = s.indexOf(ci ); 
	return s.substring(0, end);
    }

    void setNumberBoolVariables(int n) {
	NumberBoolVariables = n;
    }

    /**
     * 
     * 
     * @author Krzysztof Kuchcinski
     *
     */
    public class EmptyListener<T extends Var> extends SimpleSolutionListener<T> {

	public boolean executeAfterSolution(Search<T> search, SelectChoicePoint<T> select) {

	    return solutionLimit <= FinalNumberSolutions;
	}
    }

    int FinalNumberSolutions = 0;

    /**
     * 
     * 
     * @author Krzysztof Kuchcinski
     *
     */
    public class CostListener<T extends Var> extends SimpleSolutionListener<T> {

	public boolean executeAfterSolution(Search<T> search, SelectChoicePoint<T> select) {

	    boolean returnCode = super.executeAfterSolution(search, select);

	    if (costVariable != null)
		costValue = ((IntVar)costVariable).value();

	    FinalNumberSolutions++;

	    printSolution();
	    System.out.println("----------");

	    return returnCode;
	}
    }

}
