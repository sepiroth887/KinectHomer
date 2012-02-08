package uk.ac.stir.cs.homer.homerPolicyServer.overlap;

// PolicyOverlap.java

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.And;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.Not;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.Or;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.PrimitiveConstraint;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XdivCeqZ;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XeqC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XeqY;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XgtC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XgtY;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XgteqC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XgteqY;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XltC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XltY;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XlteqC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XlteqY;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XmodCeqZ;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XneqC;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.constraints.XneqY;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.BooleanVar;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Domain;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntDomain;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntVar;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Store;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.Var;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.DepthFirstSearch;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.IndomainMin;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.SelectChoicePoint;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.search.SimpleSelect;

/**
  <p>
    This class is designed for checking overlap among triggers/conditions in a
    policy. It defines a number of general-purpose methods that conveniently
    wrap up PolicyOverlap constraints. Trigger/condition variables and
    constraints on them are declared, then it is determined if there is a
    solution for these variables. If there is, this means that the
    triggers/conditions of the given policies overlap (and may therefore lead to
    conflict). Earlier examples are taken from "Homer Policy Conflict Handling"
    by Claire Maternaghan; later examples are from the author.
  </p>

  <p>
    Compile and run this code as follows (use "java -ea" to see Jacop assertion
    failures):
  </p>

  <code>
    javac -cp ".;C:/usr/local/jacop/jacop.jar" PolicyOverlap.java
    java -cp ".;C:/usr/local/jacop/jacop.jar" PolicyOverlap
  </code>

  <p>
    Trigger and condition variables are mapped to Jacop integer variables. Each
    of these has a time field and a value field. The time field indicates the
    relative time at which the trigger/condition becomes true (0 up to but not
    including TIME_LIMIT). The value field indicates the variable value (0 up to
    but not including VALUE_LIMIT).
  </p>

  <p>
    The event names in triggers/conditions are represented by Jacop variables.
    Normally variables are declared as "intVar(event_name)", as this allows
    their use in "then". However, variables that are not used in this way can
    more efficiently be declared as "boolVar(event_name)". In the examples given
    below, event names are plain text. However, they could as well be unique
    identifiers (e.g. "t14631379" in place of trigger "front_door").
  </p>

  <p>
    If an event name appears more than once in a "then", each such instance is a
    separate event and needs to be distinguished (e.g. "house_temperature_0",
    "house_temperature_1").
  </p>

  <p>
    A trigger/condition may have an optional parameter that is represented by
    the value field of a Jacop variable. These values should be (small)
    integers. If the trigger/condition already has a numeric parameter, this is
    a fairly direct translation. As Jacop uses integers, decimal fractions need
    translation. For example, the numbers might be multiplied by 100 to allow
    for two decimal places (e.g. 94.6 is represented by 9460). Negative numbers
    could be mapped relative to the minimum of the actual value (e.g. -100.00
    could be represented by 0 and +100.00 by 2000).
  </p>

  <p>
    If a trigger/condition has a parameter that belongs to an enumerated list,
    it should be represented by its index in the list. Suppose an immerser event
    can have a temperature parameter that is "cold", "lukewarm", "hot" and
    "boiling". These values could be mapped to 0, 1, 2 and 3.
  </p>

  <p>
    When an "intVar" variable is declared, additional variables are also
    declared for its time and value counterparts. The main, time and value
    variables are related by constraints that reflect the formula:
  </p>

  <code>
    variable = value * TIME_LIMIT + time
  </code>

  <p>
    Syntactic sugar is provided for conveniently stating policy "when"
    clauses. In the following, "..." denotes repetition and "value" means a
    variable or an integer constant:
  </p>

  <code>
    constraint :=
      and(constraint, ...) | or(constraint, ...) | not(constraint) |
      then(variable, ...)
      eq(variable, value) | ne(variable, value) |
      gt(variable, value) | ge(variable, value) |
      lt(variable, value) | le(variable, value) |
      range(variable, minimum_constant, maximum_constant)
  </code>

  @author	Kenneth J. Turner
  @version	1.0 (19th July 2011)
  <br/>		1.1 (6th October 2011)
  <br/>		1.2 (18th January 2012)
*/
public class Kens {

