package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subShooter extends SubsystemBase {
  private final int kShooterMotorTopId = 12;
  private final int kShooterMotorBottomId = 11;
  private final CANSparkMax shooterMotorTop = new CANSparkMax(kShooterMotorTopId, MotorType.kBrushless);  ;
  private final CANSparkMax shooterMotorBottom = new CANSparkMax(kShooterMotorBottomId, MotorType.kBrushless);;
  private final RelativeEncoder shooterEncoderTop = shooterMotorTop.getEncoder();
  private final RelativeEncoder shooterEncoderBottom = shooterMotorBottom.getEncoder();
  private final SparkPIDController shooterTopPID = shooterMotorTop.getPIDController();
  private final SparkPIDController shooterBottomPID = shooterMotorBottom.getPIDController();
  
  public subShooter() {
    shooterMotorTop.restoreFactoryDefaults();
    shooterMotorBottom.restoreFactoryDefaults();

    shooterMotorTop.setIdleMode(IdleMode.kCoast);
    shooterMotorBottom.setIdleMode(IdleMode.kCoast);

    shooterMotorTop.setInverted(false);
    shooterMotorBottom.setInverted(true);

    shooterTopPID.setFeedbackDevice(shooterEncoderTop);
    shooterTopPID.setP(0.1);
    shooterTopPID.setI(1e-4);
    shooterTopPID.setD(1);
    shooterTopPID.setIZone(0);
    shooterTopPID.setFF(0);
    shooterTopPID.setOutputRange(0, 1);

    shooterBottomPID.setFeedbackDevice(shooterEncoderBottom);
    shooterBottomPID.setP(0.1);
    shooterBottomPID.setI(1e-4);
    shooterBottomPID.setD(1);
    shooterBottomPID.setIZone(0);
    shooterBottomPID.setFF(0);
    shooterBottomPID.setOutputRange(0, 1);
    
    shooterMotorTop.burnFlash();
    shooterMotorBottom.burnFlash();
  }

  public void setVelocity(double velocity){
    shooterTopPID.setReference(velocity, CANSparkMax.ControlType.kVelocity);
    shooterBottomPID.setReference(velocity, CANSparkMax.ControlType.kVelocity);
  }

  public void stop(){
    shooterMotorTop.set(0);
    shooterMotorBottom.set(0);
    shooterMotorTop.stopMotor();
    shooterMotorBottom.stopMotor();
  }

  public void teleOp(double speed){
    shooterMotorTop.set(speed);
    shooterMotorBottom.set(speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter Velocity", shooterEncoderTop.getVelocity());
    //SmartDashboard.putNumber("Shooter Power", shooterMotorBottom.get());
  }
}
