// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.util.Units;

public final class Constants {
<<<<<<< HEAD
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 2;

  }
  public static class IntakeConstants {
    public static final int kIntakeID = 0;
    public static final int kIntakeInSpeed = 10;
    public static final int kIntakeOutSpeed = -10;
    public static final int kIntakeOffSpeed = 0;
=======
  public static class ControllerConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kLeftJoystickPort = 1;
    public static final int kRightJoystickPort = 2;
    public static final double kDeadband = 0.05;
  }

  public static class DrivetrainConstants {
    public static final int kLeftDriveMotorID = 0;
    public static final int kRightDriveMotorID = 1;
    public static final double kWheelDiameter = Units.inchesToMeters(6);
    public static final double kTrackWidth = Units.inchesToMeters(22.5);
    public static final int kGearReduction = 12;
    public static final double kMaxSpeed = 3.2;
>>>>>>> main
  }
}
