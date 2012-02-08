package uk.ac.stir.cs.homer.componentPlugwise.driver;

/**
  Calibration for Circle (gainA, gainB, offsetTotal, offsetNoise).

  @author	Kenneth J. Turner
  @version	1.0: 15/05/11 (KJT)
*/
public class Calibration {

  /** Circle gain A */
  private float gainA;

  /** Circle gain B */
  private float gainB;

  /** Circle noise offset */
  private float offsetNoise;

  /** Circle total offset */
  private float offsetTotal;

  /**
    Constructor for a Calibration object.

    @param gainA	gain A
    @param gainB	gain B
    @param offsetNoise	noise offset
    @param offsetTotal	total offset
  */
  public Calibration(float gainA, float gainB, float offsetNoise,
   float offsetTotal) {
    this.gainA = gainA;
    this.gainB = gainB;
    this.offsetNoise = offsetNoise;
    this.offsetTotal = offsetTotal;
  }

  /**
    Compares current object to the given object.

    @param object	object to be compared
    @return		true if equal
  */
  public boolean equals(Object object) {
    boolean result = false;
    if (object != null && object.getClass().equals(this.getClass())) {
      Calibration calibration = (Calibration) object;
      result =
	calibration.getGainA() == gainA &&
	calibration.getGainB() == gainB &&
	calibration.getOffsetNoise() == offsetNoise &&
	calibration.getOffsetTotal() == offsetTotal;
    }
    return(result);
  }

  /**
    Return gain A.

    @return	gain A
  */
  public float getGainA() {
    return(gainA);
  }

  /**
    Return gain B.

    @return	gain B
  */
  public float getGainB() {
    return(gainB);
  }

  /**
    Return noise offset.

    @return	noise offset
  */
  public float getOffsetNoise() {
    return(offsetNoise);
  }

  /**
    Return total offset.

    @return	total offset
  */
  public float getOffsetTotal() {
    return(offsetTotal);
  }

}

