/**
 *  XmodCeqZ.java
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
 * Constraint X mod C = Z
 *
 * @author Kenneth J. Turner (adapted from XmodYeqZ.java by Krzysztof Kuchcinski
 *         and Radoslaw Szymanek)
 * @version 3.1.1
 */

public class XmodCeqZ extends Constraint {

	static int counter = 1;

	/**
	 * It specifies variable x in constraint x mod c = z.
	 */
	public IntVar x;

	/**
	 * It specifies constant c in constraint x mod c = z.
	 */
	public int c;

	/**
	 * It specifies variable z in constraint x mod c = z.
	 */
	public IntVar z;

	/**
	 * It specifies the arguments required to be saved by an XML format as well as
	 * the constructor being called to recreate an object from an XML format.
	 */
	public static String[] xmlAttributes = {"x", "c", "z"};

	/**
	 * It constructs a constraint X * C = Z.
	 * @param x variable x.
	 * @param c constant c.
	 * @param z variable z.
	 */
	public XmodCeqZ(IntVar x, int c, IntVar z) {

		assert (x != null) : "Variable x is null";
		assert (z != null) : "Variable z is null";

		numberId = counter++;
		numberArgs = 3;

		this.x = x;
		this.c = c;
		this.z = z;
	}

	@Override
	public ArrayList<Var> arguments() {

		ArrayList<Var> variables = new ArrayList<Var>(3);

		variables.add(z);
		variables.add(x);
		return variables;
	}

	@Override
	public void consistency (Store store) {

		int resultMin=IntDomain.MinInt;
		int resultMax=IntDomain.MaxInt;

		do {

			store.propagationHasOccurred = false;

			// Compute bounds for reminder

			int reminderMin, reminderMax;

			if (x.min() >= 0) {
				reminderMin = 0;
				reminderMax = Math.max(Math.abs(c), Math.abs(c))  - 1;
			}
			else if (x.max() < 0) {
				reminderMax = 0;
				reminderMin = - Math.max(Math.abs(c), Math.abs(c)) + 1;
			}
			else {
				reminderMin = Math.min(Math.min(c,-c), Math.min(c,-c)) + 1;
				reminderMax = Math.max(Math.max(c,-c), Math.max(c,-c)) - 1;
			}

			z.domain.in(store.level, z, reminderMin, reminderMax);

			boolean xContainsZero = x.min() <= 0 && x.max() >= 0;
			boolean cContainsZero = c <= 0 && c >= 0;

			// Bounds for result
			if ( ! (xContainsZero && cContainsZero) ) {// if X and C contains 0 then Z can have any value.

				int oldResultMin = resultMin, oldResultMax = resultMax;

				if (c <= -1 && c >= 1) {
					resultMin = getMin(x.min(), x.max(), reminderMin, reminderMax);
					resultMax = getMax(x.min(), x.max(), reminderMin, reminderMax);
				}
				else {
					// y is either positive or negative, not both.
					float div1, div2, div3, div4;

					div1 = (float)(x.min() - z.max())/(float)c;
					div2 = (float)(x.max() - z.min())/(float)c;
					div3 = (float)(x.min() - z.max())/(float)c;
					div4 = (float)(x.max() - z.min())/(float)c;

					float min = Math.min(Math.min(div1,div2), Math.min(div3,div4));
					float max = Math.max(Math.max(div1,div2), Math.max(div3,div4));
					resultMin = (int)Math.round( Math.ceil( min) );
					resultMax = (int)Math.round( Math.floor( max ));
				}

				if (resultMin > resultMax)
				throw Store.failException;

				// TODO, Do we really need this for fixpoint?
				if (oldResultMin != resultMin || oldResultMax != resultMax)
					store.propagationHasOccurred = true;

			}

			boolean resultContainsZero = resultMin <=0 && resultMax >= 0;

			int cMin = IntDomain.MaxInt;
			int cMax = IntDomain.MinInt;

			// Bounds for Y
			if ( !(xContainsZero && resultContainsZero) ) {// if Z and X contains 0 then C can have any value.
				if ( resultMin <= -1 && resultMax >= 1) {
					cMin = getMin(x.min(), x.max(), reminderMin, reminderMax);
					cMax = getMax(x.min(), x.max(), reminderMin, reminderMax);
				}
				else {
					float div1, div2, div3, div4;
					if (resultMin != 0) {
						div1 = (float)(x.min() - z.max())/(float)resultMin;
						div2 = (float)(x.max() - z.min())/(float)resultMin;
					}
					else {
						div1 = (float)IntDomain.MinInt;
						div2 = (float)IntDomain.MaxInt;
					}
					if (resultMax != 0) {
						div3 = (float)(x.min() - z.max())/(float)resultMax;
						div4 = (float)(x.max() - z.min())/(float)resultMax;
					}
					else {
						div3 = (float)IntDomain.MinInt;
						div4 = (float)IntDomain.MaxInt;
					}
					float min = Math.min(Math.min(div1,div2), Math.min(div3,div4));
					float max = Math.max(Math.max(div1,div2), Math.max(div3,div4));
					cMin = (int)Math.round( Math.ceil( min) );
					cMax = (int)Math.round( Math.floor( max ));
				}

				if (cMin > cMax)
				throw Store.failException;

			}

			int mul;
			int zMin = IntDomain.MaxInt, zMax = IntDomain.MinInt;

			// Bounds for Z and reminder
			// 	mul = X.min() * C;
			mul = multiply(resultMin, c);
			if (mul < zMin) zMin = mul;
			if (mul > zMax) zMax = mul;
			// 	mul = X.min() * C;
			mul = multiply(resultMin, c);
			if (mul < zMin) zMin = mul;
			if (mul > zMax) zMax = mul;
			// 	mul = X.max() * C;
			mul = multiply(resultMax, c);
			if (mul < zMin) zMin = mul;
			if (mul > zMax) zMax = mul;
			// 	mul = X.max() * C;
			mul = multiply(resultMax,c);
			if (mul < zMin) zMin = mul;
			if (mul > zMax) zMax = mul;
			// x * y = z + r

			reminderMin = x.min() - zMax;
			reminderMax = x.max() - zMin;

			z.domain.in(store.level, z, reminderMin, reminderMax);

			x.domain.in(store.level, x, zMin + z.min(), zMax + z.max());

			assert checkSolution(resultMin, resultMax) == null : checkSolution(resultMin, resultMax) ;

		} while (store.propagationHasOccurred);

	}

