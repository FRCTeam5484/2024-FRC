package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subClimb extends SubsystemBase {
  private final CANSparkMax leftMotor = new CANSparkMax(14, MotorType.kBrushless);
  private final CANSparkMax rightMotor = new CANSparkMax(15, MotorType.kBrushless);
  private final RelativeEncoder leftEncoder = leftMotor.getEncoder();
  private final RelativeEncoder rightEncoder = rightMotor.getEncoder();
  private final Servo armRelease = new Servo(0);

  public subClimb() {
    leftMotor.restoreFactoryDefaults();
    rightMotor.restoreFactoryDefaults();
    leftMotor.setIdleMode(IdleMode.kBrake);
    rightMotor.setIdleMode(IdleMode.kBrake);
    leftMotor.burnFlash();
    rightMotor.burnFlash();
  }

  public void resetEncoder(){
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  public void closeServo(){
    armRelease.set(0);
  }

  public void openServo(){
    armRelease.set(180);
  }

  public void teleOp(double speed){
    leftMotor.set(speed);
    rightMotor.set(speed);
  }

  public void stop(){
    leftMotor.stopMotor();
    rightMotor.stopMotor();
  }

  @Override
  public void periodic() {
    //SmartDashboard.putNumber("Climb Left Position", leftEncoder.getPosition());
    //SmartDashboard.putNumber("Climb Right Position", rightEncoder.getPosition());
    //SmartDashboard.putNumber("Climb Left Power", leftMotor.get());
    //SmartDashboard.putNumber("Climb Right Power", rightMotor.get());
  }
}
