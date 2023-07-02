package frc.ramenrobotics.armwidget;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Constructs image of Extender and Claw, into a single
 * image.
 */
public class ExtenderAndClaw {  
  private final Image m_armExtenderImage = new Image(getClass().getResourceAsStream(
      "img/ArmExtender.png"));
  private final Image m_armClawClosedImage = new Image(getClass().getResourceAsStream(
      "img/ArmClawClosed.png"));
  private final Image m_armClawOpenedImage = new Image(getClass().getResourceAsStream(
      "img/ArmClawOpen.png"));

  private Group m_cachedGroup;
  private ExtenderPosition m_lastPosition;

  /**
   * Constructor.
   */
  public ExtenderAndClaw() {
    m_cachedGroup = null;
    m_lastPosition = null;
  }

  /**
   * This actually renders the image of extender plus claw on each call.
   */
  private Group getExtenderAndClawImageHelper(ExtenderPosition position) {
    Image clawToUse = position.m_isClawOpen
        ? m_armClawOpenedImage :
        m_armClawClosedImage;

    // Show full image if 100% extended.  Show cutoff image if 0% extended.
    double trimExtenderRightPixels = ImageUtilities.linearInterpolation(
        position.m_extendedPercent,
        Constants.kFullyRetractedTrimRightPixles,
        Constants.kFullyExtendedTrimRightPixels);

    // The open claw needs to be shifted left a few pixels, to avoid gap between it
    // and the exxtender.
    double pxLeftMarginClaw = position.m_isClawOpen
        ? Constants.kOpenClawMarginPixels
        : Constants.kClosedClawMarginPixels;

    // We shrink the extender image to "retract" the arm
    Rectangle2D extenderRect = new Rectangle2D(
        0,
        0,
        m_armExtenderImage.getWidth() - trimExtenderRightPixels,
        m_armExtenderImage.getHeight());

    ImageView extenderView = ImageUtilities.getScaledViewOfImage(m_armExtenderImage, extenderRect);
    ImageView clawView =  ImageUtilities.getScaledViewOfImage(clawToUse, null);

    double groupHeight = Math.max(
        m_armExtenderImage.getHeight(),
        clawToUse.getHeight());

    // Combine image views
    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    Group group = new Group(extenderView, clawView);

    clawView.setLayoutX(
          ((m_armExtenderImage.getWidth() - pxLeftMarginClaw) - trimExtenderRightPixels)
           * Constants.m_scaleImages);

    clawView.setLayoutY(
          (groupHeight - clawToUse.getHeight())
          * Constants.m_scaleImages / 2);

    extenderView.setLayoutY(
          (groupHeight - m_armExtenderImage.getHeight())
          * Constants.m_scaleImages / 2);

    return group;
  }

  private boolean isCachedGroupReusable(ExtenderPosition newPosition) {
    return m_cachedGroup != null
        && m_lastPosition != null
        && m_lastPosition.equals(newPosition);    
  }

  /**
   * Returns an image of the extender and claw.
   */
  public Group getExtenderAndClawGroup(ExtenderPosition position) {
    Group resultGroup = null;

    if (isCachedGroupReusable(position)) {
      resultGroup = m_cachedGroup;
    }
    else {
      resultGroup = getExtenderAndClawImageHelper(position);
      m_cachedGroup = resultGroup;
      m_lastPosition = position;
    }
  
    return resultGroup;
  }
}
