/**
 *  XmulYeqZ.java 
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
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntervalDomain;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Store;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Var;


/**
 * Constraint X * Y #= Z
 * 
 * Boundary consistency is used.
 * 
 * @author Krzysztof Kuchcinski and Radoslaw Szymanek
 * @version 3.1
 */

public class XmulYeqZ extends Constraint {

	static int counter = 1;

	/**
	 * It specifies variable x in constraint x * y = z. 
	 */
	public IntVar x;

	/**
	 * It specifies variable y in constraint x * y = z. 
	 */
	public IntVar y;

	/**
	 * It specifies variable z in constraint x * y = z. 
	 */
	public IntVar z;

	boolean xSquare = false;

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
	public XmulYeqZ(IntVar x, IntVar y, IntVar z) {

		assert (x != null) : "Variable x is null";
		assert (y != null) : "Variable y is null";
		assert (z != null) : "Variable z is null";

		numberId = counter++;
		numberArgs = 3;

		xSquare = (x == y) ? true : false;

		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public ArrayList<Var> arguments() {

		ArrayList<Var> variables = new ArrayList<Var>(3);

		variables.add(x);
		variables.add(y);
		variables.add(z);
		return variables;
	}

	@Override
	public void consistency (Store store) {

		if (xSquare)  // X^2 = Z
			do {
				
				store.propagationHasOccurred = false;

				int ZMin=IntDomain.MaxInt, ZMax=IntDomain.MinInt;
				int mul;

				// Bounds for Z
				// 		mul = X.min() * Y.min();
				mul = multiply(x.min(), y.min());
				if (mul < ZMin) ZMin = mul;
				if (mul > ZMax) ZMax = mul;

				// 		mul = X.min() * Y.max();
				mul = multiply(x.min(), y.max());
				if (mul < ZMin) ZMin = mul;
				if (mul > ZMax) ZMax = mul;
				// 		mul = X.max() * Y.min();
				mul = multiply(x.max(), y.min());
				if (mul < ZMin) ZMin = mul;
				if (mul > ZMax) ZMax = mul;

				// 		mul = X.max() * Y.max();
				mul = multiply(x.max(),y.max());
				if (mul < ZMin) ZMin = mul;
				if (mul > ZMax) ZMax = mul;

				// 		if (X.min() * X.max() < 0) ZMin=0;

				z.domain.in(store.level, z, ZMin, ZMax);

				int xMin = IntDomain.MaxInt, xMax = IntDomain.MinInt;

				// 				z.domain.inMin(store.level, z, 0);

				xMin = toInt( Math.round( Math.ceil( Math.sqrt((double)z.min()) )) );
				xMax = toInt( Math.round( Math.floor( Math.sqrt((double)z.max()) )) );

				if ( xMin > xMax )
					store.throwFailException(z);

				IntDomain dom = new IntervalDomain(-xMax, -xMin);
				dom.unionAdapt(xMin, xMax);

				x.domain.in(store.level, x, dom);

			} while(store.propagationHasOccurred);
		else    // X*Y=Z
			do {
				
				store.propagationHasOccurred = false;

				// It is needed as code below assumes x is not equal to zero. 
				if (x.singleton(0)) {
					z.domain.in(store.level, z, 0, 0);
					return;
				}

				// It is needed as code below assumes y is not equal to zero. 
				if (y.singleton(0)) {
					z.domain.in(store.level, z, 0, 0);
					return;
				}

				int xMin=IntDomain.MaxInt, xMax=IntDomain.MinInt; 
				int yMin=IntDomain.MaxInt, yMax=IntDomain.MinInt;
				int zMin=IntDomain.MaxInt, zMax=IntDomain.MinInt;

				boolean xContaintsZero = x.domain.contains(0);
				boolean zContaintsZero = z.domain.contains(0);
				boolean yContaintsZero = y.domain.contains(0);

				if (!zContaintsZero) {
					x.domain.inComplement(store.level, x, 0);
					y.domain.inComplement(store.level, y, 0);					
				}
				else {
					if (!xContaintsZero && !yContaintsZero) {
						z.domain.inComplement(store.level, z, 0);
					}
				}

				// Bounds for X
				// if Z and Y contains 0 then X can have any values.
				if ( !(zContaintsZero && yContaintsZero) ) {
					if (y.min() <= -1 && y.max() >= 1) {
						//@todo, why not divide by the smallest possible abs(y)?
						xMin = Math.min(Math.min(-z.min(), z.min()), Math.min(-z.max(), z.max()));
						xMax = -xMin; 
						x.domain.in(store.level, x, xMin, xMax);
					}
					else if (!yContaintsZero) {

						// y is either positive or negative, not both. 
						// !(zContainsZero && yContainsZero) => YcontainsZero => !zContainsZero

						float div1, div2, div3, div4;

						div1 = (float)z.min()/(float)y.min();
						div2 = (float)z.max()/(float)y.min();
						div3 = (float)z.min()/(float)y.max();
						div4 = (float)z.max()/(float)y.max();

						float min = Math.min(Math.min(div1,div2), Math.min(div3,div4));
						float max = Math.max(Math.max(div1,div2), Math.max(div3,div4));
						xMin = (int)Math.round( Math.ceil( min) );
						xMax = (int)Math.round( Math.floor( max ));

						if (xMin > xMax) 
							throw Store.failException;

						x.domain.in(store.level, x, xMin, xMax);

					}

				}

				// Bounds for Y
				// if Z and X contains 0 then Y can have any values!!!
				if ( !(zContaintsZero && xContaintsZero) ) {
					if (x.min() <= -1 && x.max() >= 1) {
						yMin = Math.min(Math.min(-z.min(), z.min()), Math.min(-z.max(), z.max()));
						yMax = -yMin; //Math.max(Math.max(-Z.min(), Z.min()), Math.max(-Z.max(), Z.max()));
						y.domain.in(store.level, y, yMin, yMax);
					}
					else if (!xContaintsZero) {

						float div1, div2, div3, div4;
						div1 = (float)z.min()/(float)x.min();
						div2 = (float)z.max()/(float)x.min();
						div3 = (float)z.min()/(float)x.max();
						div4 = (float)z.max()/(float)x.max();

						float min = Math.min(Math.min(div1,div2), Math.min(div3,div4));
						float max = Math.max(Math.max(div1,div2), Math.max(div3,div4));

						yMin = (int) Math.round( Math.ceil ( min ) );
						yMax = (int) Math.round( Math.floor( max ) );

						if (yMin > yMax)
							throw Store.failException;

						y.domain.in(store.level, y, yMin, yMax);

					}

				}

				// Bounds for Z
				int mul;
				// 		mul = X.min() * Y.min();
				mul = multiply(x.min(), y.min());
				if (mul < zMin) zMin = mul;
				if (mul > zMax) zMax = mul;
				// 		mul = X.min() * Y.max();
				mul = multiply(x.min(), y.max());
				if (mul < zMin) zMin = mul;
				if (mul > zMax) zMax = mul;
				// 		mul = X.max() * Y.min();
				mul = multiply(x.max(), y.min());
				if (mul < zMin) zMin = mul;
				if (mul > zMax) zMax = mul;
				// 		mul = X.max() * Y.max();
				mul = multiply(x.max(),y.max());
				if (mul < zMin) zMin = mul;
				if (mul > zMax) zMax = mul;
				z.domain.in(store.level, z, zMin, zMax);
			} while (store.propagationHasOccurred);
		
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
		x.putModelConstraint(this, getConsistencyPruningEvent(x));
		y.putModelConstraint(this, getConsistencyPruningEvent(y));
		z.putModelConstraint(this, getConsistencyPruningEvent(z));
		store.addChanged(this);
		store.countConstraint();
	}

	@Override
	public void removeConstraint() {
		x.removeConstraint(this);
		y.removeConstraint(this);
		z.removeConstraint(this);
	}

	@Override
	public boolean satisfied() {
		IntDomain xDom = x.dom(), yDom = y.dom(), zDom = z.dom();
		return xDom.singleton() && yDom.singleton() && zDom.singleton()
		&& xDom.min() * yDom.min() == zDom.min();
	}

	@Override
	public String toString() {

		return id() + " : XmulYeqZ(" + x + ", " + y + ", " + z + " )";
	}


	@Override
	public void increaseWeight() {
		if (increaseWeight) {
			x.weight++;
			y.weight++;
			z.weight++;
		}
	}

}
