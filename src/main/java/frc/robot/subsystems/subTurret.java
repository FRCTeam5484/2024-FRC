package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subTurret extends SubsystemBase {
  private final int kTurretMotorId = 9;
  //private final int kMinimumPosition = 0;
  //private final int kMaximumPosition = 100;

  private final CANSparkMax turretMotor = new CANSparkMax(kTurretMotorId, MotorType.kBrushless);
  private final RelativeEncoder turretEncoder = turretMotor.getEncoder();
  private final SparkPIDController turretPID = turretMotor.getPIDController();

  public subTurret() {
    turretMotor.restoreFactoryDefaults();
    turretMotor.setIdleMode(IdleMode.kBrake);
    turretMotor.setInverted(false);
    turretPID.setFeedbackDevice(turretEncoder);
    turretPID.setP(0.1);
    turretPID.setI(1e-4);
    turretPID.setD(1);
    turretPID.setIZone(0);
    turretPID.setFF(0);
    turretPID.setOutputRange(-1, 1);
    turretMotor.burnFlash();
  }

  public void resetEncoder(){
    turretEncoder.setPosition(0);
  }

  public void setPosition(double position){
    turretPID.setReference(position, CANSparkMax.ControlType.kPosition);
  }

  public double getPosition(){
    return turretEncoder.getPosition();
  }

  public void stop(){
    turretMotor.stopMotor();
  }

  public void teleOp(double speed){
    turretMotor.set(speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Turret Position", getPosition());
  }
}
