package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.MotorPowerController;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorCommand extends Command {
    ElevatorSubsystem elevatorSubsystem;
    Supplier<Double> joystick;
    MotorPowerController motorPowerController;

    public ElevatorCommand(ElevatorSubsystem elevatorSubsystem, Supplier<Double> joystick) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.joystick = joystick;
        motorPowerController = new MotorPowerController(0, 0, 0, 0, 0, 0, 0);
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double targetHeight = elevatorSubsystem.getInchesTraveled() + joystick.get();
        double power;

        targetHeight = Math.min(targetHeight, 0); //0 is max height
        targetHeight = Math.max(targetHeight, 0); //0 is min height

        power = motorPowerController.calculate(targetHeight, elevatorSubsystem.getInchesTraveled());

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