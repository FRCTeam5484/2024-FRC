package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subClimb;

public class cmdClimb_Stop extends Command {
  subClimb climb;
  public cmdClimb_Stop(subClimb climb) {
    this.climb = climb;
    addRequirements(climb);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {
    climb.stop();
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
