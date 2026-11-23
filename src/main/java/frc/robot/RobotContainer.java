// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();  

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController xboxController = new CommandXboxController(OperatorConstants.kDriverControllerPort);

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    intakeSubsystem.setDefaultCommand(new IntakeCommand(intakeSubsystem, () -> xboxController.leftTrigger().getAsBoolean() , () -> xboxController.rightTrigger().getAsBoolean()));
  }

  public Command getAutonomousCommand() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAutonomousCommand'");
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  
}
