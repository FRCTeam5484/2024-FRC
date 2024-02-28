package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShooter;

public class cmdShooter_Stop extends Command {
  subShooter shooter;
  public cmdShooter_Stop(subShooter shooter) {
    this.shooter = shooter;
    addRequirements(shooter);
  }

  @Override
  public void initialize() { shooter.stop(); }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
