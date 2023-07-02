package frc.ramenrobotics.armwidget;

/**
 * Describes current rotation of Arm.
 */
public class ArmPosition {
  public double m_raisedPercent;

  /**
   * Constructor.
   */
  public ArmPosition(double raisedPercent) {
    raisedPercent = ImageUtilities.clamp(raisedPercent, 0, 1);

    m_raisedPercent = raisedPercent;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ArmPosition)) {
      return false;
    }
    ArmPosition other = (ArmPosition) obj;
    return Double.compare(
        m_raisedPercent, other.m_raisedPercent) == 0;
  }
}
