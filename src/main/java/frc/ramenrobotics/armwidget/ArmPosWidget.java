package frc.ramenrobotics.armwidget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This Widget shows a robot arm.
 */
@Description(name = Constants.kAnimatedArmWidget, dataTypes = ArmPos.class, summary = "Displays a robot arm.")
@ParametrizedController("ArmPosWidget.fxml")
public final class ArmPosWidget extends SimpleAnnotatedWidget<ArmPos> {
  private double m_tileWidth;
  private double m_tileHeight;
  private double m_smallCanvasWidth;
  private double m_smallCanvasHeight;
  private RobotArmRenderer m_robotArmRenderer = new RobotArmRenderer();

  @FXML
  @SuppressWarnings("checkstyle:MemberNameCheck")
  private Pane root;

  @FXML
  @SuppressWarnings("checkstyle:MemberNameCheck")
  private Canvas canvas;

  @FXML
  @SuppressWarnings("checkstyle:MemberNameCheck")
  private Canvas smallCanvas;

  @FXML
  private void initialize() {
    // Save the current size of smallCanvas, which is set in the FXML file and never changes
    m_smallCanvasWidth = smallCanvas.getWidth();
    m_smallCanvasHeight = smallCanvas.getHeight();

    canvas.widthProperty().bind(root.widthProperty());
    canvas.heightProperty().bind(root.heightProperty());

    // Redraw when size changes
    canvas.widthProperty().addListener(evt -> resizeCanvas());
    canvas.heightProperty().addListener(evt -> resizeCanvas());

    // Redraw on data change
    dataProperty().addListener((newValue) -> redraw());
  }

  private void drawMessageTooSmall() {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.clearRect(0, 0, m_tileWidth, m_tileHeight);

    GraphicsContext gcSmall = smallCanvas.getGraphicsContext2D();
    gcSmall.clearRect(0, 0, m_smallCanvasWidth, m_smallCanvasHeight);

    gc.setFill(Color.LIGHTGRAY);
    gc.fillRect(0, 0, m_tileWidth, m_tileHeight);

    gc.setFill(Color.BLACK);
    gc.fillText("Window is too small", 10, 20);
  }

  private void redraw() {
    if (m_tileWidth != 0 && m_tileHeight != 0) {

      if (doesCanvasFitRobotArm()) {

        // Clear the full canvas (tile)
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, m_tileWidth, m_tileHeight);

        /*
        gc.setFill(Color.AQUA);
        gc.fillRect(0, 0, m_tileWidth, m_tileHeight);
        */

        // Get arm position from Network Tables
        ArmPos armData = getData();
        ExtenderPosition extenderPosition = new ExtenderPosition(
            armData.getPercentExtended(),
            armData.getIsClawOpen());
        ArmPosition armPosition = new ArmPosition(
            armData.getPercentRaised());

        Image armImage = m_robotArmRenderer.getArmImage(
            armPosition,
            extenderPosition);

        GraphicsContext gcSmall = smallCanvas.getGraphicsContext2D();
        gcSmall.clearRect(0, 0, m_smallCanvasWidth, m_smallCanvasHeight);

        /*
        gcSmall.setFill(Color.ROYALBLUE);
        gcSmall.fillRect(0, 0, m_smallCanvasWidth, m_smallCanvasHeight);
        */

        double imageX = 0;
        double imageY = m_smallCanvasHeight - armImage.getHeight();

        gcSmall.drawImage(armImage, imageX, imageY);
      }
      else {
        drawMessageTooSmall();
      }
    }
  }

  private boolean doesCanvasFitRobotArm() {
    return m_smallCanvasWidth <= m_tileWidth
        && m_smallCanvasHeight <= m_tileHeight;
  }

  private void resizeCanvas() {
    m_tileWidth = root.getWidth();
    m_tileHeight = root.getHeight();

    if (doesCanvasFitRobotArm()) {
      smallCanvas.setLayoutX(m_tileWidth / 2.0 - m_smallCanvasWidth / 2.0);
      smallCanvas.setLayoutY(m_tileHeight / 2.0 - m_smallCanvasHeight / 2.0);
    }
    else {
      // Too small to draw the Robot Arm
      smallCanvas.setLayoutX(0);
      smallCanvas.setLayoutY(0);
    }

    redraw();
  }

  @Override
  public Pane getView() {
    return root;
  }
}

