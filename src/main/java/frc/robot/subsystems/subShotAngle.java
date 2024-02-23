package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subShotAngle extends SubsystemBase {
  private final int kShotAngleId = 13;
  private final CANSparkMax shotAngleMotor = new CANSparkMax(kShotAngleId, MotorType.kBrushless);
  private final AbsoluteEncoder shotAngleEncoder = shotAngleMotor.getAbsoluteEncoder(Type.kDutyCycle);
  public final SparkPIDController shotAnglePID = shotAngleMotor.getPIDController();
  //private final int kMinimumPosition = 0;
  //private final int kMaximumPosition = 100;

  public subShotAngle() {
    shotAngleMotor.restoreFactoryDefaults();
    shotAngleMotor.setIdleMode(IdleMode.kBrake);
    shotAngleMotor.setInverted(true);
    shotAnglePID.setFeedbackDevice(shotAngleEncoder);
    shotAnglePID.setP(0.1);
    shotAnglePID.setI(1e-4);
    shotAnglePID.setD(1);
    shotAnglePID.setIZone(0);
    shotAnglePID.setFF(0);
    shotAnglePID.setOutputRange(-1, 1);
    shotAngleMotor.burnFlash();
  }

  public void stop(){
    shotAngleMotor.stopMotor();
  }

  public void teleOp(double speed){
    if(speed > 0 && getPosition() < 0.61 || speed < 0 && getPosition() > .7){
      shotAngleMotor.set(0);
    }
    else{
      shotAngleMotor.set(speed);
    }
    
  }

  public double getPosition(){
    return shotAngleEncoder.getPosition();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shot Angle Power", shotAngleMotor.get());
    SmartDashboard.putNumber("Shot Angle Position", getPosition());
  }
}
