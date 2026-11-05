// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.extra;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.TankDriveSubsystem;

import com.ctre.phoenix6.StatusSignal;
import java.util.function.Supplier;

public class Limelight extends SubsystemBase {
    TankDriveSubsystem drivetrain;
    Alliance alliance;
    private String ll;
    private boolean enable = false;
    private boolean trust = false;
    private int fieldError = 0;
    private int distanceError = 0;
    private Pose2d llPose;
    private final boolean isMegaTag2;
    private final boolean isLL4;
    private boolean wasDisabled = false;
    private double heartbeat = 0;
    private final StatusSignal<AngularVelocity> yawVelocitySignal;
    private final Supplier<Boolean> disableButton;
    private static final RectanglePoseArea FIELD = new RectanglePoseArea(new Translation2d(0.0, 0.0), new Translation2d(Units.feetToMeters(27), Units.feetToMeters(30)));

    // AprilTag layout
    public static AprilTagFieldLayout aprilTagLayout = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

    /** Creates a new Limelight. */
    public Limelight(String name, TankDriveSubsystem drivetrain, boolean isMegaTag2, boolean isLL4, Supplier<Boolean> disableButton) {
        this.drivetrain = drivetrain;
        this.isMegaTag2 = isMegaTag2;
        this.isLL4 = isLL4;
        this.disableButton = disableButton;
        ll = name;
        yawVelocitySignal = drivetrain.getPigeon2().getAngularVelocityZWorld();
        SmartDashboard.putNumber("Field Error", fieldError);
        SmartDashboard.putNumber("Limelight Error", distanceError);

        LimelightHelpers.SetIMUAssistAlpha(ll, 0.01);
    }

    @Override
    public void periodic() {
        if (disableButton.get()) {
            wasDisabled = true;
        }

        boolean isDisabled = DriverStation.isDisabled();
        // 1. Handle Pipeline and IMU states without manual caching
        if (isDisabled) {
            LimelightHelpers.setPipelineIndex(ll, 1); // Low-power / idle pipeline
            LimelightHelpers.SetIMUMode(ll, 1); // Mode 1: Seed internal IMU with Pigeon
        } else {
            LimelightHelpers.setPipelineIndex(ll, 0); // Active tracking pipeline
            LimelightHelpers.SetIMUMode(ll, 1); // Mode 1: Seed internal IMU with Pigeon
        }

        // 2. ALWAYS send Pigeon data so Mode 1 and Mode 4 have data to work with
        Pose2d robotPose = drivetrain.getPosition();
        double currentYawVelocity = yawVelocitySignal.refresh().getValueAsDouble();

        LimelightHelpers.SetRobotOrientation(ll, robotPose.getRotation().getDegrees(), currentYawVelocity, 0.0, 0.0, 0.0, 0.0);

        double heartbeatNow = LimelightHelpers.getHeartbeat(ll);
        if (heartbeat >= heartbeatNow) {
            // oops, I guess it's dead :(
            // is this a case of murder? :()
            return;
        }

        heartbeat = heartbeatNow;

        if (enable && !isSpinningTooFast()) {
            LimelightHelpers.PoseEstimate poseEstimate;

            if (isMegaTag2) {
                poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(ll);
            } else {
                poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(ll);
            }

            double targetDistance = poseEstimate.avgTagDist;
            double confidence = Math.max(((targetDistance - 1) / 6), 0.01);

            double[] rawStdDevs = NetworkTableInstance.getDefault().getTable(ll).getEntry("stddevs").getDoubleArray(new double[12]);

            Vector<N3> stdDevs = VecBuilder.fill(confidence, confidence, Double.MAX_VALUE);
            // Use measured pose standard deviations from the limelight if present
            if (rawStdDevs != null) {
                if (isMegaTag2) {
                    stdDevs = VecBuilder.fill(rawStdDevs[6] * 2.5, rawStdDevs[7] * 2.5, Double.MAX_VALUE);
                } else {
                    // MegaTag1 standard deviations
                    stdDevs = VecBuilder.fill(rawStdDevs[0] * 2.5, rawStdDevs[1] * 2.5, Double.MAX_VALUE);
                }
            }

            if (poseEstimate.tagCount > 0) {

                this.llPose = poseEstimate.pose;
                this.llPose = new Pose2d(llPose.getTranslation(), robotPose.getRotation());

                if (FIELD.isPoseWithinArea(this.llPose)) {

                    if (robotPose.getTranslation().getDistance(this.llPose.getTranslation()) < 0.5 || trust) {
                        drivetrain.addVisionMeasurement(this.llPose, poseEstimate.timestampSeconds, stdDevs);
                    } else {
                        distanceError++;
                        SmartDashboard.putNumber("Limelight Error", distanceError);
                    }

                } else {
                    fieldError++;
                    SmartDashboard.putNumber("Field Error", fieldError);
                }
            }
        }
    }

    private boolean isSpinningTooFast() {
        double angularVelocity = yawVelocitySignal.refresh().getValueAsDouble();

        if (isLL4) {
            return false;
        } else {
            return angularVelocity > 180;
        }
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public void useLimelight(boolean enable) {
        this.enable = enable;
    }

    public void trustLL(boolean trust) {
        this.trust = trust;
    }

    public void enable() {
        enable = true;
    }
}