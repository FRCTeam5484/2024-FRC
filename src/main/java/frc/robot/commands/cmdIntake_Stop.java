package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subIntake;

public class cmdIntake_Stop extends Command {
  subIntake intake;
  public cmdIntake_Stop(subIntake intake) {
    this.intake = intake;
    addRequirements(intake);
  }

  @Override
  public void initialize() { intake.stop(); }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