  /* ============================= Constants ================================ */

  /** Suffix of a time variable */
  private final static String TIME_SUFFIX = "_t";

  /** Variable time 0 up to but not including this limit */
  private final static int TIME_LIMIT = 10;

  /** Variable value 0 up to but not including this limit */
  private final static int VALUE_LIMIT = 10000;

  /** Suffix of a value variable */
  private final static String VALUE_SUFFIX = "_v";

  /** Maximum value of an integer variable */
  private final static int VAR_MAXIMUM = VALUE_LIMIT * TIME_LIMIT;

  /** Minimum value of an integer variable */
  private final static int VAR_MINIMUM = 0;

  /* ============================= Variables ================================ */

  /** Constraint store */
  private Store store;

  /** Declared variables (and their time/value counterparts) */
  private ArrayList<Var> variables;

  /* ============================ Main Methods ============================== */

  
  /**
    Main program.

    @param arguments	command-line arguments (unused)
  */
  public static void main(String[] arguments) {
    Kens policyOverlap = new Kens();
    /*
    policyOverlap.checkExample1();
    policyOverlap.checkExample2();
    policyOverlap.checkExample3();
    policyOverlap.checkExample4();
    policyOverlap.checkExample5();
    policyOverlap.checkExample6();
    policyOverlap.checkExample7();
    policyOverlap.checkExample8();
    policyOverlap.checkExample9();
    policyOverlap.checkExample10();
    policyOverlap.checkExample11();
    policyOverlap.checkExample12();
    */
    
    policyOverlap.checkExample13();
  }

  /**
    Check constraints for Example 1:

    <code>
      policy 1:
	when it is a Saturday and
	     the lounge lamp turns on

      policy 2:
	when (the lounge lamp turns on then
	      the lounge curtains open then
	      the lounge lamp turns off)

      example 1 has overlap [31 msec]
	day = 5 [at time 0]
	lounge_lamp_power_1 = 100 [at time 0]
	lounge_lamp_power_2 = 0 [at time 2]
	lounge_curtains_state = 1 [at time 1]
    </code>
  */
  private void checkExample1() {
    initialiseConstraints();
    IntVar day = intVar("day");
    IntVar lounge_lamp_power_1 = intVar("lounge_lamp_power_1");
    IntVar lounge_lamp_power_2 = intVar("lounge_lamp_power_2");
    IntVar lounge_curtains_state = intVar("lounge_curtains_state");
    store.impose(
      and(
	and(eq(day, 5), eq(lounge_lamp_power_1, 100)),
	then(eq(lounge_lamp_power_1, 100), eq(lounge_curtains_state, 1),
	  eq(lounge_lamp_power_2, 0))
      )
    );
    checkConstraints("example 1");
  }

  /**
    Check constraints for Example 2:

    <code>
      policy 1:
	when it is a Saturday and
	     the lounge lamp turns on

      policy 2:
	when the lounge lamp turns on or
	     the lounge curtains open

      example 2 has overlap [31 msec]
	day = 5 [at time 0]
	lounge_lamp_power = 100 [at time 0]
	lounge_curtains_state = 0 [at time 0]
    </code>
  */
  private void checkExample2() {
    initialiseConstraints();
    IntVar day = intVar("day");
    IntVar lounge_lamp_power = intVar("lounge_lamp_power");
    IntVar lounge_curtains_state = intVar("lounge_curtains_state");
    store.impose(
      and(
	and(eq(day, 5), eq(lounge_lamp_power, 100)),
	or(eq(lounge_lamp_power, 100), eq(lounge_curtains_state, 1))
      )
    );
    checkConstraints("example 2");
  }

