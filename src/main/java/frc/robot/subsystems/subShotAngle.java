package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
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
    if(speed > 0 && getPosition() <= Constants.ShotAngleConstants.MaxPostition || speed < 0 && getPosition() > Constants.ShotAngleConstants.LowestPosition){
      shotAngleMotor.stopMotor();
    }
    else{
      shotAngleMotor.set(MathUtil.clamp(speed, -0.1, 0.2));
    }
  }

  public double getPosition(){
    return shotAngleEncoder.getPosition() * 360;
  }

  public boolean safeToTurret(){
    return getPosition() <= Constants.ShotAngleConstants.TurretSafe;
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Turret Safe", safeToTurret());
    SmartDashboard.putNumber("Shot Angle Power", shotAngleMotor.get());
    SmartDashboard.putNumber("Shot Angle Position", getPosition());
    //if(SmartDashboard.getBoolean("Shot Angle Brake Mode", false) && shotAngleMotor.getIdleMode() == IdleMode.kBrake){
    //  shotAngleMotor.setIdleMode(IdleMode.kCoast);
    //}
    //else if(SmartDashboard.getBoolean("Shot Angle Brake Mode", true) && shotAngleMotor.getIdleMode() == IdleMode.kCoast){
    //  shotAngleMotor.setIdleMode(IdleMode.kBrake);
    //}
  }
}
