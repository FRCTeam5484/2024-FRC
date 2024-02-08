package frc.robot.classes;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants.SwerveConstants;

public class swerveModule {
    private final CANcoder rotationEncoder;

    private final CANSparkMax drivingSparkMax;
    private final CANSparkMax turningSparkMax;

    private final RelativeEncoder drivingEncoder;

    private final SparkPIDController drivingPIDController;
    private final PIDController turningPIDController;

    public swerveModule(int drivingCANId, int turningCANId, int canCoderId, double angularOffset) {
        rotationEncoder = new CANcoder(canCoderId);
        CANcoderConfiguration canCoderConfiguration = new CANcoderConfiguration();
        canCoderConfiguration.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        canCoderConfiguration.MagnetSensor.MagnetOffset = -angularOffset;
        rotationEncoder.getConfigurator().apply(canCoderConfiguration);
        rotationEncoder.getPosition().setUpdateFrequency(100);
        rotationEncoder.getVelocity().setUpdateFrequency(100);

        drivingSparkMax = new CANSparkMax(drivingCANId, MotorType.kBrushless);
        turningSparkMax = new CANSparkMax(turningCANId, MotorType.kBrushless);

        drivingSparkMax.restoreFactoryDefaults();
        turningSparkMax.restoreFactoryDefaults();

        drivingEncoder = drivingSparkMax.getEncoder();

        drivingPIDController = drivingSparkMax.getPIDController();
        drivingPIDController.setFeedbackDevice(drivingEncoder);

        turningPIDController = new PIDController(SwerveConstants.kTurningP, SwerveConstants.kTurningI, SwerveConstants.kTurningD);    
        turningPIDController.enableContinuousInput(0, 360);    

        drivingEncoder.setPositionConversionFactor(SwerveConstants.kDrivingEncoderPositionFactor);
        drivingEncoder.setVelocityConversionFactor(SwerveConstants.kDrivingEncoderVelocityFactor);

        drivingPIDController.setP(SwerveConstants.kDrivingP);
        drivingPIDController.setI(SwerveConstants.kDrivingI);
        drivingPIDController.setD(SwerveConstants.kDrivingD);
        drivingPIDController.setFF(SwerveConstants.kDrivingFF);
        drivingPIDController.setOutputRange(SwerveConstants.kDrivingMinOutput, SwerveConstants.kDrivingMaxOutput);

        drivingSparkMax.setIdleMode(IdleMode.kBrake);
        turningSparkMax.setIdleMode(IdleMode.kBrake);
        drivingSparkMax.setSmartCurrentLimit(SwerveConstants.kDrivingMotorCurrentLimit);
        turningSparkMax.setSmartCurrentLimit(SwerveConstants.kTurningMotorCurrentLimit);

        drivingSparkMax.burnFlash();
        turningSparkMax.burnFlash();

        drivingEncoder.setPosition(0);
    }
    
    public SwerveModuleState getState() {return new SwerveModuleState(drivingEncoder.getVelocity(), Rotation2d.fromDegrees(getAngle())); }
    public SwerveModulePosition getPosition() {return new SwerveModulePosition(drivingEncoder.getPosition(), Rotation2d.fromDegrees(getAngle()));}
    public void setDesiredState(SwerveModuleState desiredState) {
        SwerveModuleState optimizedDesiredState = SwerveModuleState.optimize(desiredState, Rotation2d.fromDegrees(getAngle()));
        if(Math.abs(optimizedDesiredState.speedMetersPerSecond) < 0.01){
            stopModule();
            return;
        }
        drivingPIDController.setReference(optimizedDesiredState.speedMetersPerSecond, CANSparkMax.ControlType.kVelocity);
        turningSparkMax.set(turningPIDController.calculate(getAngle(), optimizedDesiredState.angle.getDegrees()));
    }
    public double getRawAngle() { return rotationEncoder.getAbsolutePosition().getValueAsDouble(); }
    public double getAngle() { return getRawAngle() * 360; }    
    public void stopModule() { drivingSparkMax.stopMotor(); turningSparkMax.stopMotor(); }
    public void resetEncoders() { drivingEncoder.setPosition(0); }
}