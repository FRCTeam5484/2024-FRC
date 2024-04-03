package frc.robot.commands;

import java.util.Optional;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
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
    if(DriverStation.getAlliance().isPresent()){
      Optional<Alliance> ally = DriverStation.getAlliance();
      if(ally.get() == Alliance.Blue){
        lime.setBluePipeline();
      }
      else{
        lime.setRedPipeline();
      }
    }
    else{
      lime.setDriverPipeline();
    }

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
