/**
 *  Tree.java 
 *  This file is part of JaCoP.
 *
 *  JaCoP is a Java Constraint Programming solver. 
 *	
 *	Copyright (C) 2000-2008 Radoslaw Szymanek and Wadeck Follonier
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

package uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.knapsack;

import java.util.HashMap;
import java.util.List;

import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntVar;


/**
 * This tree is a main data structure used and required by Knapsack constraint. 
 * 
 * @author Radoslaw Szymanek and Wadeck Follonier
 */

public class Tree {
	
	/**
	 * It specifies the root of the tree.
	 */
	final public TreeNode root;
	
	/**
	 * It specifies the leaf containing the critical item. The critical item 
	 * is the one used partially by Linear Programming approach.
	 */
	public TreeLeaf criticalLeaf;

	/**
	 * It specifies the leaf containing the last right item which is being
	 * used during computeMandatory(). if max() of the right item after
	 * criticalRight is changed then it can be safely ignored.
	 */
	public int criticalRightLeaf;

	/**
	 * It specifies the leaf containing the left most item which is being
	 * used during computeForbidden(). if min() of the left item before
	 * criticalLeft is changed then it can be safely ignored.
	 */
	public int criticalLeftLeaf;
	
	/**
	 * It specifies the first (counting from left to right), the most 
	 * efficient item in the tree. 
	 */
	public TreeLeaf first;
	
	/**
	 * It specifies the last (counting from left to right), the least 
	 * efficient item in the tree.
	 */
	public TreeLeaf last;
	
	/**
	 * It specifies the optimalProfit of possibly non-integral solution
	 * generated by LP relaxation.
	 */
	public double optimalProfit = 0;
	
	/**
	 * It specifies the fraction of the critical item which has been 
	 * not included in the optimal non-integral solution.
	 */
	public int availableWeightOfCriticalItem;

	/**
	 * It specifies how much weight is used by an optimal 
	 * non-fractional solution.
	 */
	public int takenWeightOfCriticalItem;
	
	/**
	 * It specifies the already obtained profit due to the items which 
	 * are already included in the solution.
	 */
	public int alreadyObtainedProfit;
	
	/**
	 * It specifies the already used capacity due to the items which 
	 * are already included in the solution. 
	 */
	public int alreadyUsedCapacity;
	

	/**
	 * Create a single node tree. 
	 * @param node a root of this one-node tree.
	 */
	public Tree(TreeNode node) {
		this.root = node;
	}

	/**
	 * It creates a tree by making a shallow copy.
	 * @param tree
	 */
	public Tree(Tree tree) {
		this.root = tree.root;
		this.first = tree.first;
		this.last = tree.last;
	}

