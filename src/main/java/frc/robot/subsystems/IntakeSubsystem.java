package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
    private final SparkMax intakeMotor = new SparkMax(IntakeConstants.kIntakeID, MotorType.kBrushless);
    public IntakeSubsystem() {
        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(35);
        config.idleMode(IdleMode.kBrake);
        intakeMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }


    public void runIntakeIn() {
        intakeMotor.set(IntakeConstants.kIntakeInSpeed);
    }

    public void runIntakeOut() {
        intakeMotor.set(IntakeConstants.kIntakeOutSpeed);
    }
}