	int getMin(int xMin, int xMax, int reminderMin, int reminderMax) {
		int minimal = uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntDomain.MaxInt;

		minimal = (minimal > xMin - reminderMin) ? xMin - reminderMin : minimal;
		minimal = (minimal > -xMin - reminderMin) ? -xMin - reminderMin : minimal;
		minimal = (minimal > xMin + reminderMin) ? xMin + reminderMin : minimal;
		minimal = (minimal > -xMin + reminderMin) ? -xMin + reminderMin : minimal;
		minimal = (minimal > xMin - reminderMax) ? xMin - reminderMax : minimal;
		minimal = (minimal > -xMin - reminderMax) ? -xMin - reminderMax : minimal;
		minimal = (minimal > xMin + reminderMax) ? xMin + reminderMax : minimal;
		minimal = (minimal > -xMin + reminderMax) ? -xMin + reminderMax : minimal;

		minimal = (minimal > xMax - reminderMin) ? xMax - reminderMin : minimal;
		minimal = (minimal > -xMax - reminderMin) ? -xMax - reminderMin : minimal;
		minimal = (minimal > xMax + reminderMin) ? xMax + reminderMin : minimal;
		minimal = (minimal > -xMax + reminderMin) ? -xMax + reminderMin : minimal;
		minimal = (minimal > xMax - reminderMax) ? xMax - reminderMax : minimal;
		minimal = (minimal > -xMax - reminderMax) ? -xMax - reminderMax : minimal;
		minimal = (minimal > xMax + reminderMax) ? xMax + reminderMax : minimal;
		minimal = (minimal > -xMax + reminderMax) ? -xMax + reminderMax : minimal;

		return minimal;
	}

	int getMax(int xMin, int xMax, int reminderMin, int reminderMax) {
		int maximal = uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntDomain.MinInt;

		maximal = (maximal < xMax - reminderMin) ? xMax - reminderMin : maximal;
		maximal = (maximal < -xMax - reminderMin) ? -xMax - reminderMin : maximal;
		maximal = (maximal < xMax + reminderMin) ? xMax + reminderMin : maximal;
		maximal = (maximal < -xMax + reminderMin) ? -xMax + reminderMin : maximal;
		maximal = (maximal < xMax - reminderMax) ? xMax - reminderMax : maximal;
		maximal = (maximal < -xMax - reminderMax) ? -xMax - reminderMax : maximal;
		maximal = (maximal < xMax + reminderMax) ? xMax + reminderMax : maximal;
		maximal = (maximal < -xMax + reminderMax) ? -xMax + reminderMax : maximal;

		maximal = (maximal < xMin - reminderMin) ? xMin - reminderMin : maximal;
		maximal = (maximal < -xMin - reminderMin) ? -xMin - reminderMin : maximal;
		maximal = (maximal < xMin + reminderMin) ? xMin + reminderMin : maximal;
		maximal = (maximal < -xMin + reminderMin) ? -xMin + reminderMin : maximal;
		maximal = (maximal < xMin - reminderMax) ? xMin - reminderMax : maximal;
		maximal = (maximal < -xMin - reminderMax) ? -xMin - reminderMax : maximal;
		maximal = (maximal < xMin + reminderMax) ? xMin + reminderMax : maximal;
		maximal = (maximal < -xMin + reminderMax) ? -xMin + reminderMax : maximal;

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
		x.putModelConstraint(this, getConsistencyPruningEvent(x));
		store.addChanged(this);
		store.countConstraint();
	}

	@Override
	public void removeConstraint() {
		z.removeConstraint(this);
		x.removeConstraint(this);
	}

	@Override
	public boolean satisfied() {
		IntDomain Xdom = z.dom(), Zdom = x.dom();

		return Xdom.singleton() && Zdom.singleton() &&
		(c * Xdom.min() <= Zdom.min() || c * Xdom.min() < Zdom.min() + c);
	}

	@Override
	public String toString() {

		return id() + " : XmodCeqZ(" + x + ", " + c + ", " + z + " )";
	}

	@Override
	public void increaseWeight() {
		if (increaseWeight) {
			z.weight++;
			x.weight++;
		}
	}

	String checkSolution(int resultMin, int resultMax) {
		String result = null;

		if (z.singleton() && x.singleton()) {
			result = "Operation mod does not hold " + x + " mod " + c + " = " + z + "(result "+resultMin+".."+resultMax;
			for (int i=resultMin; i<=resultMax; i++) {
				if ( i*c + z.value() == x.value() )
					result = null;
			}
		}
		else
			result = null;
		return result;
	}
}
