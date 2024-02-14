package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdShotAngle_Stop extends Command {
  private subShotAngle shotAngle;
  public cmdShotAngle_Stop(subShotAngle shotAngle) {
    this.shotAngle = shotAngle;
    addRequirements(shotAngle);
  }

  @Override
  public void initialize() {
    shotAngle.stop();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
    shotAngle.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
