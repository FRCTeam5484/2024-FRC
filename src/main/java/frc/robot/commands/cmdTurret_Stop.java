package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subTurret;

public class cmdTurret_Stop extends Command {
  subTurret turret;
  public cmdTurret_Stop(subTurret turret) {
    this.turret = turret;
    addRequirements(turret);
  }

  @Override
  public void initialize() { turret.stop(); }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
