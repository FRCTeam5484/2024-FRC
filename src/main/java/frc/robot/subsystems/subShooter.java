package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subShooter extends SubsystemBase {
  private final int kShooterMotorId = 12;
  private final int kShooter2ndMotorId = 11;
  private final CANSparkMax shooterMotor = new CANSparkMax(kShooterMotorId, MotorType.kBrushless);  ;
  private final CANSparkMax shooterMotor2 = new CANSparkMax(kShooter2ndMotorId, MotorType.kBrushless);;
  private final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();
  private final SparkPIDController shooterPID = shooterMotor.getPIDController();
  
  public subShooter() {
    shooterMotor.restoreFactoryDefaults();
    shooterMotor.setIdleMode(IdleMode.kCoast);
    shooterMotor.setInverted(false);
    shooterPID.setFeedbackDevice(shooterEncoder);
    shooterPID.setP(0.1);
    shooterPID.setI(1e-4);
    shooterPID.setD(1);
    shooterPID.setIZone(0);
    shooterPID.setFF(0);
    shooterPID.setOutputRange(-1, 1);
    shooterMotor.burnFlash();

    shooterMotor2.restoreFactoryDefaults();
    shooterMotor2.setIdleMode(IdleMode.kCoast);
    shooterMotor2.setInverted(true);
    shooterMotor2.burnFlash();
  }

  public void setVelocity(double velocity){
    shooterPID.setReference(velocity, CANSparkMax.ControlType.kVelocity);
  }

  public void stop(){
    shooterMotor.set(0);
    shooterMotor2.set(0);
  }

  public void set(double speed){
    shooterMotor.set(speed);
    shooterMotor2.set(speed);
  }

  @Override
  public void periodic() {
    //SmartDashboard.putNumber("Shooter Velocity", shooterEncoder.getVelocity());
  }
}
