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

    private SwerveModuleState desiredState = new SwerveModuleState(0.0, new Rotation2d());

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

        desiredState.angle = new Rotation2d(0);
        drivingEncoder.setPosition(0);
    }
    
    public SwerveModuleState getState() {return new SwerveModuleState(drivingEncoder.getVelocity(), getRotation2d()); }
    public SwerveModulePosition getPosition() {return new SwerveModulePosition(drivingEncoder.getPosition(), getRotation2d());}
    public double GetModuleAngle() { return rotationEncoder.getAbsolutePosition().getValueAsDouble(); }
    public void setDesiredState(SwerveModuleState desiredState) {
        SwerveModuleState optimizedDesiredState = SwerveModuleState.optimize(desiredState, getRotation2d());
        if(Math.abs(optimizedDesiredState.speedMetersPerSecond) < 0.01){
            stopModule();
            return;
        }
        drivingPIDController.setReference(optimizedDesiredState.speedMetersPerSecond, CANSparkMax.ControlType.kVelocity);
        turningSparkMax.set(turningPIDController.calculate(getAngle(), optimizedDesiredState.angle.getDegrees()));
        this.desiredState = desiredState;
    }

    public void resetEncoders() {
        drivingEncoder.setPosition(0);
    }
    public double getAngle() { 
        return rotationEncoder.getAbsolutePosition().getValueAsDouble() * 360; 
    }
    public double getRawAngle(){
        return rotationEncoder.getAbsolutePosition().getValueAsDouble();
    }
    public Rotation2d getRotation2d() { 
        return Rotation2d.fromDegrees(getAngle()); 
    }
    public void stopModule(){ 
        drivingSparkMax.stopMotor(); turningSparkMax.stopMotor();
    }
}