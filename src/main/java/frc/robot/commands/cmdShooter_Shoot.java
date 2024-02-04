package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subFeeder;
import frc.robot.subsystems.subShooter;

public class cmdShooter_Shoot extends Command {
  private final subFeeder feed;
  private final subShooter shoot;
  private Timer time;

  public cmdShooter_Shoot(subFeeder feed, subShooter shoot) {
    this.feed = feed;
    this.shoot = shoot;
    addRequirements(feed, shoot);
  }

  @Override
  public void initialize() {
    time.reset();
    time.start();
  }

  @Override
  public void execute() {
    shoot.set(1);
    if(time.get() > 1){
      feed.forward();  
    }
  }

  @Override
  public void end(boolean interrupted) {
    shoot.stop();
    feed.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
