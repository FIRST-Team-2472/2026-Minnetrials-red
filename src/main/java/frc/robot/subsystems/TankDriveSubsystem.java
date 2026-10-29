package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DrivetrainConstants;

public class TankDriveSubsystem extends SubsystemBase {
    private SparkMax leftMotor = new SparkMax(DrivetrainConstants.kLeftDriveMotorID, MotorType.kBrushless);
    private SparkMax rightMotor = new SparkMax(DrivetrainConstants.kRightDriveMotorID, MotorType.kBrushless);
    private Pigeon2 pigeon = new Pigeon2(0);

    DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(0);
    DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(pigeon.getRotation2d(), leftMotor.getEncoder().getPosition() * DrivetrainConstants.kWheelCircumference, rightMotor.getEncoder().getPosition() * DrivetrainConstants.kWheelCircumference);

    public TankDriveSubsystem() {
        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(35);
        config.idleMode(IdleMode.kBrake);
        leftMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void drive(double leftPower, double rightPower) {
        leftMotor.set(leftPower);
        rightMotor.set(rightPower);
    }

    public Pose2d getPosition() {
        return odometry.getPoseMeters();
    }

    public Rotation2d getRotation() {
        return new Rotation2d(pigeon.getRotation2d().getRadians());
    }

    @Override
    public void periodic() {
        odometry.update(
            getRotation(), 
            leftMotor.getEncoder().getPosition() * DrivetrainConstants.kWheelCircumference, 
            rightMotor.getEncoder().getPosition() * DrivetrainConstants.kWheelCircumference);
    }
}