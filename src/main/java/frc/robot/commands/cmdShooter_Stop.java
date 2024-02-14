package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShooter;

public class cmdShooter_Stop extends Command {
  private final subShooter shoot;

  public cmdShooter_Stop(subShooter shoot) {
    this.shoot = shoot;
    addRequirements(shoot);
  }

  @Override
  public void initialize() {
    shoot.stop();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
