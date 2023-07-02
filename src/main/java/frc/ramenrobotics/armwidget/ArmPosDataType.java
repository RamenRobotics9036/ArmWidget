package frc.ramenrobotics.armwidget;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents data of the {@link ArmPos} type.
 */
public final class ArmPosDataType extends ComplexDataType<ArmPos> {

  /**
   * The name of data of this type as it would appear in a WPILib sendable's
   * {@code .type} entry; a differential drive base a {@code .type} of
   * "DifferentialDrive", a sendable chooser has it set to "String Chooser"; a
   * hypothetical 2D point would have it set to "Point2D".
   */
  private static final String TYPE_NAME = Constants.kAnimatedArmWidget;

  /**
   * The single instance of the point type. By convention, this is a
   * {@code public static final} field and the constructor is private to ensure
   * only a single instance of the data type exists.
   */
  public static final ArmPosDataType Instance = new ArmPosDataType();

  private ArmPosDataType() {
    super(TYPE_NAME, ArmPos.class);
  }

  @Override
  public Function<Map<String, Object>, ArmPos> fromMap() {
    return map -> new ArmPos(
        (double) map.getOrDefault("percentRaised", 0.0),
        (double) map.getOrDefault("percentExtended", 0.0),
        (boolean) map.getOrDefault("isClawOpen", false));
  }

  @Override
  public ArmPos getDefaultValue() {
    return new ArmPos(0.0, 0.0, false);
  }
}
