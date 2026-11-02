// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.util.Units;

public final class Constants {
  public static class ControllerConstants {
    public static final int kDriverControllerPort = 0;
    public static final double kDeadband = 0.05;
  }

  public static class DrivetrainConstants {
    public static final int kLeftDriveMotorID = 0;
    public static final int kRightDriveMotorID = 0;
    public static final double kWheelDiameter = Units.inchesToMeters(0);
    public static final int kGearReduction = 1;
    public static final double kMaxSpeed = 3.2;
  }
}
