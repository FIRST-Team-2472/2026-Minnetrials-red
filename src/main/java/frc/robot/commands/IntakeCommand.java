// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.*;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeCommand extends Command {
  private final IntakeSubsystem intakeSubsystem;
  Supplier<Boolean> leftTrigger; // intake in
  Supplier<Boolean> rightTrigger; // intake out

  /**
   * Creates a new ExampleCommand.
   *
   * //param subsystem The subsystem used by this command.
   */
  public IntakeCommand(IntakeSubsystem intakeSubsystem, Supplier<Boolean> leftTrigger, Supplier<Boolean> rightTrigger) {
    this.intakeSubsystem = intakeSubsystem;
    this.leftTrigger = leftTrigger;
    this.rightTrigger = rightTrigger;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (leftTrigger.get()) {
      intakeSubsystem.runIntakeIn();
    } else if (rightTrigger.get()) {
      intakeSubsystem.runIntakeOut();
    } else {
      intakeSubsystem.stopIntake();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
