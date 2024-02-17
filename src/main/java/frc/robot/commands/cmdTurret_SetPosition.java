package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subTurret;

public class cmdTurret_SetPosition extends Command {
  subTurret turret;
  double goal;
  PIDController turretPID = new PIDController(0.05, 0, 0);
  public cmdTurret_SetPosition(subTurret turret, double goal) {
    this.turret = turret;
    this.goal = goal;
    addRequirements(turret);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    turret.teleOp(turretPID.calculate(turret.getPosition(), goal));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}