	/**
	 * A merge method for trees, it added a new root from the ancients
	 * @param that A tree that is being merged with this tree.
	 * 
	 * @return The tree resulting of the merge of <i>this</i> and <i>that</i>
	 */
	public Tree merge(Tree that) {
		TreeNode resultRoot;
		resultRoot = new TreeNode(this.root, that.root);
		Tree result = new Tree(resultRoot);
		result.first = this.first;
		result.last = that.last;
		return result;
	}

	
	/**
	 * It constructs a tree out of the list of items and creates
	 * proper supporting structures. It assumes the list of items
	 * is greater than 1.
	 * 
	 * @param items knapsack items used to create the tree.
	 * @param varPositionMaping mapping of variables into positions within the tree.
	 * @param leaves array of leaves of the created tree.
	 * @param zero it specifies a variable equal to value 0.
	 */
	public Tree(KnapsackItem[] items,
					   HashMap<IntVar, TreeLeaf> varPositionMaping,
					   TreeLeaf[] leaves, 
					   IntVar zero) {
		
		assert (items.length > 1) : "Number of items must be greater than 1";
		
		TreeLeaf nullLeaf = new TreeLeaf(zero, 1, 0, items.length);
			
		int totalLength = items.length;
		
		if (totalLength % 2 == 1)
			totalLength++;
		
		TreeNode[] currentLevel = new TreeNode[totalLength];

		alreadyObtainedProfit = 0;
		
		alreadyUsedCapacity = 0;

		// Create leaves.
		for (int i = 0; i < items.length; i++) {

			KnapsackItem item = items[i];

			IntVar v = item.getVariable();
			int v_weight = item.getWeight();
			int v_profit = item.getProfit();

			TreeLeaf leaf = new TreeLeaf(v, v_weight, v_profit, i);
			leaves[i] = leaf;
			
			currentLevel[i] = leaf;
			varPositionMaping.put(v, leaf);
			
		}
		
		if (items.length < totalLength)
			currentLevel[items.length] = nullLeaf;
				
		first = leaves[0];
		last = leaves[items.length - 1];
		
		// Create internal nodes.
		while (currentLevel.length != 1) {

			
			for (int i = 0; i < currentLevel.length - 1; i++)
				currentLevel[i].setRightNeighbor(currentLevel[i+1]);
			
			for (int i = 1; i < currentLevel.length; i++)
				currentLevel[i].setLeftNeighbor(currentLevel[i-1]);

			int k = 0;
			int next = 0;
			
			int length = currentLevel.length / 2;
			
			if (length % 2 == 1 && length != 1)
				length++;
			
			TreeNode[] nextLevel = new TreeNode[length];
		
			while (k < currentLevel.length &&
					currentLevel[k] != null) {
			
				TreeNode left = currentLevel[k];
				TreeNode right = null;
				if (k + 1 < currentLevel.length)
					right = currentLevel[k+1];
			
				if (right == null) {
					// one item only.
					nextLevel[next] = right;
				}
				else {
				
					TreeNode root = new TreeNode(left, right);
					nextLevel[next] = root;
				}
			
				next++;
				k += 2;
			}
			
			if (nextLevel[length-1] == null)
				nextLevel[length-1] = new TreeNode(nullLeaf, nullLeaf);
			
			currentLevel = nextLevel;
			
		}
		
		root = currentLevel[0];

		root.recomputeDown(this);
	}
	
	/**
	 * It updates information about the critical item, as well as 
	 * information about fraction of critical item which is not 
	 * taken.  
	 * 
	 * @param capacity available capacity to be used by knapsack.
	 */
	public void updateCritical(int capacity) {

		if (capacity == 0 || root.getPSum() == 0) {
			criticalLeaf = first;
			optimalProfit = 0;
			criticalLeftLeaf = 0;
			criticalRightLeaf = last.positionInTheTree;
			takenWeightOfCriticalItem = 0;
			return;
		}
		
		TreeNode current = root;		
		int usedCapacity = 0;
		double obtainedProfit = 0;
		
		while (!current.isLeaf()) {
			
				if (current.left.getWSum() == 0) {
					current = current.right;
				} else if (current.right.getWSum() == 0) {
					current = current.left;
				} else
				/* sum(0->s-1) of w <= C and sum(0->s) of w > C */
				if (usedCapacity + current.left.getWSum() <= capacity) {
					usedCapacity += current.left.getWSum();

					/* we add the profit of all the left part */
					obtainedProfit += current.left.getPSum();

					current = current.right;
				} else {
					current = current.left;
				}
		}
	
		criticalLeaf = (TreeLeaf) current;
		
		/* the last part of the capacity used within critical */
		// min function required for situation when capacity allows to take all the items.
		takenWeightOfCriticalItem = Math.min(capacity - usedCapacity,
				criticalLeaf.getWSum());
	
		availableWeightOfCriticalItem = criticalLeaf.getWSum() - takenWeightOfCriticalItem;
		
		obtainedProfit += ((criticalLeaf.getPSum() * takenWeightOfCriticalItem) / (double)  criticalLeaf.getWSum());
	
		optimalProfit = obtainedProfit;
	
		assert (optimalProfit >= 0) : "The optimal profit is negative. ";
		
		criticalLeftLeaf = getCriticalPosition(capacity - root.getWMax());
		criticalRightLeaf = getCriticalPosition(capacity + root.getWMax());
	}

	
	/**
	 * It finds a leaf which reaches the limit of the given capacity.
	 * Items weight is added from the most efficient to the least efficient.
	 * 
	 * @param capacity available capacity to be used by knapsack.
	 * @return the position of the item.
	 */
	public int getCriticalPosition(int capacity) {

		if (capacity < 0)
			return 0;
		
		if (capacity > root.getWSum())
			return last.positionInTheTree;
		
		TreeNode current = root;		
		int usedCapacity = 0;
		
		while (!current.isLeaf()) {
			
				if (current.left.getWSum() == 0) {
					current = current.right;
				} else if (current.right.getWSum() == 0) {
					current = current.left;
				} else
				/* sum(0->s-1) of w <= C and sum(0->s) of w > C */
				if (usedCapacity + current.left.getWSum() <= capacity) {
					usedCapacity += current.left.getWSum();
					current = current.right;
				} else {
					current = current.left;
				}
		}
	
		return ((TreeLeaf)current).positionInTheTree;
		
	}

	
	
