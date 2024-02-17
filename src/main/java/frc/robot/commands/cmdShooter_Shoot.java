package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShooter;

public class cmdShooter_Shoot extends Command {
  private final subShooter shoot;

  public cmdShooter_Shoot(subShooter shoot) {
    this.shoot = shoot;
    addRequirements(shoot);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    shoot.set(.8);
  }

  @Override
  public void end(boolean interrupted) {
    shoot.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
