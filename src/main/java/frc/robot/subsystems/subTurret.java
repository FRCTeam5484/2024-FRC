package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subTurret extends SubsystemBase {
  private final int kTurretMotorId = 9;
  private final CANSparkMax turretMotor = new CANSparkMax(kTurretMotorId, MotorType.kBrushless);
  private final RelativeEncoder turretEncoder = turretMotor.getEncoder();
  private final SparkLimitSwitch forwardLimit = turretMotor.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
  private final SparkLimitSwitch reverseLimit = turretMotor.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);

  public subTurret() {
    turretMotor.restoreFactoryDefaults();
    turretMotor.setIdleMode(IdleMode.kBrake);
    turretMotor.setInverted(true);
    forwardLimit.enableLimitSwitch(true);
    reverseLimit.enableLimitSwitch(true);
    turretMotor.setSoftLimit(SoftLimitDirection.kForward, 120);
    turretMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
    turretMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    turretMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    turretMotor.burnFlash();
  }

  public void resetEncoder(){
    turretEncoder.setPosition(0);
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
    /*
    SmartDashboard.putNumber("Turret Position", getPosition());
    SmartDashboard.putNumber("Turret Power", turretMotor.get());
    
    SmartDashboard.putBoolean("Turret Forward Limit", forwardLimit.isPressed());
    SmartDashboard.putBoolean("Turret Reverse Limit", reverseLimit.isPressed());
    if(reverseLimit.isPressed()){
      resetEncoder();
    }
    if(SmartDashboard.getBoolean("Turret Brake Mode", false) && turretMotor.getIdleMode() == IdleMode.kBrake){
      turretMotor.setIdleMode(IdleMode.kCoast);
    }
    else if(SmartDashboard.getBoolean("Turret Brake Mode", true) && turretMotor.getIdleMode() == IdleMode.kCoast){
      turretMotor.setIdleMode(IdleMode.kBrake);
    }
    */
  }
}