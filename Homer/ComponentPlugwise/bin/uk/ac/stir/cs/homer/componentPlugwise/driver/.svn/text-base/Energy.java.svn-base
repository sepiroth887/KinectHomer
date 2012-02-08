package uk.ac.stir.cs.homer.componentPlugwise.driver;

/**

  Energy consumption reading for Circle (hour, energy).

  @author	Kenneth J. Turner
  @version	1.0: 15/05/11 (KJT)
*/
public class Energy {

  /** Circle energy consumption (watt-hours) */
  private int reading;

  /** Circle reading time */
  private String time;

 /**
    Constructor for a Energy object.

    @param time		time
    @param reading	energy reading
  */
  public Energy(String time, int reading) {
    this.time = time;
    this.reading = reading;
  }

  /**
    Compares current object to the given object.

    @param object	object to be compared
    @return		true if equal
  */
  public boolean equals(Object object) {
    boolean result = false;
    if (object != null && object.getClass().equals(this.getClass())) {
      Energy energy = (Energy) object;
      result =
	energy.getEnergy() == reading &&
	energy.getTime() == time;
    }
    return(result);
  }

  /**
    Return energy (watt-hours).

    @return	energy (watt-hours)
  */
  public int getEnergy() {
    return(reading);
  }

  /**
    Return time.

    @return	time
  */
  public String getTime() {
    return(time);
  }

}

