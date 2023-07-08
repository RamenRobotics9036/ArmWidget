package frc.ramenrobotics.armwidget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import frc.ramenrobotics.ArmDrawHelper;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 * This Widget shows a robot arm.
 */
@Description(name = Constants.kAnimatedArmWidget,
    dataTypes = ArmPos.class,
    summary = "Displays a robot arm.")
@ParametrizedController("ArmPosWidget.fxml")
public final class ArmPosWidget extends SimpleAnnotatedWidget<ArmPos> {
  private double m_tileWidth;
  private double m_tileHeight;
  private ChangeListener<Number> m_sizeListener = null;
  private ChangeListener<Object> m_dataChangeListener = null;
  private AnimationTimer m_timer = null;
  private ArmDrawHelper m_armDrawHelper;

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
    // Create listeners
    m_sizeListener = (observable, oldValue, newValue) -> resizeCanvas();
    m_dataChangeListener = (observable, oldValue, newValue) -> markDirty();
  
    canvas.widthProperty().bind(root.widthProperty());
    canvas.heightProperty().bind(root.heightProperty());

    // Redraw when size changes
    canvas.widthProperty().addListener(m_sizeListener);
    canvas.heightProperty().addListener(m_sizeListener);

    // Mark Canvas as dirty on data change
    dataProperty().addListener(m_dataChangeListener);

    m_armDrawHelper = new ArmDrawHelper(canvas, smallCanvas);

    markDirty();
    startAnimationTimer();

    // Check that frame-rate is valid
    if (Constants.kframesPerSecond != 30
        && Constants.kframesPerSecond != 60) {

      throw new RuntimeException("Frame count should be 30 or 60");
    }
  }

  private void startAnimationTimer() {
    m_timer = new AnimationTimer() {
      private long m_frameCount = 0;
  
      @Override
      public void handle(long now) {
        m_frameCount++;
    
        onAnimationTimer(m_frameCount);
      }
    };
   
    m_timer.start();
  }

  private void onAnimationTimer(long frameCount) {
    int everyNumFrames = 60 / Constants.kframesPerSecond;

    // Perform action every at 30fps or 60fps
    if (frameCount % everyNumFrames == 0) {
      redraw();
    }
  }

  private void markDirty() {
    m_armDrawHelper.markDirty();
  }

  // Only render arm if the canvas is visible.  Otherwise, we waste CPU cycles since there
  // are multiple instances of the widget loaded, some of which arent visible.
  private boolean isCanvasVisible() {
    Scene scene = root.getScene();

    if (scene == null
        || scene.getWindow() == null
        || !scene.getWindow().isShowing()) {

      return false;
    }

    if (canvas == null
        || !canvas.isVisible()
        || canvas.getWidth() == 0) {

      return false;
    }

    return true;
  }

  private void redraw() {
    if (!isCanvasVisible()) {
      return;
    }

    // Get arm position from Network Tables
    ArmPos armData = getData();
    ExtenderPosition extenderPosition = new ExtenderPosition(
        armData.getPercentExtended(),
        armData.getIsClawOpen());
    ArmPosition armPosition = new ArmPosition(
        armData.getPercentRaised());

    m_armDrawHelper.redraw(extenderPosition, armPosition);
  }

  private void resizeCanvas() {
    // During a resize operation, we force redraw always, for smooth animation
    markDirty();

    m_tileWidth = root.getWidth();
    m_tileHeight = root.getHeight();

    if (m_armDrawHelper.doesCanvasFitRobotArm()) {
      smallCanvas.setLayoutX(m_tileWidth / 2.0 - smallCanvas.getWidth() / 2.0);
      smallCanvas.setLayoutY(m_tileHeight / 2.0 - smallCanvas.getHeight() / 2.0);
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
