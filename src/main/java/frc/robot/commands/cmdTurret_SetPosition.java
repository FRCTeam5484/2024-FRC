package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.subShotAngle;
import frc.robot.subsystems.subTurret;

public class cmdTurret_SetPosition extends Command {
  subTurret turret;
  subShotAngle angle;
  double goal;
  PIDController turretPID = new PIDController(0.015, 0, 0);
  PIDController anglePID = new PIDController(0.005, 0, 0);
  public cmdTurret_SetPosition(subTurret turret, subShotAngle angle, double goal) {
    this.turret = turret;
    this.angle = angle;
    this.goal = goal;
    addRequirements(turret, angle);
    anglePID.setTolerance(5);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    angle.teleOp(-anglePID.calculate(angle.getPosition(), Constants.ShotAngleConstants.HigherLimit));
    if(anglePID.atSetpoint()) {
      turret.teleOp(turretPID.calculate(turret.getPosition(), goal));
    }
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}