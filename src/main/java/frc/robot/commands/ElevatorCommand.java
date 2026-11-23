package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.MotorPowerController;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorCommand extends Command {
    ElevatorSubsystem elevatorSubsystem;
    Supplier<Double> joystick;
    MotorPowerController motorPowerController;
    double targetHeight;

    public ElevatorCommand(ElevatorSubsystem elevatorSubsystem, Supplier<Double> joystick) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.joystick = joystick;
        motorPowerController = new MotorPowerController(0, 0, 0, 0, 0, 0, 0);
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void initialize() {
        targetHeight = 0;
    }

    @Override
    public void execute() {
        targetHeight = elevatorSubsystem.getInchesTraveled();
        double power;

        if (Math.abs(joystick.get()) >= ControllerConstants.kDeadband) {
            targetHeight += joystick.get(); // Adjust the scaling factor as needed
        }

        targetHeight = Math.min(targetHeight, ElevatorConstants.kMaxHeight); //0 is max height
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