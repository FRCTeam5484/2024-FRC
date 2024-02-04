package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subShotAngle extends SubsystemBase {
  private final int kShotAngleId = 13;
  private final CANSparkMax shotAngleMotor;
  private final RelativeEncoder shotAngleEncoder;
  private final SparkPIDController shotAnglePID;
  private final int kMinimumPosition = 0;
  private final int kMaximumPosition = 100;

  public subShotAngle() {
    shotAngleMotor = new CANSparkMax(kShotAngleId, MotorType.kBrushless);
    shotAngleMotor.restoreFactoryDefaults();
    shotAngleMotor.setIdleMode(IdleMode.kBrake);
    shotAngleMotor.setInverted(false);
    shotAngleEncoder = shotAngleMotor.getEncoder();
    shotAnglePID = shotAngleMotor.getPIDController();
    shotAnglePID.setFeedbackDevice(shotAngleEncoder);
    shotAnglePID.setP(0.1);
    shotAnglePID.setI(1e-4);
    shotAnglePID.setD(1);
    shotAnglePID.setIZone(0);
    shotAnglePID.setFF(0);
    shotAnglePID.setOutputRange(-1, 1);
    shotAngleMotor.burnFlash();
  }

  public void setPosition(double position){
    shotAnglePID.setReference(position, CANSparkMax.ControlType.kPosition);
  }

  public void stop(){
    shotAngleMotor.stopMotor();
  }

  public void teleOp(double speed){
    shotAngleMotor.set(speed);
  }

  public double getPosition(){
    return shotAngleEncoder.getPosition();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shot Angle Position", getPosition());
  }
}
