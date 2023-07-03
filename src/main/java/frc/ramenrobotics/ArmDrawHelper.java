package frc.ramenrobotics;

import frc.ramenrobotics.armwidget.ArmPosition;
import frc.ramenrobotics.armwidget.Constants;
import frc.ramenrobotics.armwidget.ExtenderPosition;
import frc.ramenrobotics.armwidget.RobotArmRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * The actual drawing of the arm onto the canvas happens in this class.
 */
public class ArmDrawHelper {
  private Canvas m_canvas;
  private Canvas m_smallCanvas;
  private Boolean m_isDirty;
  ExtenderPosition m_currentExtenderPos = new ExtenderPosition(0, false);
  ExtenderPosition m_targetExtenderPos = new ExtenderPosition(0, false);
  ArmPosition m_currentArmPos = new ArmPosition(0);
  ArmPosition m_targetArmPos = new ArmPosition(0);
  private RobotArmRenderer m_robotArmRenderer = new RobotArmRenderer();

  /**
   * Constructor.
   */
  public ArmDrawHelper(Canvas canvas, Canvas smallCanvas) {
    m_canvas = canvas;
    m_smallCanvas = smallCanvas;
    m_isDirty = true;
  }

  public boolean doesCanvasFitRobotArm() {
    return m_smallCanvas.getWidth() <= m_canvas.getWidth()
        && m_smallCanvas.getHeight() <= m_canvas.getHeight();
  }

  public void markDirty() {
    m_isDirty = true;
  }

  private void drawMessageTooSmall() {
    GraphicsContext gc = m_canvas.getGraphicsContext2D();
    gc.clearRect(0, 0, m_canvas.getWidth(), m_canvas.getHeight());

    GraphicsContext gcSmall = m_smallCanvas.getGraphicsContext2D();
    gcSmall.clearRect(0, 0, m_smallCanvas.getWidth(), m_smallCanvas.getHeight());

    gc.setFill(Color.LIGHTGRAY);
    gc.fillRect(0, 0, m_canvas.getWidth(), m_canvas.getHeight());

    gc.setFill(Color.BLACK);
    gc.fillText("Window is too small", 10, 20);
  }

  private double moveTowardsTarget(
      double currentPosition,
      double targetPosition,
      double timeInSeconds) {

    // 1.0 for full range (0 to 1) over the given time with 30 calls per second
    double speed = 1.0 / (timeInSeconds * Constants.kframesPerSecond);
    if (currentPosition < targetPosition) {
      currentPosition = Math.min(currentPosition + speed, targetPosition);
    }
    else if (currentPosition > targetPosition) {
      currentPosition = Math.max(currentPosition - speed, targetPosition);
    }

    return currentPosition;
  }

  private void moveTowardsTarget() {
    // We pro-actively mark this frame as dirty if current and target positions
    // differ, so we can smoothly animate towards target.
    if (!m_currentExtenderPos.equals(m_targetExtenderPos)
        || !m_currentArmPos.equals(m_targetArmPos)) {

      m_currentExtenderPos.m_extendedPercent = moveTowardsTarget(
          m_currentExtenderPos.m_extendedPercent,
          m_targetExtenderPos.m_extendedPercent,
          Constants.ksecondsToFullyExtend);

      m_currentArmPos.m_raisedPercent = moveTowardsTarget(
          m_currentArmPos.m_raisedPercent,
          m_targetArmPos.m_raisedPercent,
          Constants.ksecondsToFullyRaise);

      m_isDirty = true;
    }
  }

  /**
   * Redraw the arm on the canvas.
   */
  public void redraw(ExtenderPosition extenderPosition, ArmPosition armPosition) {
    m_targetExtenderPos = extenderPosition.copy();
    m_targetArmPos = armPosition.copy();

    // Make sure we always keep isClawOpen identical betwween current and target extender position.
    // If we don't than currentExtender will randomly turn out to not equal targetExtender, since
    // isClawOpen values aren't kept identical.
    m_currentExtenderPos.m_isClawOpen = m_targetExtenderPos.m_isClawOpen;

    moveTowardsTarget();

    if (m_isDirty
        && m_canvas.getWidth() != 0 && m_canvas.getHeight() != 0) {

      if (doesCanvasFitRobotArm()) {

        // Clear the full canvas (tile)
        GraphicsContext gc = m_canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, m_canvas.getWidth(), m_canvas.getHeight());

        Image armImage = m_robotArmRenderer.getArmImage(
            m_currentArmPos,
            m_currentExtenderPos);

        GraphicsContext gcSmall = m_smallCanvas.getGraphicsContext2D();
        gcSmall.clearRect(0, 0, m_smallCanvas.getWidth(), m_smallCanvas.getHeight());

        double imageX = 0;
        double imageY = m_smallCanvas.getHeight() - armImage.getHeight();

        gcSmall.drawImage(armImage, imageX, imageY);
      }
      else {
        drawMessageTooSmall();
      }

      m_isDirty = false;
    }
  }
}
