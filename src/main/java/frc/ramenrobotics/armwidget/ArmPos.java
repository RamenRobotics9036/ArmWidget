package frc.ramenrobotics.armwidget;

import edu.wpi.first.shuffleboard.api.data.ComplexData;
import java.util.Map;

/**
 * Represents the currnet position of the Robot Arm.
 */
public final class ArmPos extends ComplexData<ArmPos> {

  private final double m_percentRaised;
  private final double m_percentExtended;
  private final boolean m_isClawOpen;

  /**
   * Constructor.
   */
  public ArmPos(double percentRaised, double percentExtended, boolean isClawOpen) {
    this.m_percentRaised = percentRaised;
    this.m_percentExtended = percentExtended;
    this.m_isClawOpen = isClawOpen;
  }

  public double getPercentRaised() {
    return ImageUtilities.clamp(m_percentRaised, 0, 1);
  }

  public double getPercentExtended() {
    return ImageUtilities.clamp(m_percentExtended, 0, 1);
  }

  public boolean getIsClawOpen() {
    return m_isClawOpen;
  }

  @Override
  public String toHumanReadableString() {
    return Double.toString(Math.round(m_percentRaised * 100))
        + "% raised, "
        + Double.toString(Math.round(m_percentExtended * 100))
        + "% extended, "
        + (m_isClawOpen ? "open" : "closed");
  }

  @Override
  public Map<String, Object> asMap() {
    return Map.of("percentRaised",
        m_percentRaised,
        "percentExtended",
        m_percentExtended,
        "isClawOpen",
        m_isClawOpen);
  }
}
