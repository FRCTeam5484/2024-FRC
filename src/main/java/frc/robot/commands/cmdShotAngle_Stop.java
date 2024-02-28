package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdShotAngle_Stop extends Command {
  subShotAngle angle;
  public cmdShotAngle_Stop(subShotAngle angle) {
    this.angle = angle;
    addRequirements(angle);
  }

  @Override
  public void initialize() { angle.stop(); }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
