package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorCommand extends Command {
    ElevatorSubsystem elevatorSubsystem;
    Supplier<Double> joystick;

    public ElevatorCommand(ElevatorSubsystem elevatorSubsystem, Supplier<Double> joystick) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.joystick = joystick;
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double power = joystick.get();

        elevatorSubsystem.spin(power);
    }

    @Override
    public void end(boolean isInterrupted) {
        elevatorSubsystem.spin(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}