  /**
    Check constraints for Example 3:

    <code>
      policy 1:
	when it is a Saturday and
	     the lounge lamp turns on

      policy 2:
	when the lounge lamp turns on and
	     it is a Sunday

      example 3 has no overlap [0 msec]
    </code>
  */
  private void checkExample3() {
    initialiseConstraints();
    IntVar day = intVar("day");
    IntVar lounge_lamp_power = intVar("lounge_lamp_power");
    store.impose(
      and(
	and(eq(day, 5), eq(lounge_lamp_power, 100)),
	and(eq(lounge_lamp_power, 100), eq(day, 6))
      )
    );
    checkConstraints("example 3");
  }

  /**
    Check constraints for Example 4:

    <code>
      policy 1:
	when it is a Saturday and
	     the lounge lamp turns on

      policy 2:
	when the lounge lamp turns on or
	     it is a Sunday

      example 4 has overlap [0 msec]
	day = 5 [at time 0]
	lounge_lamp_power = 100 [at time 0]
    </code>
  */
  private void checkExample4() {
    initialiseConstraints();
    IntVar day = intVar("day");
    IntVar lounge_lamp_power = intVar("lounge_lamp_power");
    store.impose(
      and(
	and(eq(day, 5), eq(lounge_lamp_power, 100)),
	or(eq(lounge_lamp_power, 100), eq(day, 6))
      )
    );
    checkConstraints("example 4");
  }

  /**
    Check constraints for Example 5:

    <code>
      policy 1:
	when it is a Saturday and
	     the lounge lamp turns on

      policy 2:
	when the lounge lamp is on

      example 5 has overlap [16 msec]
	day = 5 [at time 0]
	lounge_lamp_power = 100 [at time 0]
    </code>
  */
  private void checkExample5() {
    initialiseConstraints();
    IntVar day = intVar("day");
    IntVar lounge_lamp_power = intVar("lounge_lamp_power");
    store.impose(
      and(
	and(eq(day, 5), eq(lounge_lamp_power, 100)),
	eq(lounge_lamp_power, 100)
      )
    );
    checkConstraints("example 5");
  }

  /**
    Check constraints for Example 6:

    <code>
      policy 1:
	when it is a Saturday and
	     the lounge lamp turns on

      policy 2:
	when the lounge lamp is dimmed to 50%

      example 6 has no overlap [0 msec]
    </code>
  */
  private void checkExample6() {
    initialiseConstraints();
    IntVar day = intVar("day");
    IntVar lounge_lamp_power = intVar("lounge_lamp_power");
    store.impose(
      and(
	and(eq(day, 5), eq(lounge_lamp_power, 100)),
	eq(lounge_lamp_power, 50)
      )
    );
    checkConstraints("example 6");
  }

  /**
    Check constraints for Example 7:

    <code>
      policy 1:
	when day is Monday..Friday and
	     (movement detected in driveway then
	      Claire opens the garage door then
	      movement detected in garage then
	      garage door closes then
	      the front door opens)

      policy 2:
	when (movement detected in driveway then
	      someone opens the garage door then
	      movement detected in garage then
	      garage door closes then
	      the front door opens) and
	     the house temperature is below 20

      example 7 has overlap [0 msec]
	day = 1 [at time 0]
	movement_in_driveway = 0 [at time 0]
	claire_opens_garage = 0 [at time 1]
	someone_opens_garage = 0 [at time 1]
	movement_in_garage = 0 [at time 2]
	garage_door_closes = 0 [at time 3]
	front_door_opens = 0 [at time 4]
	house_temperature = 0 [at time 0]
    </code>
  */
  private void checkExample7() {
    initialiseConstraints();
    IntVar day = intVar("day");
    IntVar movement_in_driveway = intVar("movement_in_driveway");
    IntVar claire_opens_garage = intVar("claire_opens_garage");
    IntVar someone_opens_garage = intVar("someone_opens_garage");
    IntVar movement_in_garage = intVar("movement_in_garage");
    IntVar garage_door_closes = intVar("garage_door_closes");
    IntVar front_door_opens = intVar("front_door_opens");
    IntVar house_temperature = intVar("house_temperature");
    store.impose(
      and(
	and(
	  range(day, 1, 5),
	  then(movement_in_driveway, claire_opens_garage, movement_in_garage,
	    garage_door_closes, front_door_opens)),
	and(
	  then(movement_in_driveway, someone_opens_garage,
	    movement_in_garage, garage_door_closes, front_door_opens),
	  lt(house_temperature, 20))
      )
    );
    checkConstraints("example 7");
  }

