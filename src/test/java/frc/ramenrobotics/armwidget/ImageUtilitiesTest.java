package frc.ramenrobotics.armwidget;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ImageUtilities.
 */
public class ImageUtilitiesTest {
  @BeforeEach
  public void setUp() {
  }

  @Test
  public void positiveInterpolationPercent0ReturnsStartValue() {
    double start = 100;
    double end = 200;
    double percent = 0;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == start);
  }

  @Test
  public void positiveInterpolationPercent50ReturnsMidValue() {
    double start = 100;
    double end = 200;
    double percent = 0.5;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == 150);
  }

  @Test
  public void positiveInterpolationPercent100ReturnsEndValue() {
    double start = 100;
    double end = 200;
    double percent = 1;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == end);
  }

  @Test
  public void positiveOutOfBoundsPercentClampsToStart() {
    double start = 100;
    double end = 200;
    double percent = -1;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == start);
  }

  @Test
  public void positiveOutOfBoundsPercentCase2ClampsToEnd() {
    double start = 100;
    double end = 200;
    double percent = 2;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == end);
  }

  @Test
  public void negativeInterpolationPercent0ReturnsStartValue() {
    double start = 100;
    double end = -100;
    double percent = 0;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == start);
  }

  @Test
  public void negativeInterpolationPercent50ReturnsMidValue() {
    double start = 100;
    double end = -100;
    double percent = 0.5;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == 0);
  }

  @Test
  public void negativeInterpolationPercent100ReturnsEndValue() {
    double start = 100;
    double end = -100;
    double percent = 1;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == end);
  }

  @Test
  public void negativeOutOfBoundsPercentClampsToStart() {
    double start = 100;
    double end = -100;
    double percent = -1;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == start);
  }

  @Test
  public void negativeOutOfBoundsPercentCase2ClampsToEnd() {
    double start = 100;
    double end = -100;
    double percent = 2;

    double result = ImageUtilities.linearInterpolation(percent, start, end);
    assertTrue(result == end);
  }
}