	/**
	 * Used to search for mandatory
	 * 
	 * @return The first item
	 */
	public TreeLeaf getFirst() {
		return first;
	}

	/**
	 * It returns the last (the least efficient) item in the tree. It is 
	 * a starting leaf for computeForbidden() part. 
	 * 
	 * @return The last item
	 */
	public TreeLeaf getLast() {
		return last;
	}

	@Override
	public String toString() {
		return "<{ " + root.toString() + " }>";
	}

	/**
	 * Used for updating the tree using a list of nodes that have changed.
	 * 
	 * @param list list of leaves that needs to be updated.
	 * @param startingPosition it specifies the first leaf in the array which has not been updated before.
	 */
	public void updateFromList(List<TreeLeaf> list, int startingPosition) {
		
		if (list == null || list.isEmpty()) {
			return;
		}
		
		TreeLeaf leaf;
		
		for (int i = list.size() - 1; i >= startingPosition; i--) {

			// It may be the case that two leaves with one parent have
			// changed, then two passes through the same path will happen. 
			// However, it is rather likely not worth protecting against
			// this potential inefficiency.

			leaf = list.get(i);
			leaf.updateInternalValues(this);
			leaf.parent.recomputeUp(null);
		
		}				
		
	}

	/**
	 * It recomputes all the attributes of the internal nodes of the knapsack tree.
	 */
	public void recompute() {

		root.recomputeDown(this);
		
	}
	
	
	/**
	 * It specifies the current right item of the tree which have been yet included 
	 * in computation of replaceable weight. 
	 */
	TreeNode currentNode;
	
	/**
	 * It specifies the currentWeight from which searching for next mandatory item starts from. 
	 * Only items with weight greater or equal to currentWeight can be (partially) mandatory.
	 */
	int currentWeight;
	
	/**
	 * It specifies the current profit obtained by all already traversed right items.
	 */
	int currentProfit;

	/**
	 * It specifies the profit obtained from the remaining part of the critical item.
	 */
	double profitFromCriticalLeft;

	/**
	 * It specifies the profit obtained from the remaining part of the critical item.
	 */
	double profitFromCriticalTaken;
	
	
	/**
	 * It initializes the private variables required by computation of how much weight 
	 * we can replace for any Left item.
	 */
	public void initializeComputeMandatory() {
		
		currentWeight = 0;
		currentProfit = 0;
		profitFromCriticalLeft = ( criticalLeaf.profitOfOne 
				* availableWeightOfCriticalItem ) / (double)  criticalLeaf.weightOfOne ;
		currentNode = criticalLeaf;
		exhaustedRightItems = false;
	}

	
	/**
	 * It specifies if the mandatory check has run out of right items
	 * to complement mandatory items.
	 */
	public boolean exhaustedRightItems = false;
	

