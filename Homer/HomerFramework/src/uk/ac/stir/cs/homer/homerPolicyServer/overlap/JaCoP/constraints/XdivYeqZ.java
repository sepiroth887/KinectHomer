/**
 *  XdivYeqZ.java
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


package uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints;

import java.util.ArrayList;

import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntDomain;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntVar;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Store;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Var;


/**
 * Constraint X div Y #= Z
 *
 * @author Krzysztof Kuchcinski and Radoslaw Szymanek
 * @version 3.1.1 ("toString" corrected by Kenneth J. Turner to print arguments
 *   in the order "x, y, z")
 */

public class XdivYeqZ extends Constraint {

	static int counter = 1;

	/**
	 * It specifies variable x in constraint x / y = z.
	 */
	public IntVar x;

	/**
	 * It specifies variable y in constraint x / y = z.
	 */
	public IntVar y;

	/**
	 * It specifies variable z in constraint x / y = z.
	 */
	public IntVar z;

	/**
	 * It specifies the arguments required to be saved by an XML format as well as
	 * the constructor being called to recreate an object from an XML format.
	 */
	public static String[] xmlAttributes = {"x", "y", "z"};


	/**
	 * It constructs a constraint X * Y = Z.
	 * @param x variable x.
	 * @param y variable y.
	 * @param z variable z.
	 */
	public XdivYeqZ(IntVar x, IntVar y, IntVar z) {
		assert (x != null) : "Variable x is null";
		assert (y != null) : "Variable y is null";
		assert (z != null) : "Variable z is null";

		numberId = counter++;
		numberArgs = 3;

		this.x = x;
		this.y = y;
		this.z = z;

	}

	@Override
	public ArrayList<Var> arguments() {

		ArrayList<Var> variables = new ArrayList<Var>(3);

		variables.add(z);
		variables.add(y);
		variables.add(x);
		return variables;
	}

