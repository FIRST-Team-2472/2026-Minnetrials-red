// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

<<<<<<< HEAD
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
=======
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.TankDriveCommand;
import frc.robot.subsystems.TankDriveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
>>>>>>> main
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
<<<<<<< HEAD
  // The robot's subsystems and commands are defined here...
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();  

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController controller =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
=======

  private final CommandXboxController xboxController = new CommandXboxController(ControllerConstants.kDriverControllerPort);
  private final CommandJoystick leftJoystick = new CommandJoystick(ControllerConstants.kLeftJoystickPort);
  private final CommandJoystick rightJoystick = new CommandJoystick(ControllerConstants.kRightJoystickPort);
  TankDriveSubsystem tankDriveSubsystem = new TankDriveSubsystem();
  SendableChooser<Command> autoChooser;
>>>>>>> main

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
<<<<<<< HEAD
    intakeSubsystem.setDefaultCommand(new IntakeCommand(intakeSubsystem, () -> controller.leftTrigger().getAsBoolean() , () -> controller.rightTrigger().getAsBoolean()));
   
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
=======
    tankDriveSubsystem.setDefaultCommand(new TankDriveCommand(tankDriveSubsystem, () -> leftJoystick.getY(), () -> rightJoystick.getX()));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
>>>>>>> main
