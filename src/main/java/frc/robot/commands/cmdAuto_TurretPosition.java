package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.subShotAngle;
import frc.robot.subsystems.subTurret;

public class cmdAuto_TurretPosition extends Command {
  subTurret turret;
  subShotAngle angle;
  double goal;
  PIDController turretPID = new PIDController(0.015, 0, 0);
  PIDController anglePID = new PIDController(0.005, 0, 0);
  public cmdAuto_TurretPosition(subTurret turret, subShotAngle angle, double goal) {
    this.turret = turret;
    this.angle = angle;
    this.goal = goal;
    addRequirements(turret, angle);
    anglePID.setTolerance(2);
    anglePID.setIntegratorRange(-0.3, 0.3);
    turretPID.setTolerance(5);
    turretPID.setIntegratorRange(-0.3, 0.3);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    angle.teleOp(-anglePID.calculate(angle.getPosition(), Constants.ShotAngleConstants.HigherLimit));
    if(angle.safeToTurret()){
      turret.teleOp(turretPID.calculate(turret.getPosition(), goal));
    } else {
      turret.stop();
    }
  }

  @Override
  public void end(boolean interrupted) {
    turret.stop();
    angle.stop();
  }

  @Override
  public boolean isFinished() {
    return turretPID.atSetpoint();
  }
}
