package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdShotAngle_Raise extends Command {
  private subShotAngle shotAngle;
  public cmdShotAngle_Raise(subShotAngle shotAngle) {
    this.shotAngle = shotAngle;
    addRequirements(shotAngle);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shotAngle.teleOp(.5);
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
