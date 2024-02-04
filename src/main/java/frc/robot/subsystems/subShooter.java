package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subShooter extends SubsystemBase {
  private final int kShooterMotorId = 12;
  private final CANSparkMax shooterMotor;
  private final RelativeEncoder shooterEncoder;
  private final SparkPIDController shooterPID;
  
  public subShooter() {
    shooterMotor = new CANSparkMax(kShooterMotorId, MotorType.kBrushless);
    shooterMotor.restoreFactoryDefaults();
    shooterMotor.setIdleMode(IdleMode.kCoast);
    shooterMotor.setInverted(false);
    shooterEncoder = shooterMotor.getEncoder();
    shooterPID = shooterMotor.getPIDController();
    shooterPID.setFeedbackDevice(shooterEncoder);
    shooterPID.setP(0.1);
    shooterPID.setI(1e-4);
    shooterPID.setD(1);
    shooterPID.setIZone(0);
    shooterPID.setFF(0);
    shooterPID.setOutputRange(-1, 1);
    shooterMotor.burnFlash();
  }

  public void setVelocity(double velocity){
    shooterPID.setReference(velocity, CANSparkMax.ControlType.kVelocity);
  }

  public void stop(){
    shooterMotor.set(0);
  }

  public void set(double speed){
    shooterMotor.set(speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter Velocity", shooterEncoder.getVelocity());
  }
}
