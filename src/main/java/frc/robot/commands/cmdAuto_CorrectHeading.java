package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subSwerve;

public class cmdAuto_CorrectHeading extends Command {
  subSwerve swerve;
  double offset;
  public cmdAuto_CorrectHeading(subSwerve swerve, double offset) {
    this.swerve = swerve;
    this.offset = offset;
    addRequirements(swerve);
  }

  @Override
  public void initialize() {
    swerve.setGryoOffset(offset);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
