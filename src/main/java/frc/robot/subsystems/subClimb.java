package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subClimb extends SubsystemBase {
  private final CANSparkMax leftMotor = new CANSparkMax(14, MotorType.kBrushless);
  private final CANSparkMax rightMotor = new CANSparkMax(15, MotorType.kBrushless);
  private final RelativeEncoder leftEncoder = leftMotor.getEncoder();
  private final RelativeEncoder rightEncoder = rightMotor.getEncoder();

  public subClimb() {
    leftMotor.restoreFactoryDefaults();
    rightMotor.restoreFactoryDefaults();
    leftMotor.setIdleMode(IdleMode.kBrake);
    rightMotor.setIdleMode(IdleMode.kBrake);
    leftMotor.burnFlash();
    rightMotor.burnFlash();
    
    /*
    leftMotor.setSoftLimit(SoftLimitDirection.kForward, 100);
    leftMotor.setSoftLimit(SoftLimitDirection.kReverse, -10);
    leftMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    leftMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    rightMotor.setSoftLimit(SoftLimitDirection.kForward, 100);
    rightMotor.setSoftLimit(SoftLimitDirection.kReverse, -10);
    rightMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    rightMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    */
  }

  public void resetEncoder(){
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  public void teleOp(double leftSpeed, double rightSpeed){
    leftMotor.set(leftSpeed);
    rightMotor.set(rightSpeed);
  }

  public void stop(){
    rightMotor.stopMotor();
    leftMotor.stopMotor();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Climb Left Position", leftEncoder.getPosition());
    SmartDashboard.putNumber("Climb Right Position", rightEncoder.getPosition());
  }
}