	/**
	 * It returns the amount of weight of a given item being checked which can be replaced by Right items given
	 * the amount of profitSlack. 
	 * 
	 * @param weightOfItemChecked the weight of item being checked. 
	 * @param maxNoOfItems the maximum number which can be taken of checked items.
	 * @param profitOfItemChecked the profit of the item being checked.
	 * @param efficiencyOfItemChecked the efficiency of the item being checked.
	 * @param profitSlack the amount of reserve profit which can be sacrificed before violating the constraint.
	 * 
	 * @return the amount of weight of a given item that can be replaced by Right items without violating the constraint. 
	 */
	public int computeReplacableWeight(int weightOfItemChecked, 
									   int maxNoOfItems,
									   int profitOfItemChecked,
									   double efficiencyOfItemChecked,
									   double profitSlack) {
				
		// If the function can not go beyond critical leaf 
		if ( currentNode == criticalLeaf &&
			 efficiencyOfItemChecked * availableWeightOfCriticalItem - profitFromCriticalLeft > profitSlack ) {
			
			// Playing safe, we return higher value (ceil) to avoid making items mandatory due to rounding errors). 
			// return (int) Math.ceil( profitSlack * criticalLeaf.weightOfOne / (double)  criticalLeaf.profitOfOne );
			
			return (int) Math.ceil( profitSlack * criticalLeaf.weightOfOne * weightOfItemChecked / 
					(double) ( criticalLeaf.weightOfOne * profitOfItemChecked - weightOfItemChecked * criticalLeaf.profitOfOne ) );

		}
		else if (currentWeight < availableWeightOfCriticalItem)
			currentWeight = availableWeightOfCriticalItem;
		
		int weightNoPruning = weightOfItemChecked * maxNoOfItems;

		while(!exhaustedRightItems) {

			if (currentNode.parent == null) {
				exhaustedRightItems = true;
				break;
			}

			boolean rightChild = currentNode.parent.right == currentNode;

			if (!rightChild) {

				// move right does not exceed slack.
				double profitEaten = efficiencyOfItemChecked * ( currentWeight + currentNode.rightNeighbor.getWSum() ) 
				 - ( currentProfit + currentNode.rightNeighbor.getPSum() ) - profitFromCriticalLeft;
				 
				// @TODO < changed to <=, double check that it causes no problems...
				if ( profitEaten <= profitSlack) {

					currentNode = currentNode.parent;
					currentWeight += currentNode.right.getWSum();
					currentProfit += currentNode.right.getPSum();

					continue;

				}
				else
					break;
				
			}
			
			// currentNode is the right child.
			if (rightChild) {

				if (currentNode.rightNeighbor == null) {
					exhaustedRightItems = true;
					break;
				}

				// move right does not exceed slack.
				// @TODO < changed to <=, double check that it causes no problems...
				if ( efficiencyOfItemChecked * ( currentWeight + currentNode.rightNeighbor.getWSum() ) 
					 - ( currentProfit + currentNode.rightNeighbor.getPSum() ) - profitFromCriticalLeft <= profitSlack) {

					currentNode = currentNode.rightNeighbor;
					currentWeight += currentNode.getWSum();
					currentProfit += currentNode.getPSum();

					continue;
					
				}
				else
					break;
				
			}
			

		}
	
		
		// Slack has been almost exhausted or left items has been exhausted.

		if (!exhaustedRightItems)
			while ( true ) {

				// move right does not exceed slack.
				if ( efficiencyOfItemChecked * ( currentWeight + currentNode.rightNeighbor.getWSum() ) 
					 - ( currentProfit + currentNode.rightNeighbor.getPSum() ) - profitFromCriticalLeft < profitSlack) {

						currentNode = currentNode.rightNeighbor;
						currentWeight += currentNode.getWSum();
						currentProfit += currentNode.getPSum();

				}
				else {
						// Going to right neighbor does exhaust slack.
											
						if (!currentNode.isLeaf())
							currentNode = currentNode.right;
						else
							break;
				}
				
				if (currentNode.isLeaf())
					break;
			}
		
		if (!exhaustedRightItems && currentNode.rightNeighbor.getPSum() == 0)
			currentNode = currentNode.rightNeighbor;
		
		// Found the last leaf before exceeding slack. 
		
		if (currentWeight > weightNoPruning ) {
			// Checked item does not have anything mandatory.
			// Next item must have weightNoPruning larger than currentWeight.
			return currentWeight;
		}
		
		// Not the whole slackProfit used and not the whole of item being checked is covered yet. Using item on the right (if exists) to use remaining 
		// profit.

		double lastWeight = ( profitSlack + currentProfit + profitFromCriticalLeft ) - currentWeight * profitOfItemChecked / (double) weightOfItemChecked;
		
		if (!exhaustedRightItems) {
			
			if (currentNode.rightNeighbor == null)
				System.out.println("Problem " + toString() );
			
			double efficiencyLoss = profitOfItemChecked / (double) weightOfItemChecked - currentNode.rightNeighbor.getPSum() / (double) currentNode.rightNeighbor.getWSum();
			lastWeight /= efficiencyLoss;

			// Playing safe, we can replace more, so we do not make an item mandatory when we should not.
			int result = currentWeight + (int) Math.ceil( lastWeight );
			// Playing safe, we decrease replacable weight to floor for the next iteration so we do not miss mandatory item.
			// @TODO commented out below
			// currentWeight += (int) Math.floor( lastWeight );
			return result;
			
		}
		else {

			double efficiencyLoss = profitOfItemChecked / (double)  weightOfItemChecked;
			lastWeight /= efficiencyLoss;

			// Playing safe, we can replace more, so we do not make an item mandatory when we should not.
			int result = currentWeight + (int) Math.ceil( lastWeight );
			// Playing safe, we decrease replacable weight to floor for the next iteration so we do not miss mandatory item.
			// @TODO commented out below
			// currentWeight += (int) Math.floor( lastWeight );
			return result;
			
		}
					
	}

	
	
