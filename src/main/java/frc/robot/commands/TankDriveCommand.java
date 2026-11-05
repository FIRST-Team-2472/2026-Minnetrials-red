package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.ControllerConstants;
import frc.robot.extra.AccelLimiter;
import frc.robot.extra.MotorPowerController;
import frc.robot.subsystems.TankDriveSubsystem;

public class TankDriveCommand extends Command {
    TankDriveSubsystem tankDriveSubsystem;
    Supplier<Double> leftJoystick, rightJoystick;
    MotorPowerController speedPowerController;
    AccelLimiter speedAccelLimiter;
    final double kMaxSpeed = Constants.DrivetrainConstants.kMaxSpeed;

    public TankDriveCommand(TankDriveSubsystem tankDriveSubsystem, Supplier<Double> leftJoystick, Supplier<Double> rightJoystick) {
        this.tankDriveSubsystem = tankDriveSubsystem;
        this.leftJoystick = leftJoystick;
        this.rightJoystick = rightJoystick;
        addRequirements(tankDriveSubsystem);

        speedAccelLimiter = new AccelLimiter(0.02, 0.1);
        speedPowerController = new MotorPowerController(0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double drivePower = leftJoystick.get();
        double turnPower = rightJoystick.get();

        if (Math.abs(drivePower) < ControllerConstants.kDeadband) {
            turnPower = 0;
        }

        if (Math.abs(turnPower) < ControllerConstants.kDeadband) {
            drivePower = 0;
        }

        if(drivePower == 0) {
            speedPowerController.calculate(drivePower * kMaxSpeed, -tankDriveSubsystem.getSpeedMeters());
        } else {
            drivePower = speedPowerController.calculate(drivePower * kMaxSpeed, -tankDriveSubsystem.getSpeedMeters());
        }

        tankDriveSubsystem.drive(drivePower, turnPower);
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