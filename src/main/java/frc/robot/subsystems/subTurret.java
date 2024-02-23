package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkLimitSwitch;
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
  private final SparkLimitSwitch forwardLimit = turretMotor.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
  private final SparkLimitSwitch reverseLimit = turretMotor.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);

  public subTurret() {
    turretMotor.restoreFactoryDefaults();
    turretMotor.setIdleMode(IdleMode.kBrake);
    turretMotor.setInverted(false);
    forwardLimit.enableLimitSwitch(true);
    reverseLimit.enableLimitSwitch(true);
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
    SmartDashboard.putNumber("Turret Position", getPosition());
    if(reverseLimit.isPressed()){
      resetEncoder();
    }
  }
}