	@Override
	public void consistency (Store store) {

		// it must stay as the code below assumes y is never equal to 0.
		y.domain.inComplement(store.level, y, 0);

		int reminderMin, reminderMax;

		do {

			store.propagationHasOccurred = false;

			//@todo, why remainderMin does not depend on z.min? the same for remainderMax.
			if (x.min() >= 0) {
				reminderMin = 0;
				reminderMax = Math.max(Math.abs(y.min()), Math.abs(y.max()))  - 1;
			}
			else if (x.max() < 0) {
				reminderMax = 0;
				reminderMin = - Math.max(Math.abs(y.min()), Math.abs(y.max())) + 1;
			}
			else {
				reminderMin = Math.min(Math.min(y.min(),-y.min()), Math.min(y.max(),-y.max())) + 1;
				reminderMax = Math.max(Math.max(y.min(),-y.min()), Math.max(y.max(),-y.max())) - 1;
			}

			int zMin = IntDomain.MaxInt, zMax = IntDomain.MinInt;
			int yMin = IntDomain.MaxInt, yMax = IntDomain.MinInt;
			int xMin = IntDomain.MaxInt, xMax = IntDomain.MinInt;

			// @todo, most likely can change ?ContainsZero = ?.domain.contains(0);

			boolean xContaintsZero = x.min() <=0 && x.max() >= 0;
			boolean yContaintsZero = y.min() <=0 && y.max() >= 0;

			// Bounds for Z
			if ( !(xContaintsZero && yContaintsZero) ) {// if Z and Y contains 0 then X can have any values!!!
				if (y.min() <= -1 && y.max() >= 1) {
					zMin = getMin(x.min(), x.max(), reminderMin, reminderMax);
					zMax = getMax(x.min(), x.max(), reminderMin, reminderMax);

				}
				else {
					float div1, div2, div3, div4;
					div1 = (float)(x.min() - reminderMax)/(float)y.min();
					div2 = (float)(x.max() - reminderMin)/(float)y.min();
					div3 = (float)(x.min() - reminderMax)/(float)y.max();
					div4 = (float)(x.max() - reminderMin)/(float)y.max();
					float min = Math.min(Math.min(div1,div2), Math.min(div3,div4));
					float max = Math.max(Math.max(div1,div2), Math.max(div3,div4));
					zMin = (int)Math.round( Math.ceil( min) );
					zMax = (int)Math.round( Math.floor( max ));
				}

				if (zMin > zMax)
				    throw Store.failException;

				z.domain.in(store.level, z, zMin, zMax);

			}
			else {
			    zMin = getMin(x.min(), x.max(), reminderMin, reminderMax);
			    zMax = getMax(x.min(), x.max(), reminderMin, reminderMax);

			    z.domain.in(store.level, z, zMin, zMax);
			}

			boolean zContaintsZero = z.min() <=0 && z.max() >= 0;

			// Bounds for Y
			if ( !(xContaintsZero && zContaintsZero) ) {// if Z and X contains 0 then Y can have any values!!!
				if (z.min() <= -1 && z.max() >= 1) {
					yMin = getMin(x.min(), x.max(), reminderMin, reminderMax);
					yMax = getMax(x.min(), x.max(), reminderMin, reminderMax);
				}
				else {
					float div1, div2, div3, div4;
					if (z.min() != 0) {
						div1 = (float)(x.min() - reminderMax)/(float)z.min();
						div2 = (float)(x.max() - reminderMin)/(float)z.min();
					}
					else {
						div1 = (float)IntDomain.MinInt;
						div2 = (float)IntDomain.MaxInt;
					}
					if (z.max() != 0) {
						div3 = (float)(x.min() - reminderMax)/(float)z.max();
						div4 = (float)(x.max() - reminderMin)/(float)z.max();
					}
					else {
						div3 = (float)IntDomain.MinInt;
						div4 = (float)IntDomain.MaxInt;
					}
					float min = Math.min(Math.min(div1,div2), Math.min(div3,div4));
					float max = Math.max(Math.max(div1,div2), Math.max(div3,div4));
					yMin = (int)Math.round( Math.ceil( min) );
					yMax = (int)Math.round( Math.floor( max ));
				}

				if (yMin > yMax)
				    throw Store.failException;

				y.domain.in(store.level, y, yMin, yMax);
			}

			// Bounds for X
			int mul;
			// 		mul = X.min() * Y.min();
			mul = multiply(z.min(), y.min());
			if (mul < xMin) xMin = mul;
			if (mul > xMax) xMax = mul;
			// 		mul = X.min() * Y.max();
			mul = multiply(z.min(), y.max());
			if (mul < xMin) xMin = mul;
			if (mul > xMax) xMax = mul;
			// 		mul = X.max() * Y.min();
			mul = multiply(z.max(), y.min());
			if (mul < xMin) xMin = mul;
			if (mul > xMax) xMax = mul;
			// 		mul = X.max() * Y.max();
			mul = multiply(z.max(),y.max());
			if (mul < xMin) xMin = mul;
			if (mul > xMax) xMax = mul;
			// x * y = z + r

			int rMin = x.min() - xMax;
			int rMax = x.max() - xMin;

			x.domain.in(store.level, x, getMin(xMin, xMax, rMin, rMax), getMax(xMin, xMax, rMin, rMax));

		} while (store.propagationHasOccurred);

	}

	int getMin(int zMin, int zMax, int reminderMin, int reminderMax) {
		int minimal = uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntDomain.MaxInt;

		minimal = (minimal > zMin - reminderMin) ? zMin - reminderMin : minimal;
		minimal = (minimal > -zMin - reminderMin) ? -zMin - reminderMin : minimal;
		minimal = (minimal > zMin + reminderMin) ? zMin + reminderMin : minimal;
		minimal = (minimal > -zMin + reminderMin) ? -zMin + reminderMin : minimal;
		minimal = (minimal > zMin - reminderMax) ? zMin - reminderMax : minimal;
		minimal = (minimal > -zMin - reminderMax) ? -zMin - reminderMax : minimal;
		minimal = (minimal > zMin + reminderMax) ? zMin + reminderMax : minimal;
		minimal = (minimal > -zMin + reminderMax) ? -zMin + reminderMax : minimal;

		minimal = (minimal > zMax - reminderMin) ? zMax - reminderMin : minimal;
		minimal = (minimal > -zMax - reminderMin) ? -zMax - reminderMin : minimal;
		minimal = (minimal > zMax + reminderMin) ? zMax + reminderMin : minimal;
		minimal = (minimal > -zMax + reminderMin) ? -zMax + reminderMin : minimal;
		minimal = (minimal > zMax - reminderMax) ? zMax - reminderMax : minimal;
		minimal = (minimal > -zMax - reminderMax) ? -zMax - reminderMax : minimal;
		minimal = (minimal > zMax + reminderMax) ? zMax + reminderMax : minimal;
		minimal = (minimal > -zMax + reminderMax) ? -zMax + reminderMax : minimal;

		return minimal;
	}

