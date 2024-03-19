package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subTurret;

public class cmdAuto_TurretPosition extends Command {
  subTurret turret;
  double goal;
  PIDController turretPID = new PIDController(0.02, 0, 0);
  public cmdAuto_TurretPosition(subTurret turret, double goal) {
    this.turret = turret;
    this.goal = goal;
    addRequirements(turret);
    turretPID.setIntegratorRange(-0.3, 0.3);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    turret.teleOp(turretPID.calculate(turret.getPosition(), goal));
  }

  @Override
  public void end(boolean interrupted) {
    turret.stop();
  }

  @Override
  public boolean isFinished() {
    return turretPID.atSetpoint();
  }
}
