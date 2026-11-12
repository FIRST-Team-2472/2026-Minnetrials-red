package frc.robot.subsystems;

import static edu.wpi.first.units.Units.RadiansPerSecond;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPLTVController;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.MotorPowerController;
import frc.robot.Constants.DrivetrainConstants;

public class TankDriveSubsystem extends SubsystemBase {
    private SparkMax leftMotor = new SparkMax(DrivetrainConstants.kLeftDriveMotorID, MotorType.kBrushless);
    private SparkMax rightMotor = new SparkMax(DrivetrainConstants.kRightDriveMotorID, MotorType.kBrushless);
    private Pigeon2 pigeon = new Pigeon2(0);
    private final double wheelDiameter = DrivetrainConstants.kWheelDiameter;
    private final MotorPowerController autoSpeedController = new MotorPowerController(0, 0, 0, 0, 0, 0, 0);
    private final MotorPowerController autoYawController = new MotorPowerController(0, 0, 0, 0, 0, 0, 0);

    DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(0);
    DifferentialDrivePoseEstimator poseEstimator;

    public TankDriveSubsystem() {
        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(35);
        config.idleMode(IdleMode.kBrake);
        leftMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        poseEstimator = new DifferentialDrivePoseEstimator(kinematics, pigeon.getRotation2d(), Units.inchesToMeters(getLeftInchesTraveled()), Units.inchesToMeters(getRightInchesTraveled()), new Pose2d());


        RobotConfig robotConfig = null;

        try {
            robotConfig = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AutoBuilder.configure(
            this::getPosition, 
            this::setPosition, 
            this::getCurrentChassisSpeeds, 
            (speeds, feedForward) -> driveWithChassisSpeeds(speeds), 
            new PPLTVController(0.02), 
            robotConfig, 
            () -> {return false;}, 
            this);
    }

    private double getRightInchesTraveled() {
        return rightMotor.getEncoder().getPosition() * wheelDiameter * Math.PI / DrivetrainConstants.kGearReduction;
    }

    private double getLeftInchesTraveled() {
        return leftMotor.getEncoder().getPosition() * wheelDiameter * Math.PI / DrivetrainConstants.kGearReduction;
    }

    private double getRightSpeed() {
        return Units.inchesToMeters(rightMotor.getEncoder().getVelocity() * wheelDiameter * Math.PI / DrivetrainConstants.kGearReduction / 60);
    }

    private double getLeftSpeed() {
        return Units.inchesToMeters(leftMotor.getEncoder().getVelocity() * wheelDiameter * Math.PI / DrivetrainConstants.kGearReduction / 60);
    }

    public void drive(double drivePower, double turnPower) {
        leftMotor.set(drivePower - turnPower);
        rightMotor.set(drivePower + turnPower);
    }

    public void driveWithChassisSpeeds(ChassisSpeeds chassisSpeeds) {
        double driveSpeed = autoSpeedController.calculate(chassisSpeeds.vxMetersPerSecond, getSpeedMeters());
        double yawSpeed = autoYawController.calculate(chassisSpeeds.omegaRadiansPerSecond, getRotationSpeed());

        drive(driveSpeed, yawSpeed);
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

    public double getRotationSpeed() {
        return pigeon.getAngularVelocityZWorld().getValue().in(RadiansPerSecond);
    }

    public ChassisSpeeds getCurrentChassisSpeeds(){
        DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds(getLeftSpeed(), getRightSpeed());
        return kinematics.toChassisSpeeds(wheelSpeeds);
    }

    public double getSpeedMeters() {
        double leftVelocity = leftMotor.getEncoder().getVelocity();
        double rightVelocity = -rightMotor.getEncoder().getVelocity();

        return Units.inchesToMeters((leftVelocity + rightVelocity) / 2 / DrivetrainConstants.kGearReduction * (wheelDiameter * Math.PI)) / 60;
    }

    @Override
    public void periodic() {
        poseEstimator.update(
            getRotation(), 
            Units.inchesToMeters(getLeftInchesTraveled()), 
            Units.inchesToMeters(getRightInchesTraveled()));
    }
}