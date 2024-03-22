package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subLimeLight;
import frc.robot.subsystems.subTurret;

public class cmdAuto_LimelightTargeting extends Command {
  subTurret turret;
  subLimeLight lime;
  public cmdAuto_LimelightTargeting(subTurret turret, subLimeLight lime) {
    this.turret = turret;
    this.lime = lime;
    addRequirements(turret, lime);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if(lime.hasTarget()){
      turret.teleOp(lime.pidCorrection());
    }
    else{
      turret.stop();
    }
  }

  @Override
  public void end(boolean interrupted) {
    turret.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
