package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.extra.LimelightHelpers;

public class TankDriveSubsystem extends SubsystemBase {
    private SparkMax leftMotor = new SparkMax(DrivetrainConstants.kLeftDriveMotorID, MotorType.kBrushless);
    private SparkMax rightMotor = new SparkMax(DrivetrainConstants.kRightDriveMotorID, MotorType.kBrushless);
    private Pigeon2 pigeon = new Pigeon2(0);
    private final double wheelDiameter = DrivetrainConstants.kWheelDiameter;

    DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(0);
    DifferentialDrivePoseEstimator poseEstimator;

    public TankDriveSubsystem() {
        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(35);
        config.idleMode(IdleMode.kBrake);
        leftMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        poseEstimator = new DifferentialDrivePoseEstimator(kinematics, pigeon.getRotation2d(), Units.inchesToMeters(getLeftInchesTraveled()), Units.inchesToMeters(getRightInchesTraveled()), new Pose2d());
    }

    private double getRightInchesTraveled() {
        return rightMotor.getEncoder().getPosition() * wheelDiameter * Math.PI / DrivetrainConstants.kGearReduction;
    }

    private double getLeftInchesTraveled() {
        return leftMotor.getEncoder().getPosition() * wheelDiameter * Math.PI / DrivetrainConstants.kGearReduction;
    }

    public void drive(double drivePower, double turnPower) {
        leftMotor.set(drivePower - turnPower);
        rightMotor.set(drivePower + turnPower);
    }

    public Pose2d getPosition() {
        return poseEstimator.getEstimatedPosition();
    }

    public void setPosition(Pose2d pose) {
        poseEstimator.resetPose(pose);
    }

    public Rotation2d getRotation() {
        return new Rotation2d(pigeon.getRotation2d().getRadians());
    }

    public double getSpeedMeters() {
        double leftVelocity = leftMotor.getEncoder().getVelocity();
        double rightVelocity = -rightMotor.getEncoder().getVelocity();

        return Units.inchesToMeters((leftVelocity + rightVelocity) / 2 / DrivetrainConstants.kGearReduction * (wheelDiameter * Math.PI)) / 60;
    }

    public Pigeon2 getPigeon2() {
        return pigeon;
    }

    public void addVisionMeasurement(Pose2d limelightPose, double timestamp, Vector<N3> stdDevs) {
        poseEstimator.setVisionMeasurementStdDevs(stdDevs);
        poseEstimator.addVisionMeasurement(limelightPose, timestamp);
    }

    @Override
    public void periodic() {
        poseEstimator.update(
            getRotation(), 
            Units.inchesToMeters(getLeftInchesTraveled()), 
            Units.inchesToMeters(getRightInchesTraveled()));
    }
}