	/**
	 * 
	 * It finds next leaf of a maximum weight of at least weight, so 
	 * it can have some parts of it mandatory. 
	 * 
	 * @param leaf starting leaf, the result must be to the right of this leaf.
	 * @param weight weight condition which must be satisfied by the found leaf.
	 * @return tree leaf on the right to the supplied leaf with at least specified weight.
	 */
	public TreeLeaf findNextLeafAtLeastOfWeight(TreeLeaf leaf, int weight) {

		if (leaf.rightNeighbor == null)
			return null;
		
		if (leaf.rightNeighbor.getWMax() > weight)
			return (TreeLeaf) leaf.rightNeighbor;
		
		TreeNode currentNode = leaf.parent;
		
		while (currentNode != null) {
			if (currentNode.rightNeighbor == null)
				return null;
			if (currentNode.rightNeighbor.getWMax() <= weight)
				currentNode = currentNode.parent;
			else {
				currentNode = currentNode.rightNeighbor;
				break;
			}
		}
		
		if (currentNode == null)
			return null;
		
		while (!currentNode.isLeaf()) {
			// Preference to left node if both nodes are above weight.
			if (currentNode.left.getWMax() > weight)
				currentNode = currentNode.left;
			else
				currentNode = currentNode.right;
		}
		
		return (TreeLeaf) currentNode;
	
	}
	
	

	/**
	 * It specifies that computeForbidden part of the consistency function 
	 * has run out of left mandatory items.
	 */
	
	public boolean exhaustedLeftItems = false;
	

	
	/**
	 * It initializes the private variables required by computation of how much weight 
	 * we can replace for any Left item.
	 */
	public void initializeComputeForbidden() {
		
		currentWeight = 0;
		currentProfit = 0;
		profitFromCriticalTaken = ( criticalLeaf.getWSum() - availableWeightOfCriticalItem )
								  * criticalLeaf.profitOfOne / (double)  criticalLeaf.weightOfOne; 
		currentNode = criticalLeaf;
		exhaustedLeftItems = false;
	}
	
