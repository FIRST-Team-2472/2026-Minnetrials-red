// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.TankDriveCommand;
import frc.robot.extra.Limelight;
import frc.robot.subsystems.TankDriveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {

  private final CommandXboxController xboxController = new CommandXboxController(ControllerConstants.kDriverControllerPort);
  private final CommandJoystick leftJoystick = new CommandJoystick(0);
  private final CommandJoystick rightJoystick = new CommandJoystick(0);


  TankDriveSubsystem tankDriveSubsystem = new TankDriveSubsystem();
  SendableChooser<Command> autoChooser;

  private Limelight limelight = new Limelight("limelight", tankDriveSubsystem, true, true, () -> rightJoystick.button(14).getAsBoolean());

  public RobotContainer() {
    configureBindings();
    limelight.enable();
    limelight.trustLL(true);
  }

  private void configureBindings() {
    tankDriveSubsystem.setDefaultCommand(new TankDriveCommand(tankDriveSubsystem, () -> leftJoystick.getY(), () -> rightJoystick.getX()));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}