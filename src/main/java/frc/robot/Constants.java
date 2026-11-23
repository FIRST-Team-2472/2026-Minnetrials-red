package frc.robot;

import edu.wpi.first.math.util.Units;

public final class Constants {
  public static class ControllerConstants {
  public static final int kDriverControllerPort = 0;
    public static final int kLeftJoystickPort = 1;
    public static final int kRightJoystickPort = 2;
    public static final double kDeadband = 0.05;
  }

  public static class ElevatorConstants {
    public static final int kMaxHeight = 52;
    public static final int kMotorID = 0;
    public static final int kGearReduction = 0;
    public static final int kWinchDiameter = 0;
  }
  public static class DrivetrainConstants {
    public static final int kLeftDriveMotorID = 0;
    public static final int kRightDriveMotorID = 1;
    public static final double kWheelDiameter = Units.inchesToMeters(6);
    public static final double kTrackWidth = Units.inchesToMeters(22.5);
    public static final int kGearReduction = 12;
    public static final double kMaxSpeed = 3.2;
  }
}

//make it stop before 60in // ok 67