	/**
	 * It returns the amount of weight of a given item being checked which can be replaced by Right items given
	 * the amount of profitSlack. 
	 * 
	 * @param weightOfItemChecked the weight of item being checked. 
	 * @param maxNoOfItems the maximum number which can be taken of checked items.
	 * @param profitOfItemChecked the profit of the item being checked.
	 * @param efficiencyOfItemChecked the efficiency of the item being checked.
	 * @param profitSlack the amount of reserve profit which can be sacrificed before violating the constraint.
	 * 
	 * @return the amount of weight of a given item that can be replaced by Right items without violating the constraint. 
	 */
	public int computeIntrusionWeight(int weightOfItemChecked, 
									  int maxNoOfItems,
									  int profitOfItemChecked,
									  double efficiencyOfItemChecked,
									  double profitSlack) {
		
		// If the function can not go beyond critical leaf 
		if ( currentNode == criticalLeaf &&
				profitFromCriticalTaken - efficiencyOfItemChecked * takenWeightOfCriticalItem > profitSlack ) {
			
			// Playing safe, we return higher value (ceil) to avoid making items forbidden due to rounding errors). 
			return (int) Math.ceil( profitSlack * criticalLeaf.weightOfOne * weightOfItemChecked / 
					(double) ( criticalLeaf.profitOfOne * weightOfItemChecked - profitOfItemChecked * criticalLeaf.weightOfOne ) );
			
		}
		else if (currentWeight < takenWeightOfCriticalItem)
			currentWeight = takenWeightOfCriticalItem;
		
		int weightNoPruning = weightOfItemChecked * maxNoOfItems;

		// Adding items until slack is ALMOST exhausted.
		while(!exhaustedLeftItems) {

			if (currentNode.parent == null) {
				exhaustedLeftItems = true;
				break;
			}

			boolean rightChild = currentNode.parent.right == currentNode;
			
			if (rightChild) {
				
				if (currentNode.leftNeighbor == null) {	
					exhaustedLeftItems = true;
					break;
				}

				if ( profitSlack + efficiencyOfItemChecked * ( currentWeight + currentNode.leftNeighbor.getWSum() ) - 
					 ( currentProfit + currentNode.leftNeighbor.getPSum() ) - profitFromCriticalTaken < 0) {

					// Going to left neighbor exhausts slack.
					break;
					
				}
				else {
					// Going to left neighbor does not exhaust slack.
										
					currentNode = currentNode.parent;
					currentWeight += currentNode.left.getWSum();
					currentProfit += currentNode.left.getPSum();

				}
				
				continue;
			}

			// currentNode is the left child.
			if (!rightChild) {
			
				if (currentNode.leftNeighbor == null) {	
					exhaustedLeftItems = true;
					break;
				}

				if ( profitSlack + efficiencyOfItemChecked * ( currentWeight + currentNode.leftNeighbor.getWSum() ) 
						- ( currentProfit + currentNode.leftNeighbor.getPSum() ) - profitFromCriticalTaken < 0 ) {
					
					// Going to left neighbor would exceeded slack.
					break;
					
				}
				
				currentNode = currentNode.leftNeighbor;
				currentWeight += currentNode.getWSum();
				currentProfit += currentNode.getPSum();
				
			}
			
		}
		
		// Slack has been almost exhausted or left items has been exhausted.

		if (!exhaustedLeftItems)
			while ( true ) {

				if ( profitSlack + efficiencyOfItemChecked * ( currentWeight + currentNode.leftNeighbor.getWSum() ) - 
						 ( currentProfit + currentNode.leftNeighbor.getPSum() ) - profitFromCriticalTaken < 0) {

						// Going to left neighbor exhausts slack.
						if (!currentNode.isLeaf())
							currentNode = currentNode.left;
						else
							break;
						
					}
					else {
						// Going to left neighbor does not exhaust slack.
											
						currentNode = currentNode.leftNeighbor;
						currentWeight += currentNode.getWSum();
						currentProfit += currentNode.getPSum();

					}
				
				if (currentNode.isLeaf())
					break;
			}
		
		if (!exhaustedLeftItems && currentNode.leftNeighbor.getPSum() == 0)
			currentNode = currentNode.leftNeighbor;
		
		// Found the last leaf before exceeding slack. 
		
		if (currentWeight > weightNoPruning ) {
			// Checked item does not have anything mandatory.
			// Next item must have weightNoPruning larger than currentWeight.
			return currentWeight;
		}
		
		// Not the whole slackProfit used and not the whole of item being checked is covered yet. Using item on the left (if exists) to use remaining 
		// profit.

		double lastWeight = profitSlack + currentWeight * profitOfItemChecked / (double)  weightOfItemChecked - currentProfit - profitFromCriticalTaken;

		if (!exhaustedLeftItems) {
			
			double efficiencyLoss = - profitOfItemChecked /  (double) weightOfItemChecked + currentNode.leftNeighbor.getPSum() / (double)  currentNode.leftNeighbor.getWSum();
			lastWeight /= efficiencyLoss;
			
			// Playing safe, we can replace more, so we do not make an item mandatory when we should not.
			return currentWeight + (int) Math.ceil( lastWeight );
			
		}
		else {

			double efficiencyLoss = profitOfItemChecked / (double)  weightOfItemChecked;
			lastWeight /= efficiencyLoss;
			
			// Playing safe, we can replace more, so we do not make an item mandatory when we should not.
			return currentWeight + (int) Math.ceil( lastWeight );
			
		}
					
	}

	
	