  /**
    Check constraints for Example 8:

    <code>
      policy 1:
	when (the house temperature is or is above 18 then
	      the front door opens then
	      the house temperature is 14)

      policy 2:
	when (the house temperature is above 16 then
	      the front door opens then
	      Claire enters then
	      the house temperature is below 15)

      example 8 has overlap [0 msec]
	claire_enters = 0 [at time 2]
	front_door = 1 [at time 1]
	house_temperature_0 = 18 [at time 0]
	house_temperature_1 = 14 [at time 3]
    </code>
  */
  private void checkExample8() {
    initialiseConstraints();
    IntVar claire_enters = intVar("claire_enters");
    IntVar front_door = intVar("front_door");
    IntVar house_temperature_0 = intVar("house_temperature_0");
    IntVar house_temperature_1 = intVar("house_temperature_1");
    store.impose(
      and(
	then(ge(house_temperature_0, 18), eq(front_door,1),
	  eq(house_temperature_1, 14)),
	then(gt(house_temperature_0, 16), eq(front_door,1), claire_enters,
	  lt(house_temperature_1, 15))
      )
    );
    checkConstraints("example 8");
  }

  /**
    Check constraints for Example 9:

    <code>
      policy 1:
	when the front door is closed

      policy 2:
	when the front door is open

      example 9 has no overlap [0 msec]
    </code>
  */
  private void checkExample9() {
    initialiseConstraints();
    IntVar front_door_1 = intVar("front_door");
    IntVar front_door_2 = intVar("front_door");
    store.impose(
      and(
	eq(front_door_1, 0),
	eq(front_door_2, 1)
      )
    );
    checkConstraints("example 9");
  }

  /**
    Check constraints for Example 10:

    <code>
      policy 1:
	when the lounge lamp turns off

      policy 2:
	when (the lounge lamp turns on then
	      the lounge window opens then
	      the lounge lamp turns off)

      example 10 has no overlap [0 msec]
    </code>
  */
  private void checkExample10() {
    initialiseConstraints();
    IntVar lounge_lamp_power_1 = intVar("lounge_lamp_power_1");
    IntVar lounge_lamp_power_2 = intVar("lounge_lamp_power_2");
    IntVar lounge_window_state = intVar("lounge_window_state");
    store.impose(
      and(
	eq(lounge_lamp_power_1, 0),
	then(eq(lounge_lamp_power_1, 100), eq(lounge_window_state, 1),
	  eq(lounge_lamp_power_2, 0))
      )
    );
    checkConstraints("example 10");
  }

  /**
    Check constraints for Example 11:

    <code>
      policy 1:
	when the lounge door opens and
	     the lounge humidity = 15

      policy 2:
	when (the lounge humidity > 16 then
	      the lounge door opens then
	      the lounge window opens then
	      the lounge humidity = 15)

      example 11 has no overlap [0 msec]
    </code>
  */
  private void checkExample11() {
    initialiseConstraints();
    IntVar lounge_door_state = intVar("lounge_door_state");
    IntVar lounge_humidity_1 = intVar("lounge_humidity_1");
    IntVar lounge_humidity_2 = intVar("lounge_humidity_2");
    IntVar lounge_window_state = intVar("lounge_window_state");
    store.impose(
      and(
	and(eq(lounge_door_state, 1), eq(lounge_humidity_1, 15)),
	then(gt(lounge_humidity_1, 16), eq(lounge_door_state, 1),
	  eq(lounge_window_state, 1), eq(lounge_humidity_1, 15))
      )
    );                                   
    checkConstraints("example 11");
  }

