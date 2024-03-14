package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.classes.LimelightHelpers;
import frc.robot.subsystems.subShooter;
import frc.robot.subsystems.subShotAngle;
import frc.robot.subsystems.subTurret;

public class cmdAuto_AlignToShoot extends Command {
  subShotAngle angle;
  subTurret turret;
  subShooter shooter;
  PIDController anglePID = new PIDController(0.005, 0, 0);
  public cmdAuto_AlignToShoot(subShotAngle angle, subTurret turret, subShooter shooter) {
    this.angle = angle;
    this.turret = turret;
    this.shooter = shooter;
    anglePID.setIntegratorRange(-0.2, 0.2);
    anglePID.setTolerance(1);
    addRequirements(angle, turret, shooter);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if(hasTarget()){
      turret.teleOp(turretCorrection());
      angle.teleOp(angleCorrection());
      shooter.setVelocity(shooterSpeed());
    }
    else{
      angle.teleOp(-anglePID.calculate(angle.getPosition(), Constants.ShotAngleConstants.SafeZoneShot));
    }
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }

  public boolean hasTarget(){
    return LimelightHelpers.getTV("limelight");
  }

  public double turretCorrection()
  {    
    return MathUtil.clamp(LimelightHelpers.getTX("limelight"), -0.3, 0.3);
  }

  public double angleCorrection()
  {
    return MathUtil.clamp(LimelightHelpers.getTY("limelight"), -0.3, 0.3);
  }

  public double shooterSpeed(){
    return MathUtil.clamp(LimelightHelpers.getTA("limelight") * 5000, 3000, 5000);
  }
}