	/**
	 * 
	 * It finds previous leaf of a maximum weight of at least weight, so 
	 * it can have some parts of it forbidden. 
	 * 
	 * @param leaf starting leaf, the result must be to the left of this leaf.
	 * @param weight weight condition which must be satisfied by the found leaf.
	 * @return tree leaf on the left to the supplied leaf with at least specified weight.
	 */
	public TreeLeaf findPreviousLeafAtLeastOfWeight(TreeLeaf leaf, int weight) {

		if (leaf.leftNeighbor == null)
			return null;
		
		if (leaf.leftNeighbor.getWMax() > weight)
			return (TreeLeaf) leaf.leftNeighbor;
		
		TreeNode currentNode = leaf.parent;
		
		while (currentNode != null) {
			if (currentNode.leftNeighbor == null)
				return null;
			if (currentNode.leftNeighbor.getWMax() <= weight)
				currentNode = currentNode.parent;
			else {
				currentNode = currentNode.leftNeighbor;
				break;
			}
		}
		
		if (currentNode == null)
			return null;
		
		while (!currentNode.isLeaf()) {
			// Preference to right node if both nodes are above weight.
			if (currentNode.right.getWMax() > weight)
				currentNode = currentNode.right;
			else
				currentNode = currentNode.left;
		}
		
		return (TreeLeaf) currentNode;
	
	}
	
	
	
	
	/**
	 * It computes the minimum of capacity variable for knapsack 
	 * constraint given the minimum requirement for profit. 
	 * 
	 * @param minProfit minimum profit obtained by the knapsack. 
	 * @return it returns the minimum required weight to satisfy min
	 * profit requirements.
	 */
	public int computeMinWeight(int minProfit) {

		if (minProfit == 0)
			return 0;
		
		TreeNode current = root;		
		int usedCapacity = 0;
		int obtainedProfit = 0;
		
		while (!current.isLeaf()) {
			
				if (current.left.getWSum() == 0) {
					current = current.right;
				} else if (current.right.getWSum() == 0) {
					current = current.left;
				} else
				if (obtainedProfit + current.left.getPSum() <= minProfit) {

					usedCapacity += current.left.getWSum();
					/* we add the profit of all the left part */
					obtainedProfit += current.left.getPSum();
					
					current = current.right;
				} else {
					current = current.left;
				}
		}

		int remainingProfitToFind = minProfit - obtainedProfit;
		
		TreeLeaf finalLeaf = (TreeLeaf) current;

		int minWeightForCurrentTree = (int) Math.ceil( remainingProfitToFind * finalLeaf.weightOfOne / (double) finalLeaf.profitOfOne );

		return alreadyUsedCapacity + minWeightForCurrentTree;
		
	}
	
	

	
	
	/**
	 * It computes the minimum of capacity variable for knapsack 
	 * constraint given the minimum requirement for profit. 
	 * 
	 * @param minWeight - the minimum of weight within a knapsack. 
	 * @return it returns the minimum required weight to satisfy min
	 * profit requirements.
	 */
	public int computeMinProfit(int minWeight) {

		if (minWeight == 0)
			return 0;
		
		TreeNode current = root;		
		int usedCapacity = 0;
		int obtainedProfit = 0;
		
		while (!current.isLeaf()) {
			
				if (current.left.getWSum() == 0) {
					current = current.right;
				} else if (current.right.getWSum() == 0) {
					current = current.left;
				} else
				if (usedCapacity + current.right.getWSum() <= minWeight) {

					usedCapacity += current.right.getWSum();
					/* we add the profit of all the left part */
					obtainedProfit += current.right.getPSum();
					
					current = current.left;
				} else {
					current = current.right;
				}
		}

		int remainingWeightToFind = minWeight - usedCapacity;
		
		TreeLeaf finalLeaf = (TreeLeaf) current;

		int minProfitForCurrentTree = (int) Math.ceil( remainingWeightToFind * finalLeaf.profitOfOne / (double) finalLeaf.weightOfOne );

		return alreadyObtainedProfit + minProfitForCurrentTree;
		
	}


}