	int getMax(int zMin, int zMax, int reminderMin, int reminderMax) {
		int maximal = uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntDomain.MinInt;

		maximal = (maximal < zMin - reminderMin) ? zMin - reminderMin : maximal;
		maximal = (maximal < -zMin - reminderMin) ? -zMin - reminderMin : maximal;
		maximal = (maximal < zMin + reminderMin) ? zMin + reminderMin : maximal;
		maximal = (maximal < -zMin + reminderMin) ? -zMin + reminderMin : maximal;
		maximal = (maximal < zMin - reminderMax) ? zMin - reminderMax : maximal;
		maximal = (maximal < -zMin - reminderMax) ? -zMin - reminderMax : maximal;
		maximal = (maximal < zMin + reminderMax) ? zMin + reminderMax : maximal;
		maximal = (maximal < -zMin + reminderMax) ? -zMin + reminderMax : maximal;

		maximal = (maximal < zMax - reminderMin) ? zMax - reminderMin : maximal;
		maximal = (maximal < -zMax - reminderMin) ? -zMax - reminderMin : maximal;
		maximal = (maximal < zMax + reminderMin) ? zMax + reminderMin : maximal;
		maximal = (maximal < -zMax + reminderMin) ? -zMax + reminderMin : maximal;
		maximal = (maximal < zMax - reminderMax) ? zMax - reminderMax : maximal;
		maximal = (maximal < -zMax - reminderMax) ? -zMax - reminderMax : maximal;
		maximal = (maximal < zMax + reminderMax) ? zMax + reminderMax : maximal;
		maximal = (maximal < -zMax + reminderMax) ? -zMax + reminderMax : maximal;

		return maximal;
	}

	@Override
	public int getConsistencyPruningEvent(Var var) {

		// If consistency function mode
		if (consistencyPruningEvents != null) {
			Integer possibleEvent = consistencyPruningEvents.get(var);
			if (possibleEvent != null)
				return possibleEvent;
		}
		return IntDomain.BOUND;
	}

	@Override
	public String id() {
		if (id != null)
			return id;
		else
			return this.getClass().getSimpleName() + numberId;
	}

	@Override
	public void impose(Store store) {
		z.putModelConstraint(this, getConsistencyPruningEvent(z));
		y.putModelConstraint(this, getConsistencyPruningEvent(y));
		x.putModelConstraint(this, getConsistencyPruningEvent(x));
		store.addChanged(this);
		store.countConstraint();
	}

	@Override
	public void removeConstraint() {
		z.removeConstraint(this);
		y.removeConstraint(this);
		x.removeConstraint(this);
	}

	@Override
	public boolean satisfied() {
		IntDomain xDom = z.dom(), yDom = y.dom(), zDom = x.dom();

		return xDom.singleton() && yDom.singleton() && zDom.singleton() &&
		( yDom.min() * xDom.min() <= zDom.min() || yDom.min() * xDom.min() < zDom.min() + yDom.min());
	}

	@Override
	public String toString() {

		return id() + " : XdivYeqZ(" + x + ", " + y + ", " + z + " )";
	}

	@Override
	public void increaseWeight() {
		if (increaseWeight) {
			z.weight++;
			y.weight++;
			x.weight++;
		}
	}

}
