package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class subShotAngle extends SubsystemBase {
  private final int kShotAngleId = 13;
  private final CANSparkMax shotAngleMotor = new CANSparkMax(kShotAngleId, MotorType.kBrushless);
  private final AbsoluteEncoder shotAngleEncoder = shotAngleMotor.getAbsoluteEncoder(Type.kDutyCycle);

  public subShotAngle() {
    shotAngleMotor.restoreFactoryDefaults();
    shotAngleMotor.setIdleMode(IdleMode.kBrake);
    shotAngleMotor.setInverted(true);
    shotAngleMotor.burnFlash();
  }

  public void stop(){
    shotAngleMotor.stopMotor();
  }

  public void teleOp(double speed){
    if(speed > 0 && getPosition() < Constants.ShotAngleConstants.HigherLimit || speed < 0 && getPosition() > Constants.ShotAngleConstants.LowerLimit){
      shotAngleMotor.set(0);
    }
    else{
      shotAngleMotor.set(MathUtil.clamp(speed, -0.2, 0.2));
    }
  }

  public double getPosition(){
    return shotAngleEncoder.getPosition() * 360;
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Turret Safe", getPosition() <= Constants.ShotAngleConstants.MinimumForTurret);
    SmartDashboard.putNumber("Shot Angle Power", shotAngleMotor.get());
    SmartDashboard.putNumber("Shot Angle Position", getPosition());
  }
}
