package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdAuto_HoldShotAngle extends Command {
  subShotAngle angle;
  double goal;
  PIDController anglePID = new PIDController(0.005, 0, 0);
  public cmdAuto_HoldShotAngle(subShotAngle angle) {
    this.angle = angle;
    goal = angle.getPosition();
    anglePID.setIntegratorRange(-0.3, 0.3);
    anglePID.setTolerance(2);
    addRequirements(angle);
  }

  @Override
  public void initialize() { }

  @Override
  public void execute() {
    angle.teleOp(anglePID.calculate(angle.getPosition(), goal));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
