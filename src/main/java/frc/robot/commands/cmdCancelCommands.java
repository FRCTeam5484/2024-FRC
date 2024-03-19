package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subIntake;
import frc.robot.subsystems.subShooter;
import frc.robot.subsystems.subTurret;

public class cmdCancelCommands extends Command {
  subIntake intake;
  subShooter shooter;
  subTurret turret;
  public cmdCancelCommands(subIntake intake, subShooter shooter, subTurret turret) {
    this.intake = intake;
    this.shooter = shooter;
    this.turret = turret;
    addRequirements(intake, shooter, turret);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