  /**
    Check constraints for Example 12:

    <code>
      policy 1:
	when the lounge door opens

      policy 2:
	when (the lounge humidity > 16 then
	      the lounge door opens then
	      the lounge window opens then
	      the lounge humidity = 15)

      example 12 has overlap [16 msec]
	lounge_door_state = 1 [at time 1]
	lounge_humidity_1 = 17 [at time 0]
	lounge_humidity_2 = 15 [at time 3]
	lounge_window_state = 1 [at time 2]
    </code>
  */
  private void checkExample12() {
    initialiseConstraints();
    IntVar lounge_door_state = intVar("lounge_door_state");
    IntVar lounge_humidity_1 = intVar("lounge_humidity_1");
    IntVar lounge_humidity_2 = intVar("lounge_humidity_2");
    IntVar lounge_window_state = intVar("lounge_window_state");
    store.impose(
      and(
	eq(lounge_door_state, 1),
	then(gt(lounge_humidity_1, 16), eq(lounge_door_state, 1),
	  eq(lounge_window_state, 1), eq(lounge_humidity_2, 15))
      )
    );                                   
    checkConstraints("example 12");                       
  }
  
  
  private void checkExample13()
  {
	  // All show correct overlaps
	  checkTime("Example 13", 0, 1);
	  checkTime("Example 13", 0, 2);
	  checkTime("Example 13", 0, 4);
	  checkTime("Example 13", 0, 8);
	  checkTime("Example 13", 0, 10);
	  checkTime("Example 13", 0, 23);
	  
	  // Still all show correct overlaps
	  checkTime("Example 13", 0, 1);
	  checkTime("Example 13", 1, 2);
	  checkTime("Example 13", 1, 4);
	  checkTime("Example 13", 1, 8);
	  checkTime("Example 13", 1, 10);
	  checkTime("Example 13", 1, 23);
	  
	  // Stack overflow (- anything with a minimum hour of 3 onwards)
	  checkTime("Example 13", 3, 4);
	  
  }

  private void checkTime(String name, int hour1, int  hour2)
  {
	  initialiseConstraints();
	  IntVar minutes = intVar("minutes");
	  store.impose( and(eq(minutes, hour1*60), lt(minutes, hour2*60)));
	  checkConstraints(name + ": hours " + hour1 + " to " + hour2);
  }
  
  /* ========================== Generic Methods ============================= */

  /**
    Declare and store a boolean variable (may not be used in "then"
    constraints).

    @param variable	variable name
    @return		reference to declared variable
  */
  private BooleanVar boolVar(String variable) {
    BooleanVar var = new BooleanVar(store, variable);
    variables.add(var);
    return(var);
  }

  /**
    Check and report constraints for the given example.

    @param arguments	command-line arguments (unused)
  */
  private void checkConstraints(String example) {
    Var[] variableList = variables.toArray(new Var[0]);
    long start = System.currentTimeMillis();
    DepthFirstSearch search = new DepthFirstSearch();
    search.setPrintInfo(false);
    SelectChoicePoint select =
      new SimpleSelect(variableList, null, new IndomainMin());
    boolean result = search.labeling(store, select);
    long finish = System.currentTimeMillis();
    if (result) {
      System.out.println(
	example + " has overlap [" + (finish - start) + " msec]");
      for (int i = 0; i < variableList.length; i++) {
	Var var = variableList[i];
	String varName = var.id();
	if (!varName.endsWith(TIME_SUFFIX) && !varName.endsWith(VALUE_SUFFIX)) {
	  Domain varDomain = var.dom();
	  String varString = varDomain.toString();
	  if (varDomain.singleton() &&
	      varDomain instanceof IntDomain) {
	    int varInt = ((IntDomain) varDomain).value();
	    int varTime = varInt % TIME_LIMIT;
	    int varValue = varInt / TIME_LIMIT;
	    varString =
	      varName + " = " + varValue + " [at time " + varTime + "]";
	  }
	  System.out.println("  " + varString);
	}
      }
    }
    else
      System.out.println(
	example + " has no overlap [" + (finish - start) + " msec]");
  }

