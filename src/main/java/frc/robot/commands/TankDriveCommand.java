package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Timer;
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
    Supplier<Boolean> yButton;
    Supplier<Boolean> xButton;
    Timer timer = new Timer();
    final double kMaxSpeed = Constants.DrivetrainConstants.kMaxSpeed;
    public TankDriveCommand(TankDriveSubsystem tankDriveSubsystem, Supplier<Double> leftJoystick, Supplier<Double> rightJoystick, Supplier<Boolean> yButton, Supplier<Boolean> xButton) {
        this.tankDriveSubsystem = tankDriveSubsystem;
        this.leftJoystick = leftJoystick;
        this.rightJoystick = rightJoystick;
        this.yButton = yButton;
        this.xButton = xButton;
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
        double tx = tankDriveSubsystem.getTagTX();
        double ta  = tankDriveSubsystem.getTagTA();
        double direction = tx / Math.abs(tx); // either 1 or -1 always
       
        if (yButton.get() && ta > 0) {
            while (tx > 1 || tx < -1) {
                    tankDriveSubsystem.drive(0, direction);
            }
            while (ta < 40) {
                tankDriveSubsystem.drive(1, 0);
            }
            while (tx > 1 || tx < -1) {
                    tankDriveSubsystem.drive(0, direction);
            }
            
            
               
            
            
        } else if (xButton.get()){
            while (tx > 1 || tx < -1) {
                    tankDriveSubsystem.drive(0, direction);
            }
            tankDriveSubsystem.drive(0, 0);
        } else {

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