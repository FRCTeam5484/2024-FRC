package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subFeeder;
import frc.robot.subsystems.subShooter;

public class cmdShooter_Stop extends Command {
  private final subFeeder feed;
  private final subShooter shoot;

  public cmdShooter_Stop(subFeeder feed, subShooter shoot) {
    this.feed = feed;
    this.shoot = shoot;
    addRequirements(feed, shoot);
  }

  @Override
  public void initialize() {
    shoot.stop();
    feed.stop();
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
