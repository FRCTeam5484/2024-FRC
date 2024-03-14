package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.subShooter;

public class cmdAuto_ShootVelocity extends Command {
  subShooter shooter;
  Double velocity;
  public cmdAuto_ShootVelocity(subShooter shooter, Double velocity) {
    this.shooter = shooter;
    this.velocity = velocity;
    addRequirements(shooter);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shooter.setVelocity(velocity);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
