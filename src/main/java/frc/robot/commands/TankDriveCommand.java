package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ControllerConstants;
import frc.robot.subsystems.TankDriveSubsystem;

public class TankDriveCommand extends Command {
    TankDriveSubsystem tankDriveSubsystem;
    Supplier<Double> leftJoystick, rightJoystick;

    public TankDriveCommand(TankDriveSubsystem tankDriveSubsystem, Supplier<Double> leftJoystick,
            Supplier<Double> rightJoystick) {
        this.tankDriveSubsystem = tankDriveSubsystem;
        this.leftJoystick = leftJoystick;
        this.rightJoystick = rightJoystick;
        addRequirements(tankDriveSubsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double leftPower = leftJoystick.get();
        double rightPower = rightJoystick.get();

        if (Math.abs(rightJoystick.get()) > ControllerConstants.kDeadband) {
            leftPower += rightJoystick.get();
            rightPower -= rightJoystick.get();
        }

        if(Math.abs(leftJoystick.get()) > ControllerConstants.kDeadband) {
            tankDriveSubsystem.drive(leftPower,rightPower);
        }
    }

    @Override
    public void end(boolean isInterrupted) {
        tankDriveSubsystem.drive(0, 0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}