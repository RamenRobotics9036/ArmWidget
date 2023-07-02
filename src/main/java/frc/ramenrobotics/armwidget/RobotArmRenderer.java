package frc.ramenrobotics.armwidget;

import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Draws entire Robot arm.
 */
public class RobotArmRenderer {
  private final Image m_robotBaseImage = new Image(getClass().getResourceAsStream(
      "img/ArmTallerBase.png"));

  private ExtenderAndClaw m_extenderAndClaw = new ExtenderAndClaw();
  private Image m_cachedImage;
  private ArmPosition m_lastArmPosition;
  private ExtenderPosition m_lastExtenderPosition;

  /**
   * Constructor.
   */
  public RobotArmRenderer() {
    m_cachedImage = null;
    m_lastArmPosition = null;
    m_lastExtenderPosition = null;
  }

  private boolean isCachedImageReusable(
      ArmPosition newArmPosition,
      ExtenderPosition newExtenderPosition) {

    return m_cachedImage != null
        && m_lastArmPosition != null
        && m_lastExtenderPosition != null
        && m_lastArmPosition.equals(newArmPosition)
        && m_lastExtenderPosition.equals(newExtenderPosition);
  }

  private Image redrawRobotArm(
      ArmPosition armPosition,
      ExtenderPosition extenderPosition) {

    Group extenderAndClawGroup = m_extenderAndClaw.getExtenderAndClawGroup(extenderPosition);

    ImageView robotBaseImageView =  ImageUtilities.getScaledViewOfImage(m_robotBaseImage, null);
  
    // Create a new Pane to store the images
    Pane pane = new Pane();
  
    // Add the ImageView and extender and claw group to the Pane
    pane.getChildren().addAll(robotBaseImageView, extenderAndClawGroup);

    // Set the coordinates for the ImageView and extender and claw group
    robotBaseImageView.setLayoutX(0);
    robotBaseImageView.setLayoutY(0);

    double pivotCenterX = Constants.kArmPivotCenterX * Constants.m_scaleImages;
    double pivotCenterY = Constants.kArmPivotCenterY * Constants.m_scaleImages;
    double pivotCenterToExtender = Constants.kArmPivotCenterToExtender * Constants.m_scaleImages;

    // Remove any previous rotation transformation (since this may be a cached image)
    // However, make sure to ONLY remove rotation transformations, and leave all
    // other transformations (such as scaling) in place.
    // Also, note that we remove the rotation BEFORE setting LayoutX and LayoutY,
    // to avoid the rotation affecting the coordinates.
    extenderAndClawGroup.getTransforms().removeIf(
        transform -> transform instanceof Rotate);

    extenderAndClawGroup.setLayoutX(
        pivotCenterX
        + pivotCenterToExtender);

    extenderAndClawGroup.setLayoutY(
        pivotCenterY
        - (extenderAndClawGroup.getBoundsInParent().getHeight() / 2));

    // Figure out how much to rotate the displayed arm
    double degreesToRotate = ImageUtilities.linearInterpolation(
        armPosition.m_raisedPercent,
        Constants.kArmDisplayRotationLowest,
        Constants.kArmDisplayRotationHighest);

    // Apply rotation to extender
    Rotate rotate = new Rotate(
        degreesToRotate,
        -1 * (Constants.kArmPivotCenterToExtender * Constants.m_scaleImages),
        extenderAndClawGroup.getBoundsInParent().getHeight() / 2);
    extenderAndClawGroup.getTransforms().add(rotate);

    SnapshotParameters parameters = new SnapshotParameters();
    parameters.setFill(Color.TRANSPARENT);
    return pane.snapshot(parameters, null);
  }

  /**
   * Returns image of robot arm, based on current arm and extender positions.
   */
  public Image getArmImage(
      ArmPosition armPosition,
      ExtenderPosition extenderPosition) {

    Image resultImage = null;

    if (isCachedImageReusable(armPosition, extenderPosition)) {
      resultImage = m_cachedImage;
    }
    else {
      resultImage = redrawRobotArm(armPosition, extenderPosition);
      m_cachedImage = resultImage;
      m_lastArmPosition = armPosition;
      m_lastExtenderPosition = extenderPosition;
    }

    return resultImage;
  }
}