  /**
    Initialise variables and constraint store
  */
  private void initialiseConstraints() {
    variables = new ArrayList<Var>();
    store = new Store();
  }

  /**
    Declare and store an integer variable. Besides the main variable, additional
    variables are declared and stored for its time field and value field.

    @param variable	variable name
    @return		reference to declared variable
  */
  private IntVar intVar(String variable) {
    String variableTime = variable + TIME_SUFFIX;
    String variableValue = variable + VALUE_SUFFIX;
    IntVar var = new IntVar(store, variable, VAR_MINIMUM, VAR_MAXIMUM);
    IntVar varTime = new IntVar(store, variableTime, VAR_MINIMUM, VAR_MAXIMUM);
    IntVar varValue =
      new IntVar(store, variableValue, VAR_MINIMUM, VAR_MAXIMUM);
    variables.add(var);
    variables.add(varTime);
    variables.add(varValue);
    store.impose(new XmodCeqZ(var, TIME_LIMIT, varTime));
    store.impose(new XdivCeqZ(var, TIME_LIMIT, varValue));
    return(var);
  }

  /**
    Return the time variable associated with a primitive constraint that must be
    a comparison (eq, le, lt, ge, gt, ne). The variable derives from the
    first-named value in the constraint ("x"). This assumes that Jacop respects
    this naming convention (which is true of version 3.1 at least).

    @param constraint	primitive constraint
    @return		reference to time variable (null if invalid constraint)
  */
  private IntVar timeVar(PrimitiveConstraint constraint) {
    IntVar var = null;
    try {
      Class constraintClass = constraint.getClass();
      Field constraintVar = constraintClass.getField("x");
      var = (IntVar) (constraintVar.get(constraint));
      var = timeVar(var);
    }
    catch (Exception exception) {
      // ignored
    }
    if (var == null)
      System.err.println(
	"timeVar: constraint '" + constraint + "' must be a comparison");
    return(var);
  }

  /**
    Return the time variable associated with the given variable (which may be a
    main, time or value variable).

    @param variable	variable name
    @return		reference to time variable
  */
  private IntVar timeVar(IntVar variable) {
    String varName = variable.id();
    if (varName.endsWith(TIME_SUFFIX))
      varName = varName.substring(0, varName.length() - TIME_SUFFIX.length());
    else if (varName.endsWith(VALUE_SUFFIX))
      varName = varName.substring(0, varName.length() - VALUE_SUFFIX.length());
    Var var = store.findVariable(varName + TIME_SUFFIX);
    if (var != null && var instanceof IntVar)
      variable = (IntVar) var;
    else
      System.err.println(
	"timeVar: time variable for '" + varName + "' missing");
    return(variable);
  }

  /**
    Return the value variable corresponding to the given main variable.

    @param variable	variable name
    @return		reference to value variable
  */
  private IntVar valueVar(IntVar variable) {
    String varName = variable.id();
    Var var = store.findVariable(varName + VALUE_SUFFIX);
    if (var != null && var instanceof IntVar)
      variable = (IntVar) var;
    else
      System.err.println(
	"valueVar: value variable for '" + varName + "' missing");
    return(variable);
  }

  /**
    Return the "and" of the given primitive constraints.

    @param constraints	two or more primitive constraints
    @return		"and" of the given primitive constraints
  */
  private PrimitiveConstraint and(PrimitiveConstraint... constraints) {
    return(new And(constraints));
  }

