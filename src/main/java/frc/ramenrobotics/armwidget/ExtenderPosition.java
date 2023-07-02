package frc.ramenrobotics.armwidget;

/**
 * Describes current position of extender and claw.
 */
public class ExtenderPosition {
  public double m_extendedPercent;
  public boolean m_isClawOpen;

  /**
   * Constructor.
   */
  public ExtenderPosition(double extendedPercent, boolean isClawOpen) {
    extendedPercent = ImageUtilities.clamp(extendedPercent, 0, 1);

    m_extendedPercent = extendedPercent;
    m_isClawOpen = isClawOpen;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ExtenderPosition)) {
      return false;
    }
    ExtenderPosition other = (ExtenderPosition) obj;
    return Double.compare(
        m_extendedPercent, other.m_extendedPercent) == 0
        && m_isClawOpen == other.m_isClawOpen;
  }
}
