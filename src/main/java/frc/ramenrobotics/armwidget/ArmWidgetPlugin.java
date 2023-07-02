package frc.ramenrobotics.armwidget;

import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;
import java.util.List;
import java.util.Map;

/**
 * A plugin for displaying Arm Position.
 */
@Description(group = "frc.ramenrobotics", name = "ArmWidget", version = "1.0.0", summary = "Displays a simple Robot Arm.")
public final class ArmWidgetPlugin extends Plugin {

  @Override
  public List<DataType> getDataTypes() {
    return List.of(ArmPosDataType.Instance);
  }

  @Override
  public List<ComponentType> getComponents() {
    return List.of(WidgetType.forAnnotatedWidget(ArmPosWidget.class));
  }

  @Override
  public Map<DataType, ComponentType> getDefaultComponents() {
    return Map.of(ArmPosDataType.Instance, WidgetType.forAnnotatedWidget(ArmPosWidget.class));
  }
}
