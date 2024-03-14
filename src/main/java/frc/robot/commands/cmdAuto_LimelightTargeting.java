package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.subLimeLight;
import frc.robot.subsystems.subShotAngle;
import frc.robot.subsystems.subTurret;

public class cmdAuto_LimelightTargeting extends Command {
  subTurret turret;
  subLimeLight lime;
  subShotAngle angle;
  PIDController turretPID = new PIDController(0.02, 0, 0);
  PIDController anglePID = new PIDController(0.004, 0, 0);
  public cmdAuto_LimelightTargeting(subTurret turret, subLimeLight lime, subShotAngle angle) {
    this.turret = turret;
    this.lime = lime;
    this.angle = angle;
    addRequirements(turret, lime, angle);

    turretPID.setIntegratorRange(-0.2, 0.2);
    anglePID.setIntegratorRange(-0.08, 0.3);
    anglePID.setTolerance(1);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    angle.teleOp(-anglePID.calculate(angle.getPosition(), 500));
    if(angle.safeToTurret()){
      turret.teleOp(-turretPID.calculate(lime.getX(), 0));
    } else {
      turret.stop();
    }
  }

  @Override
  public void end(boolean interrupted) {
    turret.stop();
    angle.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
