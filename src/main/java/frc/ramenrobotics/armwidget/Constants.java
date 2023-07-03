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

  // Animation times
  public static final double ksecondsToFullyExtend = 1;
  public static final double ksecondsToFullyRaise = 1;

  // Frames per second animation - Stick to 30 or 60
  public static int kframesPerSecond = 60;
}