  /**
    Return the negated given primitive constraint.

    @param constraint	primitive constraint
    @return		negated given primitive constraint
  */
  private PrimitiveConstraint not(PrimitiveConstraint constraint) {
    return(new Not(constraint));
  }

  /**
    Return the "or" of the given primitive constraints.

    @param constraints	two or more primitive constraints
    @return		"or" of the given primitive constraints
  */
  private PrimitiveConstraint or(PrimitiveConstraint... constraints) {
    return(new Or(constraints));
  }

  /**
    Return a primitive constraint that says "variable = constant".

    @param variable	integer variable
    @param constant	integer constant
    @return		primitive constraint says "variable = constant"
  */
  private PrimitiveConstraint eq(IntVar variable, int constant) {
    return(new XeqC(valueVar(variable), constant));
  }

  private PrimitiveConstraint eq(IntVar variable1, IntVar variable2) {
    return(new XeqY(valueVar(variable1), valueVar(variable2)));
  }

  /**
    Return a primitive constraint that says "variable >= constant".

    @param variable	integer variable
    @param constant	integer constant
    @return		primitive constraint says "variable >= constant"
  */
  private PrimitiveConstraint ge(IntVar variable, int constant) {
    return(new XgteqC(valueVar(variable), constant));
  }

  /**
    Return a primitive constraint "variable1 >= variable2".

    @param variable1	integer variable
    @param variable2	integer variable
    @return		primitive constraint "variable1 >= variable2"
  */
  private PrimitiveConstraint ge(IntVar variable1, IntVar variable2) {
    return(new XgteqY(valueVar(variable1), valueVar(variable2)));
  }

  /**
    Return a primitive constraint "variable > constant".

    @param variable	integer variable
    @param constant	integer constant
    @return		primitive constraint "variable > constant"
  */
  private PrimitiveConstraint gt(IntVar variable, int constant) {
    return(new XgtC(valueVar(variable), constant));
  }

  /**
    Return a primitive constraint "variable1 > variable2".

    @param variable1	integer variable
    @param variable2	integer variable
    @return		primitive constraint "variable1 > variable2"
  */
  private PrimitiveConstraint gt(IntVar variable1, IntVar variable2) {
    return(new XgtY(valueVar(variable1), valueVar(variable2)));
  }

  /**
    Return a primitive constraint "variable <= constant".

    @param variable	integer variable
    @param constant	integer constant
    @return		primitive constraint "variable <= constant"
  */
  private PrimitiveConstraint le(IntVar variable, int constant) {
    return(new XlteqC(valueVar(variable), constant));
  }

  /**
    Return a primitive constraint "variable1 <= variable2".

    @param variable1	integer variable
    @param variable2	integer variable
    @return		primitive constraint "variable1 <= variable2"
  */
  private PrimitiveConstraint le(IntVar variable1, IntVar variable2) {
    return(new XlteqY(valueVar(variable1), valueVar(variable2)));
  }

  /**
    Return a primitive constraint "variable < constant".

    @param variable	integer variable
    @param constant	integer constant
    @return		primitive constraint "variable < constant"
  */
  private PrimitiveConstraint lt(IntVar variable, int constant) {
    return(new XltC(valueVar(variable), constant));
  }

  /**
    Return a primitive constraint "variable1 < variable2".

    @param variable1	integer variable
    @param variable2	integer variable
    @return		primitive constraint "variable1 < variable2"
  */
  private PrimitiveConstraint lt(IntVar variable1, IntVar variable2) {
    return(new XltY(valueVar(variable1), valueVar(variable2)));
  }

  /**
    Return a primitive constraint "variable != constant".

    @param variable	integer variable
    @param constant	integer constant
    @return		primitive constraint "variable != constant"
  */
  private PrimitiveConstraint ne(IntVar variable, int constant) {
    return(new XneqC(valueVar(variable), constant));
  }

