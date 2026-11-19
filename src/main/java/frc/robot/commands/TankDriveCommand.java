package frc.robot.commands;

import java.time.chrono.JapaneseEra;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.AccelLimiter;
import frc.robot.Constants;
import frc.robot.MotorPowerController;
import frc.robot.Constants.ControllerConstants;
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
        speedPowerController = new MotorPowerController(0.33, 0.1, 0, 0, 0, 0.67, 1);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double drivePower = -leftJoystick.get();
        double turnPower = -rightJoystick.get();

        if (Math.abs(turnPower) < ControllerConstants.kDeadband) {
            turnPower = 0;
        }

        if (Math.abs(drivePower) < ControllerConstants.kDeadband) {
            drivePower = 0;
        }

        if(drivePower == 0) {
            speedPowerController.calculate(drivePower * kMaxSpeed, -tankDriveSubsystem.getSpeedMeters());
        } else {
            drivePower = speedPowerController.calculate(drivePower * kMaxSpeed, -tankDriveSubsystem.getSpeedMeters());
        }

        drivePower = speedAccelLimiter.calculate(drivePower);
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