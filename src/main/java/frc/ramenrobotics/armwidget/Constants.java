package frc.ramenrobotics.armwidget;

/**
 * Constants for the robot arm widget.
 */
public class Constants {
  public static final double m_scaleImages = 0.25;

  // Name of the widget to load
  public static final String kAnimatedArmWidget = "AnimatedArm";

  // Claw and extender constants
  public static final double kOpenClawMarginPixels = 4;
  public static final double kClosedClawMarginPixels = 0;
  public static final double kFullyExtendedTrimRightPixels = 0;
  public static final double kFullyRetractedTrimRightPixles = 200;

  // Robot base constants
  public static final double kArmPivotCenterX = 92;
  public static final double kArmPivotCenterY = 96;
  public static final double kArmPivotCenterToExtender = 120;

  // Arm rotation constants
  public static final double kArmDisplayRotationLowest = 50;
  public static final double kArmDisplayRotationHighest = -10;
}