  /**
    Return a primitive constraint "variable1 != variable2".

    @param variable1	integer variable
    @param variable2	integer variable
    @return		primitive constraint "variable1 != variable2"
  */
  private PrimitiveConstraint ne(IntVar variable1, IntVar variable2) {
    return(new XneqY(valueVar(variable1), valueVar(variable2)));
  }

  /**
    Return a primitive constraint "minimum <= variable <= maximum".

    @param variable	integer variable
    @param minimum	minimum integer constant
    @param maximum	maximum integer constant
    @return		primitive constraint "minimum <= variable <= maximum"
  */
  private PrimitiveConstraint range(IntVar variable, int minimum, int maximum) {
    if (minimum > maximum)
    System.err.println("range: invalid range '" + minimum + ".." + maximum +
	"' for '" + variable + "'");
    return(and(ge(variable, minimum), le(variable, maximum)));
  }

  /**
    Return a primitive constraint requiring "time0 < time1 < ..." where these
    values refer to the time fields of the variables. Primitive constraints are
    also incorporated into the constraint list.

    @param event	two or more events (variables or comparisons)
    @return		primitive constraints plus "time0 < time1 < ..."
  */
  private PrimitiveConstraint then(Object... event) {
    return(new And(thenAux(event)));
  }

  /**
    Return a list of primitive constraint requiring "time0 < time1 < ..." where
    these values refer to the time fields of the variables. Primitive
    constraints are also incorporated into the constraint list.

    @param event	two or more events (variables or comparisons)
    @return		primitive constraint "time0 < time1 < ..."
			(null if error)
  */
  private ArrayList<PrimitiveConstraint> thenAux(Object... event) {
    ArrayList<PrimitiveConstraint> constraints =// initialise constraints
      new ArrayList<PrimitiveConstraint>();
    int length = event.length;			// get number of events
    if (length == 0)				// no events?
      System.err.println("then: cannot have an empty event list");
    else if (length == 1)			// one event?
      System.err.println(
	"then: cannot have just one event '" + event[0] + "'");
    else {
      Object event0 = event[0];			// get first event
      IntVar varTime0 = null;			// initialise its time variable
      if (event0 instanceof IntVar) {		// integer variable?
	varTime0 = timeVar((IntVar) event0);	// get integer variable
      }
      else if (event0 instanceof		// primitive constraint?
	       PrimitiveConstraint) {
	PrimitiveConstraint constraint0 =	// get constraint
	  (PrimitiveConstraint) event0;
	varTime0 = timeVar(constraint0);	// get constraint variable
	constraints.add(constraint0);		// add first value constraint
      }
      else					// not variable/constraint
	System.err.println(
	  "then: event '" + event0 + "' must be a variable or a comparison");

      Object event1 = event[1];			// get second event
      IntVar varTime1 = null;			// initialise its time variable
      if (event1 instanceof IntVar) {		// integer variable?
	varTime1 = timeVar((IntVar) event1);	// get integer variable
      }
      else if (event1 instanceof		// primitive constraint?
	       PrimitiveConstraint) {
	PrimitiveConstraint constraint1 =	// get constraint
	  (PrimitiveConstraint) event1;
	varTime1 = timeVar(constraint1);	// get constraint variable
	if (length == 2)			// last constraint?
	  constraints.add(constraint1);		// add second value constraint
      }
      else					// not variable/constraint
	System.err.println(
	  "then: event '" + event1 + "' must be a variable or a comparison");

      if (varTime0 != null && varTime1 != null) { // time variables found?
	PrimitiveConstraint timeConstraint =	// set time constraint
	  new XltY(varTime0, varTime1);
	constraints.add(timeConstraint);	// add time constraint
	if (length > 2) {			// more constraints?
	  Object[] eventRest = Arrays.copyOfRange(event, 1, length);
	  constraints.addAll(thenAux(eventRest));
	}
      }
    }
    return(constraints);
  }

}

