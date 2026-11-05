// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.ElevatorCommand;
import frc.robot.subsystems.ElevatorSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

public class RobotContainer {

  private final XboxController xboxController = new XboxController(0);
  ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
  SendableChooser<Command> autoChooser;

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    elevatorSubsystem.setDefaultCommand(new ElevatorCommand(elevatorSubsystem, () -> xboxController.getLeftY()));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
